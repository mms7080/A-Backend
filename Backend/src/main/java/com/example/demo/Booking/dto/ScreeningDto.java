package com.example.demo.booking.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningDto {
    
    private Long id; // 상영회차 고유 식별자 (DB PK)

    private String movieTitle; // 영화 제목

    private String theaterName; // 상영관 이름

    private LocalDateTime startTime; // 상영 시작 시간
}
