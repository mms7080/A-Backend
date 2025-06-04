package com.example.demo.BookingPay;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepository;

    // ✅ 예매 저장
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    // ✅ 사용자별 예매 내역 조회
    @GetMapping("/user/{userId}")
    public List<Reservation> getByUser(@PathVariable String userId) {
        return reservationRepository.findByUserId(userId);
    }

    // ✅ 전체 예매 목록 (관리자용)
    @GetMapping
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }
}
