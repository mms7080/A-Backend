package com.example.demo.Reservation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByOrderId(String orderId);
}
