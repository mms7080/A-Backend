package com.example.demo.Booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    
   // 특정 사용자(userId)의 모든 예매(Booking) 내역을 조회
   // Booking 엔티티 내 user 필드의 id를 기준으로 조회

   List<Booking> findByUserId(Long userId);

   // 특정 예매 ID(id)와 특정 예매 상태(status)를 모두 만족하는 예매(Booking) 내역을 조회
   Optional<Booking> findByIdAndStatus(Long id, BookingStatus status);

   // 특정 사용자(userId)의 특정 예매 상태(status)를 가진 모든 예매(Booking) 내역을 조회
   List<Booking> findByUserIdAndStatusOrderByBookingTimeDesc(Long userId, BookingStatus status);
   
    
} 
