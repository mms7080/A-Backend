package com.example.demo.Booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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



@Service
@RequiredArgsConstructor
@DependsOn({"movieDao", "theaterService"}) 
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
            log.info("Showtime data already exists. Skipping initialization.");
            return;
        }

        log.info("Initializing dummy showtime data (seats will be generated on demand by SeatService)...");

        List<Movie> allMovies = movieRepository.findAll();
        List<Theater> allTheaters = theaterRepository.findAll();

        if (allMovies.isEmpty() || allTheaters.isEmpty()) {
            log.warn("No movies or theaters found in the database. Skipping showtime initialization.");
            return;
        }

        LocalDate today = LocalDate.now();
        String[] auditoriumNamesBase = {"1관", "2관", "3관", "Dolby 관", "IMAX 관"};
        LocalTime[] timeSlotsToUse = {
            LocalTime.of(10, 0), 
            LocalTime.of(13, 0),
            LocalTime.of(16, 30), 
            LocalTime.of(23, 0)
           
        };

        int createdShowtimeCount = 0;
        Map<Long, Map<LocalDate, Map<LocalTime, Set<String>>>> theaterScheduleUsage = new HashMap<>();
        int daysToGenerate = 1; // 오늘 하루치 데이터만 생성

        for (int dayOffset = 0; dayOffset < daysToGenerate; dayOffset++) {
            LocalDate currentDate = today.plusDays(dayOffset);

            for (Movie movie : allMovies) {
                for (Theater theater : allTheaters) {
                    theaterScheduleUsage.putIfAbsent(theater.getId(), new HashMap<>());
                    theaterScheduleUsage.get(theater.getId()).putIfAbsent(currentDate, new HashMap<>());

                    for (LocalTime time : timeSlotsToUse) {
                        theaterScheduleUsage.get(theater.getId()).get(currentDate).putIfAbsent(time, new HashSet<>());

                        String assignedAuditorium = null;
                        for (int audIdx = 0; audIdx < auditoriumNamesBase.length; audIdx++) {
                            String candidateAuditorium = auditoriumNamesBase[(int)((movie.getId() + theater.getId() + time.getHour() + audIdx) % auditoriumNamesBase.length)];
                            if (!theaterScheduleUsage.get(theater.getId()).get(currentDate).get(time).contains(candidateAuditorium)) {
                                assignedAuditorium = candidateAuditorium;
                                break;
                            }
                        }

                        if (assignedAuditorium == null) {
                            // log.warn("All auditoriums are busy for MovieId: {}, TheaterId: {}, Date: {}, Time: {}. Skipping this showtime slot.",
                            //          movie.getId(), theater.getId(), currentDate, time);
                            continue;
                        }

                        Showtime showtime = Showtime.builder()
                                .movie(movie)
                                .theater(theater)
                                .startTime(LocalDateTime.of(currentDate, time))
                                .auditoriumName(assignedAuditorium)
                                .build();
                        showtimeRepository.save(showtime); // Showtime만 저장
                        createdShowtimeCount++;
                        
                        theaterScheduleUsage.get(theater.getId()).get(currentDate).get(time).add(assignedAuditorium);
                        
                        
                    }
                }
            }
        }

        if (createdShowtimeCount > 0) {
            log.info("Dummy showtime data initialization complete. {} showtimes created (seats will be generated on demand by SeatService).", createdShowtimeCount);
        } else {
            log.info("No showtime data was generated to initialize. Check movie/theater availability or if all auditoriums were busy.");
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