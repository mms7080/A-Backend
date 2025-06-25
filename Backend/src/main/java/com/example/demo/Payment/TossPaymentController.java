package com.example.demo.Payment;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.BookingStatus;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Payment.EmailService;
import com.example.demo.Reservation.ReservationRepository;
import com.example.demo.User.UserService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TossPaymentController {

    private static final String SECRET_KEY = "test_sk_DpexMgkW36vnlW1bALgB3GbR5ozO";
    private final PaymentRepository repository;
    private final BookingRepository bookingRepository; // 추가
    private final ReservationRepository reservationRepo;
    private final SeatRepository seatRepository; // 추가

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public TossPaymentController(PaymentRepository repository, BookingRepository bookingRepository,ReservationRepository reservationRepo, SeatRepository seatRepository) { // 생성자 수정
        this.repository = repository;
        this.bookingRepository = bookingRepository; // 추가
        this.reservationRepo = reservationRepo; // 추가
        this.seatRepository = seatRepository;
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

            String realOrderId = req.getOrderId();

            Map<String, String> body = new HashMap<>();
            body.put("paymentKey", req.getPaymentKey());
            body.put("orderId", realOrderId);
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
            payment.setOrderId(req.getOrderId());
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

            payment.setUserId(req.getUserId());
            Payment savedPayment = repository.save(payment);

            if ("reservation".equals(type)) {
                try {
                    Long bookingId = Long.parseLong(realOrderId);
                    Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
                    
                    booking.setPayment(savedPayment);
                    booking.setStatus(BookingStatus.CONFIRMED);
                    booking.setTotalPrice(new BigDecimal(req.getAmount()));

                    if (booking.getSelectedSeats() != null) {
                        booking.getSelectedSeats().forEach(seat -> seat.setStatus(SeatStatus.RESERVED));
                    }
                    bookingRepository.save(booking);

                    System.out.println("✅ Booking ID " + bookingId + " status updated to CONFIRMED and seats to RESERVED.");

                } catch (NumberFormatException e) {
                    System.err.println("❌ Order ID is not a valid Booking ID: " + realOrderId);
                } catch (Exception e) {
                    System.err.println("❌ Booking status update failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            // ... (이메일 발송 로직 생략)
            return ResponseEntity.ok(savedPayment);

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
        private String userId;
    }

    @Transactional
    @PatchMapping("/refund/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        
        // 1. paymentId로 Payment 정보 조회
        Optional<Payment> optionalPayment = repository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 결제 내역이 존재하지 않습니다.");
        }
        Payment payment = optionalPayment.get();

        // 2. Payment와 연결된 Booking 정보 조회
        Optional<Booking> optionalBooking = bookingRepository.findByPaymentId(payment.getId());
        if(optionalBooking.isPresent()){
            Booking booking = optionalBooking.get();
            booking.setStatus(BookingStatus.CANCELLED_BY_USER);

            // 3. 연결된 좌석 상태 변경
            if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
                List<Long> seatIds = booking.getSelectedSeats().stream()
                                            .map(Seat::getId)
                                            .collect(Collectors.toList());
                if (!seatIds.isEmpty()) {
                    seatRepository.updateSeatStatusByIds(seatIds, SeatStatus.AVAILABLE);
                    System.out.println("✅ [Booking] 좌석 상태 변경 완료: " + seatIds);
                }
            }
            bookingRepository.save(booking);
            System.out.println("💳 [Booking] 예매 환불 처리 완료: " + booking.getId());
        } else {
            System.out.println("ℹ️ paymentId " + paymentId + "에 연결된 Booking 정보가 없습니다.");
        }
        
        // 4. Payment 상태 변경
        payment.setRefundstatus("CANCELED");
        repository.save(payment);
        System.out.println("💳 [Payment] 결제 환불 처리 완료: " + payment.getId());

        // 5. 레거시 Reservation 시스템 상태 변경
        reservationRepo.findByOrderId(payment.getOrderId()).ifPresent(reservation -> {
            reservation.setStatus("CANCELED");
            reservationRepo.save(reservation);
            System.out.println("💳 [Reservation] 레거시 예매 환불 처리 완료: " + reservation.getOrderId());
        });

        return ResponseEntity.ok("환불 처리 완료");
    }
}
