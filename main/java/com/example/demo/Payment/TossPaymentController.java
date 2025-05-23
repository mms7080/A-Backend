package com.example.demo.Payment;

import lombok.Data;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TossPaymentController {

    private static final String SECRET_KEY = "test_sk_DpexMgkW36vnlW1bALgB3GbR5ozO";
    private final PaymentRepository repository;

    public TossPaymentController(PaymentRepository repository) {
        this.repository = repository;
    }

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
