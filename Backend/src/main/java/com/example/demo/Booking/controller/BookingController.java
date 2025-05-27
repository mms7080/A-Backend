package com.example.demo.Booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Annotations.Auth;
import com.example.demo.Booking.dto.BookingRequestDto;
import com.example.demo.Booking.dto.BookingResponseDto;
import com.example.demo.Booking.dto.MovieFilterDto;
import com.example.demo.Booking.dto.SeatDto;
import com.example.demo.Booking.dto.ShowtimeDto;
import com.example.demo.Booking.dto.TheaterDto;
import com.example.demo.Booking.dto.TheaterFilterDto;
import com.example.demo.Booking.service.BookingService;
import com.example.demo.Booking.service.SeatService;
import com.example.demo.Booking.service.ShowtimeService;
import com.example.demo.Booking.service.TheaterService;
import com.example.demo.Movie.MovieRepository;
import com.example.demo.Payment.Payment;
import com.example.demo.User.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BookingController {
    private final TheaterService theaterService;
    private final ShowtimeService showtimeService;
    private final SeatService seatService;
    private final BookingService bookingService;
    private final MovieRepository movieRepository;

    public BookingController (TheaterService theaterService,
                              ShowtimeService showtimeService,
                              SeatService seatService,
                              BookingService bookingService,
                              MovieRepository movieRepository) {
    this.theaterService =theaterService;
    this.showtimeService = showtimeService;
    this.seatService = seatService;
    this.bookingService = bookingService;
    this.movieRepository = movieRepository;
                              }

    // 1. 상영시간 선택 페이지
    // 등록된 모든 영화관 지역 목록을 조회
    @GetMapping("/regions")
    public ResponseEntity<List<String>> getAllRegions() { 
        List<String> regions = theaterService.getAllRegions();
        return ResponseEntity.ok(regions);
    }
    
    // 특정 지역(region)에 해당하는 영화관 목록을 TheaterDto 형태로 조회 
    // URL의 쿼리 파라미터, 예: /theaters?region=서울
    @GetMapping("/theaters")
    public ResponseEntity<List<TheaterDto>> getTheatersByRegion(@RequestParam String region ) {
        List<TheaterDto> theaters = theaterService.getTheaterByRegion(region);
        return ResponseEntity.ok(theaters);
    }
    // 특정 영화관(theaterId)과 날짜(date)를 기준으로, 해당 날짜에 그 영화관에서 상영하는 모든 영화 목록을
    // MovieFilterDto 형태로 조회
    @GetMapping("/movies/filter")
    public ResponseEntity<List<MovieFilterDto>> getMoviesForTheaterAndDate( 
            @RequestParam Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MovieFilterDto> movies = showtimeService.getMoviesForTheaterAndDate(theaterId, date); 
        return ResponseEntity.ok(movies);
    }

    // 특정 영화(movieId)와 날짜(date)를 기준으로, 해당 날짜에 그 영화를 상영하는 모든 영화관 목록을
    // TheaterFilterDto 형태로 조회
    @GetMapping("/theaters/filter")
    public ResponseEntity<List<TheaterFilterDto>> getTheatersForMovieAndDate( 
            @RequestParam Long movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TheaterFilterDto> theaters = showtimeService.getTheatersForMovieAndDate(movieId, date); 
        return ResponseEntity.ok(theaters);
    }

    // 특정 영화관(theaterId), 특정 영화(movieId), 특정 날짜(date)의 상영시간표를 ShowtimeDto 형태로 조회
    // 각 DTO에는 예매 가능한 좌석 수도 포함
    @GetMapping("/showtimes")
    public ResponseEntity<List<ShowtimeDto>> getShowtimes( 
            @RequestParam Long theaterId,
            @RequestParam Long movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShowtimeDto> showtimes = showtimeService.getShowtimes(theaterId, movieId, date); 
        return ResponseEntity.ok(showtimes);
    }

    // 02. 인원/좌석 선택 페이지
    // 특정 상영시간(showtimeId)에 해당하는 모든 좌석 목록을 SeatDto 형태로 조회\
    @GetMapping("/showtimes/{showtimeId}/seats")
    public ResponseEntity<List<SeatDto>> getSeatsForShowtime(@PathVariable Long showtimeId) { //
        List<SeatDto> seats = seatService.getSeatsForShowtime(showtimeId); //
        return ResponseEntity.ok(seats);
    }

    // 사용자의 예매 시도(인원 및 좌석 선택 완료 후 결제 전 단계)를 처리
    @PostMapping("/initiate")
    public ResponseEntity<BookingResponseDto> initiateBooking(@RequestBody BookingRequestDto bookingRequestDto, @Auth User user) { 
        if (user == null) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        bookingRequestDto.setUserId(user.getId()); 
        BookingResponseDto responseDto = bookingService.createBookingAttempt(bookingRequestDto); 
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 03. 결제 및 예매 확정/실패 처리, 로그인이 필요한 작업
    // 외부 결제 시스템(예: 토스페이먼츠)을 통한 결제가 성공적으로 완료된 후,
    // 그 결과를 받아 예매를 최종적으로 확정
    @PostMapping("/confirm-payment")
    public ResponseEntity<BookingResponseDto> handlePaymentConfirmation( //
            @RequestParam Long bookingId, 
            @RequestBody Payment paymentDetails, //
            @Auth User user //
            ) {
        
        // 1. 사용자 인증 확인 (필수): 로그인하지 않은 사용자는 이 API를 호출할 수 없음
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }

        // 2. 결제 정보의 주문 ID(orderId)와 전달받은 예매 ID(bookingId)가 일치하는지 검증
        
        if (!String.valueOf(bookingId).equals(paymentDetails.getOrderId())) { 
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // BookingService 호출 시 현재 사용자 정보(user) 전달
        BookingResponseDto bookingResponseDto = bookingService.confirmBooking(bookingId, paymentDetails, user);
        return ResponseEntity.ok(bookingResponseDto);
    }

    // 예매 실패 또는 사용자의 예매 취소(결제 전 단계에서 사용자가 중단한 경우)를 처리
    @PostMapping("/fail/{bookingId}")
    public ResponseEntity<Void> failBooking(@PathVariable Long bookingId, @Auth User user) { 
        if (user == null) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
// BookingService 호출 시 현재 사용자 정보(user) 전달
        bookingService.failBooking(bookingId, user); 
        return ResponseEntity.ok().build();
    }

    // 사용자 예매 내역 조회
    // 현재 로그인한 사용자의 확정된('CONFIRMED') 예매 내역 목록을 조회
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(@Auth User user) { 
        if (user == null) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }
        
        List<BookingResponseDto> bookings = bookingService.getBookingsByUserId(user.getId()); //
       
        return ResponseEntity.ok(bookings);
    }

    // 현재 로그인한 사용자의 특정 예매 ID(bookingId)에 해당하는 상세 예매 내역을 조회
    @GetMapping("/my-bookings/{bookingId}")
    public ResponseEntity<BookingResponseDto> getMyBookingDetails(@PathVariable Long bookingId, @Auth User user) { 
        if (user == null) { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }
       
        BookingResponseDto bookingDetails = bookingService.getBookingDetails(bookingId, user.getId()); 
        return ResponseEntity.ok(bookingDetails);
    }

    // 4.

}
