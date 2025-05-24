package com.example.demo.Booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Booking.entity.Showtime;



public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
//특정 영화관(theaterId), 특정 영화(movieId), 그리고 주어진 날짜 범위(하루)에 해당하는 모든 상영시간(Showtime) 정보를 조회합니다.
 @Query("SELECT s FROM Showtime s WHERE s.theater.id = :theaterId AND s.movie.id = :movieId AND s.startTime >= :startOfDay AND s.startTime < :endOfDay ORDER BY s.startTime ASC")
    List<Showtime> findShowtimes(
        @Param("theaterId") Long theaterId,
        @Param("movieId") Long movieId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
    );

    // 특정 영화관(theaterId)과 특정 날짜 범위(하루)에 상영하는 모든 상영시간 정보를 조회
    List<Showtime> findByTheaterIdAndStartTimeBetween(Long theaterId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    // 특정 영화(movieId)와 특정 날짜 범위(하루)에 해당 영화를 상영하는 모든 상영시간 정보를 조회
    List<Showtime> findByMovieIdAndStartTimeBetween(Long movieId, LocalDateTime startOfDay, LocalDateTime endOfDay);
} 