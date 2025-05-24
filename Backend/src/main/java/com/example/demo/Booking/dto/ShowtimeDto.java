package com.example.demo.Booking.dto;

import java.time.LocalDateTime;

import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Movie.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDto {
    
    private Long id;
    private Long movieId;
    private String movieTitle;
    private String movieRate;
    private Long theaterId;
    private String theaterName;
    private String auditoriumName;
    private LocalDateTime startTime;
    private long availableSeatsCount;

    public static ShowtimeDto fromEntity(Showtime showtime, long availableSeatsCount){
        if( showtime == null){
            return null;
        }

        Movie movie = showtime.getMovie();
        Long movieIdResult = (movie !=null) ? movie.getId() : null;
        String movieTitleResult = (movie != null) ? movie.getTitle() : "영화 정보 없음";
        String movieRateResult = (movie != null) ? movie.getRate() : "정보 없음";
        
        return new ShowtimeDto(
            showtime.getId(),
            movieIdResult,
            movieTitleResult,
            movieRateResult,
            showtime.getTheater().getId(),
            showtime.getTheater().getName(),
            showtime.getAuditoriumName(),
            showtime.getStartTime(),
            availableSeatsCount
        );
            
    }
}
