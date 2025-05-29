package com.example.demo.Booking.dto.response;

import com.example.demo.Movie.Movie; 
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예매 과정에서 사용될 영화의 간략한 정보를 담는 DTO
 */
@Getter
@NoArgsConstructor 
public class MovieBookingInfoDto {

    private Long movieId;           
    private String title;           
    private String posterUrl;       
    private String wideImageUrl;    
    private String ageRating;       
    private Integer runningTime;    

   
    public MovieBookingInfoDto(Movie movie) {
        this.movieId = movie.getId();
        this.title = movie.getTitle(); 
        this.posterUrl = movie.getPoster();
        this.wideImageUrl = movie.getWideImage();
        this.ageRating = movie.getRate(); 
        this.runningTime = movie.getRunningTime(); 
    }

    
    public static MovieBookingInfoDto fromEntity(Movie movie) { 
        if (movie == null) return null;
        return new MovieBookingInfoDto(movie);
    }

    
    public MovieBookingInfoDto(Long movieId, String title, String posterUrl, String wideImageUrl, String ageRating, Integer runningTime) {
        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.wideImageUrl = wideImageUrl;
        this.ageRating = ageRating;
        this.runningTime = runningTime;
    }
}