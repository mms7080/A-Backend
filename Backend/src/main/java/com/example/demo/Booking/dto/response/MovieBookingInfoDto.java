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
    // 부제목 추가 + 평점 + 예매율 + 누적관객수 + 장르 + 감독 + 출연
    private String titleEnglish;
    private String genre;
    private String director;
    private String cast;
    private Double score;
    private Double reserveRate;
    private Long totalView;
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
        this.titleEnglish = movie.getTitleEnglish();
        this.genre = movie.getGenre();
        this.director = movie.getDirector();
        this.cast = movie.getCast();
        this.score = movie.getScore();
        this.reserveRate = movie.getReserveRate();
        this.totalView = movie.getTotalView();

    }

    
    public static MovieBookingInfoDto fromEntity(Movie movie) { 
        if (movie == null) return null;
        return new MovieBookingInfoDto(movie);
    }

    
    public MovieBookingInfoDto(Long movieId, String title, String posterUrl, String wideImageUrl, 
    String ageRating, Integer runningTime, String titleEnglish, String gener, String director, 
    String cast, Double score, Double reserveRate, Long totalView) {

        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.wideImageUrl = wideImageUrl;
        this.ageRating = ageRating;
        this.runningTime = runningTime;
        this.titleEnglish = titleEnglish;
        this.genre = gener;
        this.director = director;
        this.cast = cast;
        this.score = score;
        this.reserveRate = reserveRate;
        this.totalView = totalView;
    }
}