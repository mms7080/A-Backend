package com.example.demo.Booking.repository;

import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus; // SeatStatus Enum import
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SeatRepository extends JpaRepository<Seat, Long> { 

    /**
     * 특정 상영시간(showtimeId)에 속한 모든 좌석(Seat) 목록을 조회
     */
    List<Seat> findByShowtimeId(Long showtimeId);

    /**
     * 특정 상영시간(showtimeId)에 속하면서 특정 상태(status)를 가진 모든 좌석(Seat) 목록을 조회합니다.
     * 예를 들어, 특정 상영시간의 '예약 가능(AVAILABLE)' 상태인 좌석만 조회할 때 사용합니다.
     */
    List<Seat> findByShowtimeIdAndStatus(Long showtimeId, SeatStatus status); //

    /**
     * 주어진 좌석 ID 목록(seatIds)에 해당하는 모든 좌석(Seat) 엔티티를 조회
     */
    List<Seat> findByIdIn(List<Long> seatIds);
}
