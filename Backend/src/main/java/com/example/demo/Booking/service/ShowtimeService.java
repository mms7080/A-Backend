package com.example.demo.Booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.MovieFilterDto;
import com.example.demo.Booking.dto.ShowtimeDto;
import com.example.demo.Booking.dto.TheaterFilterDto;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.entity.Showtime;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Movie.Movie;
import com.example.demo.Movie.MovieRepository;

import jakarta.annotation.PostConstruct;

// 상영시간표 조회, 특정 조건에 맞는 영화/영화관 필터링, 초기 데이터 생성 및 좌석 생성 등의 기능을 제공
@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, //
                           MovieRepository movieRepository, //
                           TheaterRepository theaterRepository, //
                           SeatRepository seatRepository) { //
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
        }

    // 특정 영화관(theaterId), 특정 영화(movieId), 특정 날짜(date)의 상영시간표를 ShowtimeDto 형태로 조회
    @Transactional(readOnly = true)
    public List<ShowtimeDto> getShowtimes(Long theaterId, Long movieId, LocalDate date){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Showtime> showtimes = showtimeRepository.findShowtimes(theaterId, movieId, startOfDay, startOfDay);

        return showtimes.stream().map(showtime -> {
            long availableSeats = seatRepository.findByShowtimeIdAndStatus(showtime.getId(), SeatStatus.AVAILABLE).size();
            return ShowtimeDto.fromEntity(showtime, availableSeats);
        }).collect(Collectors.toList());
    }

    // 특정 영화관(theaterId)과 날짜(date)에 상영하는 모든 영화 목록을 MovieFilterDto 형태로 조회
    @Transactional(readOnly = true)
    public List<MovieFilterDto> getMoviesForTheaterAndDate(Long theaterId, LocalDate date) { //
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        List<Showtime> showtimes = showtimeRepository.findByTheaterIdAndStartTimeBetween(theaterId, startOfDay, endOfDay); //
        if (showtimes.isEmpty()) { 
            return Collections.emptyList();
        }
        
        return showtimes.stream()
                .map(Showtime::getMovie) 
                .distinct() 
                .map(MovieFilterDto::fromEntity) 
                .collect(Collectors.toList());
    }

    // 특정 영화(movieId)와 날짜(date)에 해당 영화를 상영하는 모든 영화관 목록을 TheaterFilterDto 형태로 조회
    @Transactional(readOnly = true)
    public List<TheaterFilterDto> getTheatersForMovieAndDate(Long movieId, LocalDate date) { //
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        List<Showtime> showtimes = showtimeRepository.findByMovieIdAndStartTimeBetween(movieId, startOfDay, endOfDay); //
        if (showtimes.isEmpty()) { 
            return Collections.emptyList();
        }
        
        return showtimes.stream()
                .map(Showtime::getTheater) 
                .distinct() 
                .map(TheaterFilterDto::fromEntity) 
                .collect(Collectors.toList());
    }

    // 특정 ID에 해당하는 상영시간(Showtime) 엔티티를 조회
    @Transactional(readOnly = true)
    public Showtime getShowtimeById(Long showtimeId) { 
        return showtimeRepository.findById(showtimeId) 
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + showtimeId));
    }

    // 애플리케이션 시작 시점에 초기 상영시간 및 관련 좌석 데이터를 생성
    // 개발 및 테스트 환경에서 초기 데이터를 편리하게 설정하기 위해 사용
    @PostConstruct
    @Transactional
    public void initShowtimesAndSeats() {
        if (showtimeRepository.count() == 0) { 
            List<Movie> movies = movieRepository.findAll(); 
            List<Theater> theaters = theaterRepository.findAll(); 
            Random random = new Random(); // 랜덤 데이터 생성을 위한 Random 객체

            if (movies.isEmpty() || theaters.isEmpty()) { 
                System.err.println("초기 상영시간 데이터 생성을 위해 영화(Movie) 및 영화관(Theater) 데이터가 먼저 등록되어야 합니다.");
                return; 
            }

            LocalDate today = LocalDate.now(); 
            // 모든 영화와 모든 영화관의 조합에 대해 반복
            movies.forEach(movie -> {
                theaters.forEach(theater -> {
                    
                    int numberOfShowtimes = random.nextInt(2) + 2; 
                    for (int i = 0; i < numberOfShowtimes; i++) {
                        Showtime showtime = new Showtime(); 
                        showtime.setMovie(movie); 
                        showtime.setTheater(theater); 
                        // 상영 시작 시간: 오늘 날짜 + (오전 9시 ~ 저녁 10시 사이 랜덤 시간) + (0분 또는 30분 랜덤)
                        int randomHour = random.nextInt(14) + 9; 
                        int randomMinute = random.nextBoolean() ? 0 : 30; 
                        showtime.setStartTime(today.atTime(randomHour, randomMinute)); 
                        showtime.setAuditoriumName((random.nextInt(5) + 1) + "관"); 
                        showtime.setBasePrice(15000.0 + random.nextInt(6) * 1000); 

                        Showtime savedShowtime = showtimeRepository.save(showtime); // 생성된 상영시간 정보를 데이터베이스에 저장
                        
                        generateSeatsForShowtime(savedShowtime, 9, 12);
                    }
                });
            });
            System.out.println("초기 상영시간 및 좌석 데이터가 성공적으로 생성되었습니다."); 
        }
    }

    // 특정 상영시간(Showtime)에 대한 좌석(Seat)들을 자동으로 생성하여 데이터베이스에 저장
    @Transactional 
    public void generateSeatsForShowtime(Showtime showtime, int rows, int numbersPerRow) { 
        List<Seat> seats = new ArrayList<>(); 
        for (int i = 0; i < rows; i++) { 
            String seatRow = String.valueOf((char) ('A' + i)); 
            for (int j = 1; j <= numbersPerRow; j++) { 
                Seat seat = new Seat(); 
                seat.setSeatRow(seatRow); 
                seat.setSeatNumber(j); 
                seat.setStatus(SeatStatus.AVAILABLE); 
                seat.setShowtime(showtime); 
                seats.add(seat); 
            }
        }
        seatRepository.saveAll(seats); // 생성된 모든 좌석 엔티티를 데이터베이스에 한 번의 배치 작업으로 저장 
        
        showtime.setSeats(seats); 
        showtimeRepository.save(showtime); 
    }
}
