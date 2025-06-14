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

    /**
     * 애플리케이션 시작 시 더미 상영 시간표 데이터를 생성합니다.
     * 이 로직은 모든 영화가 모든 영화관에서 매일 최소 3회 이상 상영되도록 보장합니다.
     */
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
        
        final int DAYS_TO_GENERATE = 2; // 2일치 데이터 생성
        final int MIN_SHOWTIMES_PER_DAY = 5; // 날짜별 최소 상영 횟수
        final String[] AUDITORIUM_NAMES = {"1관", "2관", "3관", "4관", "IMAX 관"};
        final LocalTime[] TIME_SLOTS = {
            LocalTime.of(9, 0), LocalTime.of(10, 30), LocalTime.of(12, 0), LocalTime.of(13, 45),
            LocalTime.of(15, 30), LocalTime.of(17, 15), LocalTime.of(19, 0), LocalTime.of(21, 30),
            LocalTime.of(23, 55)
        };
        
        Random random = new Random();
        int totalCreatedCount = 0;

        // [영화관ID -> [날짜 -> 사용된 "시간-상영관" 키 Set]] 형식으로 스케줄 현황을 관리합니다.
        Map<Long, Map<LocalDate, Set<String>>> scheduleMap = new HashMap<>();
        allTheaters.forEach(theater -> scheduleMap.put(theater.getId(), new HashMap<>()));

        // 영화 -> 영화관 -> 날짜 순으로 순회하며 최소 상영 횟수 보장
        for (Movie movie : allMovies) {
            for (Theater theater : allTheaters) {
                for (int dayOffset = 0; dayOffset < DAYS_TO_GENERATE; dayOffset++) {
                    LocalDate currentDate = LocalDate.now().plusDays(dayOffset);
                    scheduleMap.get(theater.getId()).putIfAbsent(currentDate, new HashSet<>());

                    int createdCountForCombo = 0;
                    // 한 조합(영화,영화관,날짜)에 대해 최소 3번 상영되도록 시도
                    for (int i = 0; i < MIN_SHOWTIMES_PER_DAY; i++) {
                        
                        // 무한 루프 방지를 위해 최대 10번만 시도
                        for (int attempt = 0; attempt < 50; attempt++) {
                            LocalTime randomTime = TIME_SLOTS[random.nextInt(TIME_SLOTS.length)];
                            String randomAuditorium = AUDITORIUM_NAMES[random.nextInt(AUDITORIUM_NAMES.length)];
                            String scheduleKey = randomTime.toString() + "-" + randomAuditorium;

                            // 해당 시간-상영관이 비어있는지 확인
                            if (!scheduleMap.get(theater.getId()).get(currentDate).contains(scheduleKey)) {
                                Showtime showtime = Showtime.builder()
                                        .movie(movie)
                                        .theater(theater)
                                        .startTime(LocalDateTime.of(currentDate, randomTime))
                                        .auditoriumName(randomAuditorium)
                                        .build();
                                showtimeRepository.save(showtime);
                                
                                // 사용된 슬롯으로 기록
                                scheduleMap.get(theater.getId()).get(currentDate).add(scheduleKey);
                                totalCreatedCount++;
                                createdCountForCombo++;
                                break; // 다음 최소 보장 횟수로 넘어감 (i++)
                            }
                        }
                    }
                    if (createdCountForCombo < MIN_SHOWTIMES_PER_DAY) {
                         log.warn("영화 '{}', 극장 '{}', 날짜 {}에 {}개의 상영만 배정되었습니다. (시간대 포화)",
                                movie.getTitle(), theater.getName(), currentDate, createdCountForCombo);
                    }
                }
            }
        }
        
        log.info("더미 상영 시간표 데이터 생성을 완료했습니다. 총 {}개의 시간표가 생성되었습니다.", totalCreatedCount);
    }
    
    // getShowtimes, getAvailableDates 등 나머지 메서드는 이전과 동일하게 유지합니다.
    public List<ShowtimeDto> getShowtimes(Long movieId, Long theaterId, LocalDate date) {
        log.debug("Fetching showtimes for movieId: {}, theaterId: {}, date: {}", movieId, theaterId, date);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 영화를 찾을 수 없습니다: " + movieId));
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 극장을 찾을 수 없습니다: " + theaterId));
        
        LocalDateTime startDateTime;
        if(date.isEqual(LocalDate.now())){
            startDateTime = LocalDateTime.now(); 
        }else{
            startDateTime = date.atStartOfDay();
        }

        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);  
        
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