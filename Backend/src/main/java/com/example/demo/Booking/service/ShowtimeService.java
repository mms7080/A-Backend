package com.example.demo.Booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.response.ShowtimeDto;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.exception.ResourceNotFoundException;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.Movie;
import com.example.demo.Movie.MovieRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 상영 시간표(Showtime) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * - 더미 데이터 생성
 * - 조건에 맞는 상영 시간표 조회
 * - 예매 가능한 날짜 조회
 */
@Service
@RequiredArgsConstructor
@DependsOn({"movieDao", "theaterService"}) // TheaterService와 MovieDao가 먼저 생성되도록 의존성 설정
public class ShowtimeService {
    private static final Logger log = LoggerFactory.getLogger(ShowtimeService.class);
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
   
    @Value("${booking.max-advance-days:15}")
    private int maxAdvanceDays;

    
    @PostConstruct
    @Transactional
    public void initShowtimes() {
    if (showtimeRepository.count() > 0) {
        log.info("기존 상영 시간표 데이터가 있어 초기화를 건너뜁니다.");
        return;
    }

    log.info("더미 상영 시간표 데이터 생성을 시작합니다...");

    List<Movie> allMovies = movieRepository.findAll();
    List<Theater> allTheaters = theaterRepository.findAll();

    if (allMovies.isEmpty() || allTheaters.isEmpty()) {
        log.warn("영화 또는 영화관 정보가 없어 상영 시간표를 생성할 수 없습니다.");
        return;
    }
    // 테스트시 서버 빨리 키고 싶으면 영화관, 상영시간 데이터 주석처리하고 줄이면 빨리 켜짐
    final int DAYS_TO_GENERATE = 2; // 2일치 데이터 생성
    final String[] AUDITORIUM_NAMES = {"1관", "2관", "3관", "4관","5관","6관", "IMAX 관"};
    final LocalTime[] TIME_SLOTS = {
        LocalTime.of(9, 0), LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(13, 45),
        LocalTime.of(15, 30), LocalTime.of(17, 15), LocalTime.of(19, 0), LocalTime.of(21, 30),
        LocalTime.of(23, 55)
    };
    
    int totalCreatedCount = 0;

    // 1. 영화관 -> 날짜 순으로 순회
    for (Theater theater : allTheaters) {
        for (int dayOffset = 0; dayOffset < DAYS_TO_GENERATE; dayOffset++) {
            LocalDate currentDate = LocalDate.now().plusDays(dayOffset);

            // 2. 해당 영화관, 해당 날짜에 가능한 모든 '상영 슬롯' 목록 생성
            // 상영 슬롯은 (시간, 상영관)의 조합입니다.
            List<Showtime> potentialShowtimes = new ArrayList<>();
            for (String auditorium : AUDITORIUM_NAMES) {
                for (LocalTime time : TIME_SLOTS) {
                    potentialShowtimes.add(Showtime.builder()
                            .theater(theater)
                            .startTime(LocalDateTime.of(currentDate, time))
                            .auditoriumName(auditorium)
                            .build());
                }
            }
            // 3. 생성된 슬롯 목록을 무작위로 섞어 랜덤성을 부여합니다.
            Collections.shuffle(potentialShowtimes);

            // 4. 섞인 슬롯 목록을 순회하며 영화를 순서대로 배정합니다. (공정한 분배)
            // 예를 들어, 1번 슬롯 -> 영화 A, 2번 슬롯 -> 영화 B, 3번 슬롯 -> 영화 C, ...
            for (int i = 0; i < potentialShowtimes.size(); i++) {
                Showtime showtime = potentialShowtimes.get(i);
                
                // i % allMovies.size() 를 통해 영화 목록을 순환하며 배정합니다.
                Movie movieToAssign = allMovies.get(i % allMovies.size());
                
                showtime.setMovie(movieToAssign); 
                                                  
                Showtime finalShowtime = Showtime.builder()
                        .movie(movieToAssign)
                        .theater(showtime.getTheater())
                        .startTime(showtime.getStartTime())
                        .auditoriumName(showtime.getAuditoriumName())
                        .build();

                showtimeRepository.save(finalShowtime);
                totalCreatedCount++;
            }
        }
    }
        
        log.info("더미 상영 시간표 데이터 생성을 완료했습니다. 총 {}개의 시간표가 생성되었습니다.", totalCreatedCount);
    }
    
    
    public List<ShowtimeDto> getShowtimes(Long movieId, Long theaterId, LocalDate date) {
        log.debug("Fetching showtimes for movieId: {}, theaterId: {}, date: {}", movieId, theaterId, date);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 영화를 찾을 수 없습니다: " + movieId));
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 극장을 찾을 수 없습니다: " + theaterId));
        

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        // 지나간 영화시간 지우는 코드 (사용시 해당날짜와 시간이 없어지 오류가 있음)
        // LocalDateTime startDateTime;
        // if(date.isEqual(LocalDate.now())){
        //     startDateTime = LocalDateTime.now(); 
        // }else{
        //     startDateTime = date.atStartOfDay();
        // }

        // LocalDateTime endOfDay = date.atTime(LocalTime.MAX);  
        
        List<Showtime> showtimes = showtimeRepository
                .findByTheaterAndMovieAndStartTimeBetweenOrderByStartTimeAsc(theater, movie, startDateTime, endOfDay);
        if (showtimes.isEmpty()) {
            log.info("No showtimes found for movieId: {}, theaterId: {}, date: {}", movieId, theaterId, date);
        }
        return showtimes.stream()
                .map(ShowtimeDto::fromEntity) 
                .collect(Collectors.toList());
    }

    public List<LocalDate> getAvailableDates(Long movieId, Long theaterId) {
        log.debug("Fetching available dates for movieId: {} and theaterId: {}", movieId, theaterId);
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("해당 ID의 영화를 찾을 수 없습니다: " + movieId);
        }
        if (!theaterRepository.existsById(theaterId)) {
            throw new ResourceNotFoundException("해당 ID의 극장을 찾을 수 없습니다: " + theaterId);
        }
        LocalDateTime today = LocalDate.now().atStartOfDay(); 
        LocalDateTime maxDate = today.plusDays(maxAdvanceDays).with(LocalTime.MAX); 
        List<LocalDateTime> showtimeDateTimes = showtimeRepository.findDistinctShowtimeDates(
                theaterId,
                movieId,
                today,
                maxDate.toLocalDate().plusDays(1).atStartOfDay() 
        );
        if (showtimeDateTimes.isEmpty()) {
            log.info("No available dates found for movieId: {} and theaterId: {}", movieId, theaterId);
        }
        return showtimeDateTimes.stream()
                .map(LocalDateTime::toLocalDate) 
                .distinct()                      
                .sorted()                        
                .collect(Collectors.toList());
    }
}