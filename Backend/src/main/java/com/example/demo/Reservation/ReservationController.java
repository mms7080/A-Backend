package com.example.demo.Reservation;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 필요한 모든 클래스 import
import com.example.demo.Booking.entity.BookingStatus;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.MovieRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.MovieRepository;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.Movie;
import com.example.demo.Movie.MovieRepository;
import com.example.demo.Payment.PaymentRepository;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReservationController {

    private final ReservationRepository reservationRepo;
    private final CouponRepository couponRepo;
    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository; // 의존성 추가
    private final MovieRepository movieRepository;       // 의존성 추가
    private final TheaterRepository theaterRepository;   // 의존성 추가


    // 생성자 수정
     public ReservationController(ReservationRepository reservationRepo, CouponRepository couponRepo,
                                 PaymentRepository paymentRepo, BookingRepository bookingRepository, SeatRepository seatRepository,
                                 ShowtimeRepository showtimeRepository, MovieRepository movieRepository, TheaterRepository theaterRepository) {
        this.reservationRepo = reservationRepo;
        this.couponRepo = couponRepo;
        this.paymentRepo = paymentRepo;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    @Component
    public class ReservationDataInitializer {
        private final ReservationRepository repository;
        public ReservationDataInitializer(ReservationRepository repository) { this.repository = repository; }
        @PostConstruct
        public void init() {
            if (repository.count() > 0) {
                System.out.println("⚠️ 예약 데이터가 이미 존재하여 초기화를 건너뜁니다.");
                return;
            }
            List<Reservation> dummyReservations = List.of(
                    new Reservation(null, "root1", 1L, "서울", "강남", "2025-06-07", "10:00", "A1", 1, 0, 0, 0, 12000, "movie-1717550000001", "2025-06-06T10:00:00+09:00", "CONFIRMED"),
                    new Reservation(null, "root2", 2L, "서울", "홍대", "2025-06-07", "13:30", "C3,C4", 2, 0, 0, 0, 24000, "movie-1717550000002", "2025-06-06T10:00:00+09:00", "CONFIRMED")
            );
            repository.saveAll(dummyReservations);
            System.out.println("✅ 예약 더미 데이터 삽입 완료");
        }
    }

    @PostMapping
    public ResponseEntity<?> saveReservation(
            @RequestBody Reservation reservation,
            @RequestParam(required = false) Long couponId) {
        if (reservation.getUserId() == null || reservation.getUserId().isBlank()) {
            return ResponseEntity.badRequest().body("사용자 정보가 없습니다.");
        }
        if (reservation.getSeats() == null || reservation.getSeats().isBlank()) {
            return ResponseEntity.badRequest().body("좌석 정보가 누락되었습니다.");
        }
        if (reservation.getOrderId() != null && reservationRepo.countByOrderId(reservation.getOrderId()) > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 예약된 주문입니다.");
        }
        int finalPrice = reservation.getTotalPrice();
        if (couponId != null) {
            Optional<Coupon> optionalCoupon = couponRepo.findById(couponId);
            if (optionalCoupon.isEmpty()) {
                return ResponseEntity.badRequest().body("쿠폰을 찾을 수 없습니다.");
            }
            Coupon coupon = optionalCoupon.get();
            if (coupon.isUsed()) {
                return ResponseEntity.badRequest().body("이미 사용된 쿠폰입니다.");
            }
            if (!coupon.getUserId().equals(reservation.getUserId())) {
                return ResponseEntity.badRequest().body("해당 사용자의 쿠폰이 아닙니다.");
            }
            switch (coupon.getType()) {
                case "GENERAL_TICKET": finalPrice = 0; break;
                case "DISCOUNT": finalPrice = Math.max(0, finalPrice - coupon.getDiscountAmount()); break;
                default: return ResponseEntity.badRequest().body("알 수 없는 쿠폰 타입입니다.");
            }
            coupon.setUsed(true);
            couponRepo.save(coupon);
        }
        reservation.setTotalPrice(finalPrice);
        reservation.setApprovedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        reservation.setStatus("CONFIRMED");
        try {
            Reservation saved = reservationRepo.save(reservation);
            System.out.println("✅ 예약 저장 완료: " + saved);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("❌ 예약 저장 중 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 저장 실패: " + e.getMessage());
        }
    }

    /**
     * 예매 취소(환불) 처리 메서드
     */
    @Transactional
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        return reservationRepo.findById(id).map(reservation -> {
            if ("CANCELED".equals(reservation.getStatus())) {
                return ResponseEntity.badRequest().body("이미 환불된 예매입니다.");
            }

            reservation.setStatus("CANCELED");
            reservationRepo.save(reservation);
            System.out.println("🔁 [Reservation] 예약 환불 처리 완료: " + reservation.getId());

            String orderId = reservation.getOrderId();

            paymentRepo.findByOrderId(orderId).ifPresent(payment -> {
                payment.setRefundstatus("CANCELED");
                paymentRepo.save(payment);
                System.out.println("💳 [Payment] 결제 환불 처리 완료: " + orderId);
            });

            try {
                List<Theater> theaters = theaterRepository.findByName(reservation.getTheater());
                if(theaters.isEmpty()) throw new RuntimeException("극장 정보를 찾을 수 없습니다: " + reservation.getTheater());
                
                Movie movie = movieRepository.findById(reservation.getMovieId())
                        .orElseThrow(() -> new RuntimeException("영화 정보를 찾을 수 없습니다: " + reservation.getMovieId()));

                LocalDateTime showDateTime = LocalDateTime.parse(reservation.getDate() + "T" + reservation.getTime());

                List<Showtime> showtimes = showtimeRepository
                    .findByTheaterAndMovieAndStartTimeBetweenOrderByStartTimeAsc(
                        theaters.get(0), movie, showDateTime.minusMinutes(1), showDateTime.plusMinutes(1));
                
                if(showtimes.isEmpty()) throw new RuntimeException("상영 시간표를 찾을 수 없습니다.");
                Showtime showtime = showtimes.get(0);

                // --- ⬇️ 이 부분이 핵심 수정 사항입니다 ⬇️ ---
                // 좌석 이름을 , 로 분리하고, 각 이름의 앞뒤 공백을 제거합니다.
                List<String> seatNamesToCancel = Arrays.stream(reservation.getSeats().split(","))
                                                       .map(String::trim)
                                                       .collect(Collectors.toList());

                List<Seat> seatsToUpdate = seatRepository.findAllByShowtimeId(showtime.getId())
                    .stream()
                    .filter(seat -> seatNamesToCancel.contains(seat.getFullSeatName()))
                    .collect(Collectors.toList());

                if (!seatsToUpdate.isEmpty()) {
                    List<Long> seatIds = seatsToUpdate.stream().map(Seat::getId).collect(Collectors.toList());
                    seatRepository.updateSeatStatusByIds(seatIds, SeatStatus.AVAILABLE);
                    System.out.println("✅ [Seat] 좌석 상태 변경 완료: " + seatIds);
                } else {
                     System.out.println("⚠️ 환불할 좌석을 DB에서 찾지 못했습니다. DB 좌석이름과 Reservation에 저장된 좌석이름을 비교해주세요. 좌석이름: " + seatNamesToCancel);
                }

                // Booking 상태도 변경
                String[] parts = orderId.split("-");
                if (parts.length > 1 && parts[0].equals("movie")) {
                    Long bookingId = Long.parseLong(parts[1]);
                    bookingRepository.findById(bookingId).ifPresent(booking -> {
                        booking.setStatus(BookingStatus.CANCELLED_BY_USER);
                        bookingRepository.save(booking);
                        System.out.println("💳 [Booking] 예매 환불 처리 완료: " + bookingId);
                    });
                }
            } catch (Exception e) {
                System.err.println("❌ 좌석 상태 변경 중 오류 발생: " + e.getMessage());
                // 에러 발생 시 로그를 남기고, 환불 흐름은 계속 진행되도록 함
            }

            return ResponseEntity.ok("예매가 환불되었습니다.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("예매를 찾을 수 없습니다."));
    }
}


