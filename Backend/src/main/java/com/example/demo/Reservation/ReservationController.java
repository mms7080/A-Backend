package com.example.demo.Reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReservationController {

    private final ReservationRepository repo;

    public ReservationController(ReservationRepository repo) {
        this.repo = repo;
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


