package com.example.demo.Booking.service;

import com.example.demo.Booking.dto.request.BookingRequestDto;
import com.example.demo.Booking.dto.response.BookingResponseDto;
import com.example.demo.Booking.entity.*; 
import com.example.demo.Booking.exception.CustomBookingException;
import com.example.demo.Booking.exception.ResourceNotFoundException;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.repository.ShowtimeRepository;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingProcessService {
	
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    

    
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto requestDto, Long userId) {
        // 1. 사용자, 상영시간표 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        Showtime showtime = showtimeRepository.findById(requestDto.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("상영 시간표를 찾을 수 없습니다: " + requestDto.getShowtimeId()));

        // 2. 좌석 유효성 검사
        List<Seat> selectedSeats = seatRepository.findAllById(requestDto.getSelectedSeatIds());
        if (selectedSeats.size() != requestDto.getSelectedSeatIds().size()) {
            throw new CustomBookingException("선택된 좌석 중 일부를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        for (Seat seat : selectedSeats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new CustomBookingException("좌석 " + seat.getFullSeatName() + "은(는) 이미 선택되었거나 예매할 수 없는 좌석입니다.", HttpStatus.CONFLICT);
            }
        }

        // 3. 인원 수와 좌석 수 일치 여부 검증 (주석 해제 및 로직 강화)
        int totalCustomerCount = requestDto.getCustomerCounts().values().stream().mapToInt(Integer::intValue).sum();
        if (totalCustomerCount != selectedSeats.size()) {
            throw new CustomBookingException("선택된 좌석 수와 총 인원 수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        
        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .customerCounts(requestDto.getCustomerCounts())
                .totalPrice(BigDecimal.ZERO)
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        for (Seat seat : selectedSeats) {
            booking.addSelectedSeat(seat);
            seat.setStatus(SeatStatus.SELECTED);
        }
        seatRepository.saveAll(selectedSeats);
        
        Booking savedBooking = bookingRepository.save(booking);

        // BookingResponseDto 생성 시 가격 정책 맵 전달
        return BookingResponseDto.fromEntity(savedBooking);

    }    
}
