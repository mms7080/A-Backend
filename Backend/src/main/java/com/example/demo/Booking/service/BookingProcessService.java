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
    private final PricePolicyService pricePolicyService;

    
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

        // 3. 인원 수 검증
        // int totalCustomerCount = requestDto.getCustomerCounts().values().stream().mapToInt(Integer::intValue).sum();
        // if (totalCustomerCount != selectedSeats.size()) {
        //     throw new CustomBookingException("선택된 좌석 수와 총 인원 수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        // }
        // if (totalCustomerCount == 0 && !selectedSeats.isEmpty()) {
        //      throw new CustomBookingException("인원을 선택하지 않고 좌석만 선택할 수 없습니다.", HttpStatus.BAD_REQUEST);
        // }
        // if (totalCustomerCount > 0 && selectedSeats.isEmpty()) {
        //     throw new CustomBookingException("좌석을 선택하지 않고 인원만 선택할 수 없습니다.", HttpStatus.BAD_REQUEST);
        // }
        //  if (totalCustomerCount == 0 && selectedSeats.isEmpty()){

        //     throw new CustomBookingException("예매할 좌석 및 인원을 선택해주세요.", HttpStatus.BAD_REQUEST);
        // }

        // BigDecimal finalPrice = pricePolicyService.calculateTotalPrice(requestDto.getCustomerCounts(), requestDto.getCouponCode());

        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .customerCounts(requestDto.getCustomerCounts())
                // .totalPrice(finalPrice)
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        for (Seat seat : selectedSeats) {
            booking.addSelectedSeat(seat);
            seat.setStatus(SeatStatus.SElECTED);
        }
        seatRepository.saveAll(selectedSeats);
        Booking savedBooking = bookingRepository.save(booking);

        // BookingResponseDto 생성 시 가격 정책 맵 전달
        return BookingResponseDto.fromEntity(savedBooking, pricePolicyService.getAllPricePolicies());

    }    
}
