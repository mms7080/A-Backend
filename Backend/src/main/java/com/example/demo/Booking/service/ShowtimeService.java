package com.example.demo.Booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.MovieFilterDto;
import com.example.demo.Booking.dto.ShowtimeDto; // 수정된 DTO 임포트
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

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository,
                           MovieRepository movieRepository,
                           TheaterRepository theaterRepository,
                           SeatRepository seatRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
    }

    // 특정 영화관(theaterId), 특정 영화(movieId), 특정 날짜(date)의 상영시간표를 ShowtimeDto 형태로 조회
    @Transactional(readOnly = true)
    public List<ShowtimeDto> getShowtimes(Long theaterId, Long movieId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1); // 당일 23:59:59.999...까지

        List<Showtime> showtimes = showtimeRepository.findShowtimes(theaterId, movieId, startOfDay, endOfDay);

        return showtimes.stream().map(showtime -> {
            // 해당 상영시간의 사용 가능한 좌석 수를 계산합니다.
            long availableSeats = seatRepository.countByShowtimeIdAndStatus(showtime.getId(), SeatStatus.AVAILABLE);
            // 수정된 DTO의 fromEntity 메소드를 호출합니다.
            return ShowtimeDto.fromEntity(showtime, availableSeats);
        }).collect(Collectors.toList());
    }

    // 특정 영화관(theaterId)과 날짜(date)에 상영하는 모든 영화 목록을 MovieFilterDto 형태로 조회
    @Transactional(readOnly = true)
    public List<MovieFilterDto> getMoviesForTheaterAndDate(Long theaterId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Showtime> showtimes = showtimeRepository.findByTheaterIdAndStartTimeBetween(theaterId, startOfDay, endOfDay);
        if (showtimes.isEmpty()) {
            return Collections.emptyList();
        }

        return showtimes.stream()
                .map(Showtime::getMovie)
                .distinct()
                .map(MovieFilterDto::fromEntity) // MovieFilterDto도 fromEntity와 같은 패턴을 사용하는지 확인 필요
                .collect(Collectors.toList());
    }

    // 특정 영화(movieId)와 날짜(date)에 해당 영화를 상영하는 모든 영화관 목록을 TheaterFilterDto 형태로 조회
    @Transactional(readOnly = true)
    public List<TheaterFilterDto> getTheatersForMovieAndDate(Long movieId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Showtime> showtimes = showtimeRepository.findByMovieIdAndStartTimeBetween(movieId, startOfDay, endOfDay);
        if (showtimes.isEmpty()) {
            return Collections.emptyList();
        }

        return showtimes.stream()
                .map(Showtime::getTheater)
                .distinct()
                .map(TheaterFilterDto::fromEntity) // TheaterFilterDto도 fromEntity와 같은 패턴을 사용하는지 확인 필요
                .collect(Collectors.toList());
    }

    // 특정 ID에 해당하는 상영시간(Showtime) 엔티티를 조회
    @Transactional(readOnly = true)
    public Showtime getShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + showtimeId));
    }

    // (initShowtimesAndSeats 및 generateSeatsForShowtime 메소드는 이전과 동일하게 유지)
    @PostConstruct
    @Transactional
    public void initShowtimesAndSeats() {
        if (showtimeRepository.count() == 0) { 
            List<Movie> movies = movieRepository.findAll(); 
            List<Theater> theaters = theaterRepository.findAll(); 
            // Random random = new Random();

            if (movies.isEmpty() || theaters.isEmpty()) { 
                System.err.println("초기 상영시간 데이터 생성을 위해 영화(Movie) 및 영화관(Theater) 데이터가 먼저 등록되어야 합니다.");
                return; 
            }

           // 고정된 상영 시간 목록 (예: 프론트엔드의 timeSlots 와 유사하게)
            List<LocalTime> fixedTimes = List.of(
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                    LocalTime.of(14, 0),
                    LocalTime.of(16, 0),
                    LocalTime.of(18, 0),
                    LocalTime.of(20, 0),
                    LocalTime.of(22, 0)
            );

            LocalDate today = LocalDate.now();

            // 향후 14일간의 데이터 생성 (오늘 포함)
            for (int dayOffset = 0; dayOffset < 5; dayOffset++) {
                LocalDate currentDate = today.plusDays(dayOffset);

                movies.forEach(movie -> {
                    theaters.forEach(theater -> {
                        // 각 영화-극장 조합에 대해 12개의 다른 상영관(auditorium)에 시간표를 생성한다고 가정
                        for (int auditoriumNum = 1; auditoriumNum <= 12; auditoriumNum++) {
                            // 각 상영관마다 위에서 정의한 fixedTimes 중 일부 또는 전부를 랜덤하게 선택하여 생성
                            // 여기서는 예시로 각 상영관마다 fixedTimes 전체를 사용
                            for (LocalTime time : fixedTimes) {
                                Showtime showtime = new Showtime();
                                showtime.setMovie(movie);
                                showtime.setTheater(theater);
                                showtime.setStartTime(currentDate.atTime(time)); // 현재 날짜와 고정된 시간 사용
                                showtime.setAuditoriumName(auditoriumNum + "관"); // 상영관 번호
                                
                                // Showtime 엔티티에 screenType 필드가 있다면 여기서 설정
                                // 예: 실제로는 Auditorium 엔티티에서 screenType을 가져와야 할 수 있음
                                // showtime.setScreenType(getScreenTypeForAuditorium(theater, auditoriumNum + "관"));
                                // 아래는 임시 값
                                if (auditoriumNum % 3 == 0) {
                                    // showtime.setScreenType("3D"); // 예시
                                } else if (auditoriumNum % 4 == 0) {
                                    // showtime.setScreenType("IMAX"); // 예시
                                } else {
                                    // showtime.setScreenType("2D"); // 예시
                                }

                                // 가격은 기존 랜덤 로직을 사용하거나 고정값 또는 다른 규칙으로 설정 가능
                                Random randomPrice = new Random(); // 가격 랜덤 생성을 위해 로컬 Random 사용
                                showtime.setBasePrice(15000.0 + randomPrice.nextInt(6) * 1000);

                                Showtime savedShowtime = showtimeRepository.save(showtime);
                                generateSeatsForShowtime(savedShowtime, 9, 12); // 각 상영시간에 대한 좌석 생성
                            }
                        }
                    });
                });
            }
            System.out.println("초기 상영시간 및 좌석 데이터가 성공적으로 생성되었습니다. (고정 시간표)");
        }
    }
    
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
        seatRepository.saveAll(seats);
        
        showtime.setSeats(seats); 
        showtimeRepository.save(showtime); 
    }
}