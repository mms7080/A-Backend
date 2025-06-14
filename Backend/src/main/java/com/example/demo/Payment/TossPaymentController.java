package com.example.demo.Payment;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.BookingStatus;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Payment.EmailService;
import com.example.demo.Reservation.ReservationRepository;
import com.example.demo.User.UserService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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
    private final BookingRepository bookingRepository; // 추가
    private final ReservationRepository reservationRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public TossPaymentController(PaymentRepository repository, BookingRepository bookingRepository,ReservationRepository reservationRepo) { // 생성자 수정
        this.repository = repository;
        this.bookingRepository = bookingRepository; // 추가
        this.reservationRepo = reservationRepo; // 추가
    }

    @PostConstruct
    public void insertDummyPayments() {
        if (repository.count() > 0)
            return;

        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Payment p1 = new Payment(null, "dummyKey1", "order-111", 12000, "root", "일반관람권", "DONE", now, "카드", "현대카드",
                "1111-****-****-1111","CONFIRMED");
        Payment p2 = new Payment(null, "dummyKey2", "order-112", 15000, "root", "더블콤보", "DONE", now, "카드", "삼성카드",
                "2222-****-****-2222","CONFIRMED");
        Payment p3 = new Payment(null, "dummyKey3", "order-113", 18000, "root", "러브콤보", "DONE", now, "기타", null, null,"CONFIRMED");

        repository.saveAll(List.of(p1, p2, p3));
        System.out.println("✅ 결제 더미 데이터 3건 자동 삽입 완료");
    }

    @PostMapping("/confirm/store")
    public ResponseEntity<?> confirmStorePayment(@RequestBody TossConfirmRequest req) {
        return confirmGeneric(req, "store");
    }

    @PostMapping("/confirm/reservation")
    public ResponseEntity<?> confirmReservationPayment(@RequestBody TossConfirmRequest req) {
        return confirmGeneric(req, "reservation");
    }

    private ResponseEntity<?> confirmGeneric(TossConfirmRequest req, String type) {
        try {
            System.out.println("✅ 결제 승인 시도 (" + type + ")");
            System.out.println("paymentKey: " + req.getPaymentKey());
            System.out.println("orderId: " + req.getOrderId());
            System.out.println("amount: " + req.getAmount());
            System.out.println("userId(username): " + req.getUserId());

            Optional<Payment> existing = repository.findByPaymentKey(req.getPaymentKey());
            if (existing.isPresent()) {
                System.out.println("⚠️ 이미 승인된 결제");
                return ResponseEntity.ok(existing.get());
            }

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
                    Map.class);

            Map<String, Object> res = response.getBody();

            Payment payment = new Payment();
            payment.setPaymentKey((String) res.get("paymentKey"));
            payment.setOrderId((String) res.get("orderId"));
            payment.setAmount(((Number) res.get("totalAmount")).intValue());
            payment.setOrderName((String) res.getOrDefault("orderName", "영화 예매"));
            payment.setApprovedAt((String) res.get("approvedAt"));
            payment.setStatus((String) res.get("status"));
            payment.setRefundstatus("CONFIRMED");

            if (res.get("card") instanceof Map card) {
                payment.setMethod("카드");
                payment.setCardCompany((String) card.get("company"));
                payment.setCardNumber((String) card.get("number"));
            } else {
                payment.setMethod("기타");
            }

            payment.setUserId(req.getUserId()); // username
            repository.save(payment);

            // ✅ 예매(Booking) 상태 업데이트 로직 추가 (reservation 타입일 때만)
            if ("reservation".equals(type)) {
                try {
                    // orderId가 bookingId라고 가정하고 파싱
                    Long bookingId = Long.parseLong(req.getOrderId()); 
                    Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

                    // 1. Booking 상태를 CONFIRMED로 변경
                    booking.setStatus(BookingStatus.CONFIRMED);
                    
                    // 2. 최종 결제 금액 업데이트
                    booking.setTotalPrice(new BigDecimal(req.getAmount()));

                    // 3. 연결된 좌석들의 상태를 RESERVED로 변경
                    if (booking.getSelectedSeats() != null) {
                        booking.getSelectedSeats().forEach(seat -> seat.setStatus(SeatStatus.RESERVED));
                    }
                    bookingRepository.save(booking); // 변경된 booking과 seat 상태를 함께 저장 (cascade)

                    System.out.println("✅ Booking ID " + bookingId + " status updated to CONFIRMED and seats to RESERVED.");

                } catch (NumberFormatException e) {
                    System.err.println("❌ Order ID is not a valid Booking ID: " + req.getOrderId());
                    // 에러 처리...
                } catch (Exception e) {
                    System.err.println("❌ Booking status update failed: " + e.getMessage());
                    e.printStackTrace();
                    // 에러 처리...
                }
            }

            // ✅ 이메일 발송 (reservation 전용)
            if (type.equals("reservation")) {
                try {
                    String email = userService.getEmailById(req.getUserId());
                    System.out.println("📧 조회된 이메일: " + email);

                    if (email == null || email.isBlank()) {
                        System.err.println("❌ 이메일 조회 실패: 유효한 username 아님");
                    } else {
                        String content = String.format("""
                                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                                    <h2>🎬 FILMORA 예매가 성공적으로 완료되었습니다!</h2>
                                    <h3>[예매 정보]</h3>
                                    <p><strong>예매 번호:</strong> %s</p>
                                    <p><strong>결제 금액:</strong> %,d원</p>
                                    <p><strong>결제 수단:</strong> %s (%s)</p>
                                    <p><strong>결제 일시:</strong> %s</p>
                                    <hr>
                                    <p>예매 내역은 마이페이지에서 확인하실 수 있습니다.<br>감사합니다!</p>
                                </div>
                                """,
                                payment.getOrderId(),
                                payment.getAmount(),
                                payment.getMethod(),
                                payment.getCardCompany() != null ? payment.getCardCompany() : "기타",
                                payment.getApprovedAt());

                        emailService.sendReservationSuccessEmail(
                                email,
                                "🎟️ 영화 예매 완료 안내",
                                content);
                        System.out.println("✅ 예매 이메일 발송 성공");
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ 이메일 발송 중 예외 발생: " + e.getMessage());
                }
            }

            return ResponseEntity.ok(payment);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ 결제 승인 실패: " + e.getMessage());
        }
    }

    @Data
    static class TossConfirmRequest {
        private String paymentKey;
        private String orderId;
        private int amount;
        private String userId; // 반드시 username 값이 들어가야 함
    }

    @PatchMapping("/refund/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        if (!repository.existsById(paymentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 결제 내역이 존재하지 않습니다.");
        }

        Optional<Payment> optionalPayment = repository.findById(paymentId);

        Payment payment = optionalPayment.get();

        payment.setRefundstatus("CANCELED");
        payment.setApprovedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        repository.save(payment); // 변경 사항 저장

        // 💳 관련 예매 정보 환불 처리
        String orderId = payment.getOrderId();
        reservationRepo.findByOrderId(orderId).ifPresent(reservation -> {
            reservation.setStatus("CANCELED");
            reservation.setApprovedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            reservationRepo.save(reservation);
            System.out.println("💳 예매 환불 처리 완료: " + orderId);
        });

        return ResponseEntity.ok("환불 처리 완료");
    }
}
