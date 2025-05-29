package com.example.demo.Booking.repository;

import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Movie.Movie; 
import com.example.demo.Booking.entity.Theater; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    List<Showtime> findByTheaterAndMovieAndStartTimeBetweenOrderByStartTimeAsc(
            Theater theater, Movie movie, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Showtime s WHERE s.theater.id = :theaterId AND s.movie.id = :movieId AND s.startTime >= :startDate AND s.startTime < :endDate ORDER BY s.startTime ASC")
    List<Showtime> findShowtimesByTheaterAndMovieAndDateRange(
            @Param("theaterId") Long theaterId,
            @Param("movieId") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT DISTINCT s.startTime FROM Showtime s WHERE s.theater.id = :theaterId AND s.movie.id = :movieId AND s.startTime >= :rangeStart AND s.startTime < :rangeEnd ORDER BY s.startTime ASC")
    List<LocalDateTime> findDistinctShowtimeDates(
            @Param("theaterId") Long theaterId,
            @Param("movieId") Long movieId,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd
    );




        }