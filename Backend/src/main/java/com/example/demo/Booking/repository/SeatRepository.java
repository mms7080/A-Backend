package com.example.demo.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.booking.entity.Seat;
import com.example.demo.booking.entity.SeatStatus;

import java.util.List;


public interface SeatRepository extends JpaRepository<Seat, Long> {

    // 특정 상영회차에 속한 모든 좌석 조회
    List<Seat> findByScreeningId(Long screeningId);

    // 연속된 사용 가능한 좌석(count)만큼 조회
    List<Seat> findContiguousByScreeningIdAndStatus(Long screeningId, SeatStatus status);

    // 특정 상영회차의 사용 가능한 좌석 수 반환
    long countByScreeningIdAndStatus(Long screeningId, SeatStatus status);

}
