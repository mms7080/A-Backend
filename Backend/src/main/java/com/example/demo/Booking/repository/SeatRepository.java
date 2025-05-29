package com.example.demo.Booking.repository;

import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.Showtime; 
import com.example.demo.Booking.entity.SeatStatus; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> { 

    
    List<Seat> findByShowtimeOrderBySeatRowAscSeatNumberAsc(Showtime showtime);

    @Query("SELECT s FROM Seat s WHERE s.showtime.id = :showtimeId ORDER BY s.seatRow ASC, s.seatNumber ASC")
    List<Seat> findAllByShowtimeId(@Param("showtimeId") Long showtimeId);


    Optional<Seat> findByShowtimeAndSeatRowAndSeatNumber(Showtime showtime, String seatRow, int seatNumber);

    @Modifying(clearAutomatically = true, flushAutomatically = true) // 벌크 연산 후 영속성 컨텍스트를 클리어하고 플러시합니다.
    @Query("UPDATE Seat s SET s.status = :status WHERE s.id IN :seatIds")
    int updateSeatStatusByIds(@Param("seatIds") List<Long> seatIds, @Param("status") SeatStatus status);
    
}
