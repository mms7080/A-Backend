package com.example.demo.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Booking.entity.Theater;
import java.util.List;


public interface TheaterRepository extends JpaRepository<Theater, Long> {
    // 특정 지역에 해당하는 모든 영화관 목록 조회
    List<Theater> findByRegion(String region);
    
}
