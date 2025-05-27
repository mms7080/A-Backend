package com.example.demo.Booking.dto; // 패키지 경로는 실제 프로젝트에 맞게 조정해주세요.

import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Movie.Movie; // Movie 클래스의 정확한 패키지 경로로 수정해주세요.

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // 원본 타입 유지를 위해 import
import java.time.format.DateTimeFormatter; // 시간 포매팅을 위해 추가

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
    private String auditoriumName; // 프론트엔드에서 screenName으로 사용될 수 있음
    private String startTime; // LocalDateTime에서 String으로 변경 (HH:mm 형식)
    private String screenType; // 프론트엔드에서 사용하는 screenType 필드 추가
    private long availableSeatsCount;


    public static ShowtimeDto fromEntity(Showtime showtime, long availableSeatsCount) {
        if (showtime == null) {
            return null;
        }

        Movie movie = showtime.getMovie();
        Long movieIdResult = (movie != null) ? movie.getId() : null;
        String movieTitleResult = (movie != null) ? movie.getTitle() : "영화 정보 없음";
        String movieRateResult = (movie != null) ? movie.getRate() : "정보 없음";

        // LocalDateTime을 "HH:mm" 형식의 String으로 변환
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedStartTime = (showtime.getStartTime() != null) ? showtime.getStartTime().format(timeFormatter) : null;

        // screenType을 위한 임시 값 (실제로는 Showtime 엔티티나 관련 로직에서 가져와야 함)
        String screenTypeResult = "2D"; // 예시 값, 실제 데이터로 대체 필요

        return new ShowtimeDto(
                showtime.getId(),
                movieIdResult,
                movieTitleResult,
                movieRateResult,
                showtime.getTheater().getId(),
                showtime.getTheater().getName(),
                showtime.getAuditoriumName(),
                formattedStartTime, // 포맷된 시간 문자열 사용
                screenTypeResult,   // screenType 추가
                availableSeatsCount
        );
    }

    
    
}