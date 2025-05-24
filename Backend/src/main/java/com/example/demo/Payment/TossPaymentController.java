package com.example.demo.Payment;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TossPaymentController {

    private static final String SECRET_KEY = "test_sk_DpexMgkW36vnlW1bALgB3GbR5ozO";
    private final PaymentRepository repository;

    public TossPaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

    // ✅ 서버 실행 시 더미 결제 데이터 3건 삽입
    @PostConstruct
    public void insertDummyPayments() {
        if (repository.count() > 0) return;

        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Payment p1 = new Payment(null, "dummyKey1", "order-111", 12000, "1", "일반관람권", "DONE", now, "카드", "현대카드", "1111-****-****-1111");
        Payment p2 = new Payment(null, "dummyKey2", "order-112", 15000, "2", "더블콤보", "DONE", now, "카드", "삼성카드", "2222-****-****-2222");
        Payment p3 = new Payment(null, "dummyKey3", "order-113", 18000, "2", "러브콤보", "DONE", now, "기타", null, null);

        repository.saveAll(List.of(p1, p2, p3));
        System.out.println("✅ 결제 더미 데이터 3건 자동 삽입 완료");
    }

    // ✅ 실제 Toss 결제 승인 처리
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody TossConfirmRequest req) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String auth = Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + auth);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("paymentKey", req.getPaymentKey());
            body.put("orderId", req.getOrderId());
            body.put("amount", String.valueOf(req.getAmount()));

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> res = response.getBody();

            Payment payment = new Payment();
            payment.setPaymentKey((String) res.get("paymentKey"));
            payment.setOrderId((String) res.get("orderId"));
            payment.setAmount((Integer) res.get("totalAmount"));
            payment.setOrderName((String) res.get("orderName"));
            payment.setApprovedAt((String) res.get("approvedAt"));
            payment.setStatus((String) res.get("status"));

            Map<String, Object> method = (Map<String, Object>) res.get("card");
            if (method != null) {
                payment.setMethod("카드");
                payment.setCardCompany((String) method.get("company"));
                payment.setCardNumber((String) method.get("number"));
            } else {
                payment.setMethod("기타");
            }

            payment.setUserId(req.getUserId());
            repository.save(payment);

            return ResponseEntity.ok(payment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 승인 실패: " + e.getMessage());
        }
    }

    @Data
    static class TossConfirmRequest {
        private String paymentKey;
        private String orderId;
        private int amount;
        private String userId;
    }
}
