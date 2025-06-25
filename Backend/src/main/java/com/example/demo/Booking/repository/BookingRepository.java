package com.example.demo.Booking.repository;

import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.BookingStatus; 
import com.example.demo.User.User; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

   List<Booking> findByUserOrderByBookingTimeDesc(User user);

   @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.bookingTime DESC")
   List<Booking> findAllByUserIdOrderByBookingTimeDesc(@Param("userId") Long userId);

   List<Booking> findByStatus(BookingStatus status);

   List<Booking> findByBookingTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

   List<Booking> findByShowtimeId(Long showtimeId);

   Optional<Booking> findByIdAndUser_Id(Long bookingId, Long userId);

   Optional<Booking> findByPaymentId(Long paymentId);
} 
