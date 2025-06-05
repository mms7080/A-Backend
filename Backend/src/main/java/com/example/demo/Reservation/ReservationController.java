package com.example.demo.Reservation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReservationController {

    private final ReservationRepository repo;

    public ReservationController(ReservationRepository repo) {
        this.repo = repo;
    }


@Component
public class ReservationDataInitializer {

    private final ReservationRepository repository;

    public ReservationDataInitializer(ReservationRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() > 0) {
            System.out.println("⚠️ 예약 데이터가 이미 존재하여 초기화를 건너뜁니다.");
            return;
        }

        List<Reservation> dummyReservations = List.of(
            new Reservation(null, "root", 1L, "서울", "강남", "2025-06-05", "18:00", "A1", 2, 0, 0, 0, 15000, "reservation-1717560000000"),
            new Reservation(null, "root2", 1L, "부산", "센텀", "2025-06-06", "20:30", "B3", 3, 0, 0, 0, 15000, "reservation-1717560100000"),
            new Reservation(null, "root3", 1L, "대전", "둔산", "2025-06-07", "14:00", "C1", 1, 1, 0, 0, 15000, "reservation-1717560200000"),
            new Reservation(null, "root4", 4L, "서울", "홍대", "2025-06-08", "16:45", "D1", 0, 0, 1, 0, 15000, "reservation-1717560300000"),
            new Reservation(null, "root5", 5L, "인천", "송도", "2025-06-09", "12:00", "E2", 2, 1, 0, 1, 15000, "reservation-1717560400000")
        );

        repository.saveAll(dummyReservations);
        System.out.println("✅ 예약 더미 데이터 삽입 완료");
    }
}



    @PostMapping
    public ResponseEntity<?> save(@RequestBody Reservation reservation) {
        // 필수값 검증
        if (reservation.getUserId() == null || reservation.getUserId().isBlank()) {
            return ResponseEntity.badRequest().body("사용자 정보가 없습니다.");
        }
        if (reservation.getSeats() == null || reservation.getSeats().isBlank()) {
            return ResponseEntity.badRequest().body("좌석 정보가 누락되었습니다.");
        }

        // 저장
        try {
            Reservation saved = repo.save(reservation);
            System.out.println("✅ 예약 저장 완료: " + saved);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("❌ 예약 저장 중 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 저장 실패: " + e.getMessage());
        }
    }
}


