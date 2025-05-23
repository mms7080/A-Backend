package com.example.demo.Booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Booking.entity.Screening;

import java.util.List;
import java.time.LocalDateTime;



public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    // 특정 상영관의 상영회차만 빠르게 조회
    List<Screening> findByTheaterName(String theaterName);

    // 영화 제목에 키워드가 포함된 상영회차 조회  
    Page<Screening> findByMovieTitleContaining(String keyword, Pageable pageable);
    
    // 특정 기간 내 상영회차 조회
    List<Screening> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);

    // 예약 가능한(앞으로 시작하는) 상영회차만 조회
    List<Screening> findByStartTimeAfter(LocalDateTime now);
} 
