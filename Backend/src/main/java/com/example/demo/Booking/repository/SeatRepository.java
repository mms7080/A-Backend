package com.example.demo.Booking.repository;

import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus; 
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SeatRepository extends JpaRepository<Seat, Long> { 

    /**
     * 특정 상영시간(showtimeId)에 속한 모든 좌석(Seat) 목록을 조회
     */
    List<Seat> findByShowtimeId(Long showtimeId);

    /**
     * 특정 상영시간(showtimeId)에 속하면서 특정 상태(status)를 가진 모든 좌석(Seat) 목록을 조회
     */
    List<Seat> findByShowtimeIdAndStatus(Long showtimeId, SeatStatus status); //

    /**
     * 주어진 좌석 ID 목록(seatIds)에 해당하는 모든 좌석(Seat) 엔티티를 조회
     */
    List<Seat> findByIdIn(List<Long> seatIds);

    long countByShowtimeIdAndStatus(Long showtimeId, SeatStatus status);
    
}
