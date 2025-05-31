package com.example.demo.Booking.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList; 
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@DependsOn({"movieDao", "theaterService"})
// @Transactional(readOnly = true)
public class ShowtimeService {
    private static final Logger log = LoggerFactory.getLogger(ShowtimeService.class);
    private final ShowtimeRepository showtimeRepository; 
    private final MovieRepository movieRepository; 
    private final TheaterRepository theaterRepository;

    @Value("${booking.max-advance-days:15}") 
    private int maxAdvanceDays;

    // 💡 서버 시작 시 더미 상영 시간표 데이터 자동 삽입
    // @PostConstruct  //테스트할때만 주석 풀어서 사용
    @Transactional 
    public void initShowtimes() {
        if (showtimeRepository.count() > 0) { // DB에 이미 상영 시간표 데이터가 있으면 실행 안 함
            log.info("Showtime data already exists. Skipping initialization.");
            return;
        }

       log.info("Initializing dummy showtime data for ALL movies and ALL theaters...");
        List<Showtime> showtimesToSave = new ArrayList<>();

        // 1. 모든 영화 정보 가져오기
        List<Movie> allMovies = movieRepository.findAll();
        // 2. 모든 극장 정보 가져오기
        List<Theater> allTheaters = theaterRepository.findAll();

        if (allMovies.isEmpty() || allTheaters.isEmpty()) {
            log.warn("No movies or theaters found in the database. Skipping showtime initialization.");
            return;
        }

        LocalDate today = LocalDate.now();
        String[] auditoriumNames = {"1관", "2관", "3관", "Dolby Cinema", "IMAX관"}; // 상영관 이름 다양화
        LocalTime[] timeSlots = {
            LocalTime.of(9, 30), LocalTime.of(11, 0), LocalTime.of(12, 30), 
            LocalTime.of(14, 0), LocalTime.of(15, 30), LocalTime.of(17, 0),
            LocalTime.of(18, 30), LocalTime.of(20, 0), LocalTime.of(21, 30)
        };

        // 오늘부터 3일간의 더미 데이터 생성 (너무 많아지지 않도록 날짜 제한)
        // 필요에 따라 이 반복 횟수를 조절하여 생성되는 데이터 양을 제어. (예: 1일치만 또는 maxAdvanceDays까지)
        for (int i = 0; i < 1; i++) { // 예시로 3일치 데이터만 생성
            LocalDate currentDate = today.plusDays(i);

            for (Movie movie : allMovies) {
                for (Theater theater : allTheaters) {
                    for (LocalTime time : timeSlots) {
                            showtimesToSave.add(Showtime.builder()
                                    .movie(movie)
                                    .theater(theater)
                                    .startTime(LocalDateTime.of(currentDate, time))
                                    .auditoriumName(auditoriumNames[(int)((movie.getId() + theater.getId() + time.getHour()) % auditoriumNames.length)]) 
                                    .build());
                        }
                    }
                }
            }

        if (!showtimesToSave.isEmpty()) {
            showtimeRepository.saveAll(showtimesToSave);
            log.info("Dummy showtime data initialization complete. {} showtimes created.", showtimesToSave.size());
        } else {
            log.info("No showtime data was generated to initialize. Check conditions or prerequisite data.");
        }
    }


    public List<ShowtimeDto> getShowtimes(Long movieId, Long theaterId, LocalDate date) {
        log.debug("Fetching showtimes for movieId: {}, theaterId: {}, date: {}", movieId, theaterId, date);

        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 영화를 찾을 수 없습니다: " + movieId));

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 극장을 찾을 수 없습니다: " + theaterId));

        LocalDateTime startOfDay = date.atStartOfDay(); 
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);  

        List<Showtime> showtimes = showtimeRepository
                .findByTheaterAndMovieAndStartTimeBetweenOrderByStartTimeAsc(theater, movie, startOfDay, endOfDay);

        if (showtimes.isEmpty()) {
            log.info("No showtimes found for movieId: {}, theaterId: {}, date: {}", movieId, theaterId, date);
        }

        return showtimes.stream()
                .map(ShowtimeDto::fromEntity) 
                .collect(Collectors.toList());
    }

    public List<LocalDate> getAvailableDates(Long movieId, Long theaterId) {
        log.debug("Fetching available dates for movieId: {} and theaterId: {}", movieId, theaterId);

        // Movie 및 Theater 존재 여부 확인 (선택 사항이지만, ID만으로 조회하므로 안전을 위해)
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
                maxDate.plusDays(1).toLocalDate().atStartOfDay() 
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
