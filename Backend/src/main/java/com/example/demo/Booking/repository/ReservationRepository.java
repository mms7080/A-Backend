package com.example.demo.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Booking.entity.Reservation;
import java.util.List;
import java.time.LocalDateTime;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // 1. 고객 이름으로 예약 조회
    List<Reservation> findByCustomerName(String customerName);

    // 2. 특정 좌석(ID)을 포함한 예약 조회
    List<Reservation> findBySeats_Id(Long seatId);
    
    // 3. 예약 시간 범위(기간) 내의 예약 조회
    List<Reservation> findByReservedAtBetween(LocalDateTime from, LocalDateTime to);
} 
