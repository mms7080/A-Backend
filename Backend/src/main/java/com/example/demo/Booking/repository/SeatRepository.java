package com.example.demo.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Booking.entity.Seat;


import java.util.List;


public interface SeatRepository extends JpaRepository<Seat, Long> {

    // 특정 상영회차에 속한 모든 좌석 조회
    List<Seat> findByScreeningId(Long screeningId);
}
