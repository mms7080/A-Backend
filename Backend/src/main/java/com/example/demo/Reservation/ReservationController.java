package com.example.demo.Reservation;

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
    System.out.println("📥 예약 저장 요청: " + reservation);
    return ResponseEntity.ok(repo.save(reservation));
}

}
