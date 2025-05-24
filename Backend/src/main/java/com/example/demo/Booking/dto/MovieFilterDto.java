package com.example.demo.Booking.dto;

import com.example.demo.Movie.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieFilterDto {
    
    private Long id; 
    private String title;
    private String rate;

    public static MovieFilterDto fromEntity(Movie movie){
        if(movie == null){
            return null;
        }
        return new MovieFilterDto(
            movie.getId(),
            movie.getTitle(),
            movie.getRate()
        );
    }
}
