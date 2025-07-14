package com.example.demo.Payment;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter; // 추가
import java.util.Arrays; // 추가
import java.util.Base64;
import java.util.HashMap;
import java.util.List; // 추가
import java.util.Map; // 추가
import java.util.Optional; // 추가
import java.util.stream.Collectors; // 추가

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // 추가
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.BookingStatus;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.Movie;
import com.example.demo.Movie.MovieRepository;
import com.example.demo.Reservation.ReservationRepository;
import com.example.demo.User.UserService;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TossPaymentController {

    private static final String SECRET_KEY = "test_sk_DpexMgkW36vnlW1bALgB3GbR5ozO";
    private final PaymentRepository repository;
    private final BookingRepository bookingRepository; // 추가
    private final ReservationRepository reservationRepo;
    private final SeatRepository seatRepository; // 추가
    private final ShowtimeRepository showtimeRepository; // 추가
    private final MovieRepository movieRepository;       // 추가
    private final TheaterRepository theaterRepository;   // 추가

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

     public TossPaymentController(PaymentRepository repository, BookingRepository bookingRepository,
                                 ReservationRepository reservationRepo, SeatRepository seatRepository,
                                 ShowtimeRepository showtimeRepository, MovieRepository movieRepository, TheaterRepository theaterRepository) { // 생성자 수정
        this.repository = repository;
        this.bookingRepository = bookingRepository;
        this.reservationRepo = reservationRepo;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository; // 추가
        this.movieRepository = movieRepository;       // 추가
        this.theaterRepository = theaterRepository;   // 추가
    }

    @PostConstruct
    public void insertDummyPayments() {
        if (repository.count() > 0)
            return;

        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Payment p1 = new Payment(null, "dummyKey1", "order-111", 14000, "root", "일반관람권", "DONE", now, "카드", "현대카드",
                "1111-****-****-1111","CONFIRMED");
        Payment p2 = new Payment(null, "dummyKey2", "order-112", 13900, "root", "더블콤보", "DONE", now, "카드", "삼성카드",
                "2222-****-****-2222","CONFIRMED");
        Payment p3 = new Payment(null, "dummyKey3", "order-113", 10900, "root", "러브콤보", "DONE", now, "기타", null, null,"CONFIRMED");

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
                    String bookingIdStr = realOrderId;
                    if (realOrderId.startsWith("movie-")) {
                        bookingIdStr = realOrderId.substring(6); // "movie-" 접두사 제거
                    }
                    Long bookingId = Long.parseLong(bookingIdStr);
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
@PatchMapping("/refund/order/{orderId}") // paymentId 대신 orderId 사용
public ResponseEntity<?> refundPaymentByOrderId(@PathVariable String orderId) {

    // 1. orderId로 Payment 정보 조회
    Optional<Payment> optionalPayment = repository.findByOrderId(orderId);
    if (optionalPayment.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 주문 내역이 존재하지 않습니다.");
    }
    Payment payment = optionalPayment.get();

    // 2. Reservation 정보 조회 및 상태 변경 (레거시 시스템 및 기본 정보)
    // Reservation은 항상 존재한다고 가정하거나, 없어도 Booking 처리와 독립적으로 진행
    Optional<com.example.demo.Reservation.Reservation> optionalReservation = reservationRepo.findByOrderId(orderId);
    if (optionalReservation.isEmpty()) {
        // Log this, but don't stop the refund if only Reservation is missing (unlikely, but for robustness)
        System.out.println("ℹ️ orderId " + orderId + "에 연결된 레거시 Reservation 정보가 없습니다.");
    }

    optionalReservation.ifPresent(reservation -> {
        reservation.setStatus("CANCELED");
        reservationRepo.save(reservation);
        System.out.println("💳 [Reservation] 레거시 예매 환불 처리 완료: " + reservation.getOrderId());
    });

    // 3. Booking 정보 조회 및 상태 변경 (신규 시스템)
    // 이 부분이 기존에 'Booking not found' 오류로 인해 좌석 업데이트가 안 되던 핵심
    if (orderId.startsWith("movie-")) {
        try {
            Long bookingId = Long.parseLong(orderId.substring(6)); // "movie-" 접두사 제거
            Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

            if(optionalBooking.isPresent()){ // Booking 정보가 있는 경우
                Booking booking = optionalBooking.get();
                booking.setStatus(BookingStatus.CANCELLED_BY_USER);

                // 3.1. 연결된 좌석 상태 변경 (Booking 기준으로)
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
            } else { // Booking 정보가 없는 경우, Reservation 정보를 사용하여 좌석 해제 시도 (대체 로직)
                System.out.println("ℹ️ orderId " + orderId + "에 연결된 Booking 정보가 없습니다. Reservation 정보를 사용하여 좌석 해제를 시도합니다.");
                if (optionalReservation.isPresent()) {
                    com.example.demo.Reservation.Reservation res = optionalReservation.get();
                    try {
                        // Reservation 데이터를 기반으로 Showtime 및 Movie 정보 조회
                        List<Theater> theaters = theaterRepository.findByName(res.getTheater());
                        if(theaters.isEmpty()) {
                            throw new RuntimeException("Fallback: 극장 정보를 찾을 수 없습니다: " + res.getTheater());
                        }
                        
                        Movie movie = movieRepository.findById(res.getMovieId())
                                .orElseThrow(() -> new RuntimeException("Fallback: 영화 정보를 찾을 수 없습니다: " + res.getMovieId()));

                        // 예약 날짜와 시간을 LocalDateTime으로 파싱
                        LocalDateTime showDateTime = LocalDateTime.parse(res.getDate() + "T" + res.getTime());

                        // 해당 극장, 영화, 시간대에 맞는 Showtime 조회 (시간대 오차 고려)
                        List<Showtime> showtimes = showtimeRepository
                            .findByTheaterAndMovieAndStartTimeBetweenOrderByStartTimeAsc(
                                theaters.get(0), movie, showDateTime.minusMinutes(1), showDateTime.plusMinutes(1));
                        
                        if(showtimes.isEmpty()) {
                            throw new RuntimeException("Fallback: 상영 시간표를 찾을 수 없습니다.");
                        }
                        Showtime showtime = showtimes.get(0); // 첫 번째 상영 시간표 사용

                        // Reservation에 저장된 좌석 이름을 파싱하여 Seat 엔티티 조회 및 상태 변경
                        List<String> seatNamesToCancel = Arrays.stream(res.getSeats().split(","))
                                                               .map(String::trim)
                                                               .collect(Collectors.toList());

                        List<Seat> seatsToUpdate = seatRepository.findAllByShowtimeId(showtime.getId())
                            .stream()
                            .filter(seat -> seatNamesToCancel.contains(seat.getFullSeatName()))
                            .collect(Collectors.toList());

                        if (!seatsToUpdate.isEmpty()) {
                            List<Long> seatIds = seatsToUpdate.stream().map(Seat::getId).collect(Collectors.toList());
                            seatRepository.updateSeatStatusByIds(seatIds, SeatStatus.AVAILABLE); // 좌석 상태를 AVAILABLE로 변경
                            System.out.println("✅ [Fallback] 좌석 상태 변경 완료 (Reservation 데이터 사용): " + seatIds);
                        } else {
                             System.out.println("⚠️ [Fallback] 환불할 좌석을 DB에서 찾지 못했습니다. Reservation 좌석이름: " + seatNamesToCancel);
                        }
                    } catch (Exception seatReleaseException) {
                        System.err.println("❌ [Fallback] Reservation 데이터를 통한 좌석 해제 중 오류 발생: " + seatReleaseException.getMessage());
                        seatReleaseException.printStackTrace();
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("❌ Order ID is not a valid Booking ID: " + orderId);
        } catch (Exception e) {
            System.err.println("❌ Booking 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 4. Payment 상태 변경
    payment.setRefundstatus("CANCELED");
    repository.save(payment);
    System.out.println("💳 [Payment] 결제 환불 처리 완료: " + payment.getId());

    // Payment와 Reservation은 이제 모두 'CANCELED' 상태로 업데이트되었으므로 추가적인 레거시 Reservation 업데이트는 불필요
    // (위에 optionalReservation.ifPresent 블록에서 이미 처리됨)

    return ResponseEntity.ok("환불 처리 완료");
}
}
