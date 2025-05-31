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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingProcessService {
	
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    private static final Map<CustomerCategory, BigDecimal> PRICE_POLICY = Map.of(
        CustomerCategory.ADULT, new BigDecimal("15000"),
        CustomerCategory.YOUTH, new BigDecimal("12000"),
        CustomerCategory.SENIOR, new BigDecimal("7000"),
        CustomerCategory.DISABLED, new BigDecimal("6000") 
    ); 

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
        int totalCustomerCount = requestDto.getCustomerCounts().values().stream().mapToInt(Integer::intValue).sum();
        if (totalCustomerCount != selectedSeats.size()) {
            throw new CustomBookingException("선택된 좌석 수와 총 인원 수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        if (totalCustomerCount == 0 && !selectedSeats.isEmpty()) {
             throw new CustomBookingException("인원을 선택하지 않고 좌석만 선택할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (totalCustomerCount > 0 && selectedSeats.isEmpty()) {
            throw new CustomBookingException("좌석을 선택하지 않고 인원만 선택할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
         if (totalCustomerCount == 0 && selectedSeats.isEmpty()){

            throw new CustomBookingException("예매할 좌석 및 인원을 선택해주세요.", HttpStatus.BAD_REQUEST);
        }


        // 4. 가격 계산
        BigDecimal totalPrice = calculateTotalPrice(requestDto.getCustomerCounts());

        // 5. 예매 엔티티 생성 및 저장
        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .customerCounts(requestDto.getCustomerCounts())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING_PAYMENT) // 결제 대기 상태로 시작 (실제 결제 연동 시 변경될 수 있음)
                .build();

        // 6. 선택된 좌석을 예매에 추가하고 상태 변경
        for (Seat seat : selectedSeats) {
            booking.addSelectedSeat(seat); // Booking 엔티티 내 selectedSeats 컬렉션에 추가
            seat.setStatus(SeatStatus.SElECTED); // 좌석 상태를 '선택됨(임시)'으로 변경 (결제 완료 시 RESERVED로 변경 필요)
        }
        
        seatRepository.saveAll(selectedSeats); // 변경된 좌석 상태를 DB에 저장
        Booking savedBooking = bookingRepository.save(booking); // 예매 정보 저장

        // 7. 응답 DTO 변환 및 반환
        return BookingResponseDto.fromEntity(savedBooking);
    }

    private BigDecimal calculateTotalPrice(Map<CustomerCategory, Integer> customerCounts) {
        return customerCounts.entrySet().stream()
                .map(entry -> {
                    BigDecimal pricePerCategory = PRICE_POLICY.get(entry.getKey());
                    if (pricePerCategory == null) {
                        // 정의되지 않은 고객 유형에 대한 처리 (예: 예외 발생 또는 기본 가격 적용)
                        throw new CustomBookingException("알 수 없는 고객 유형입니다: " + entry.getKey(), HttpStatus.BAD_REQUEST);
                    }
                    return pricePerCategory.multiply(new BigDecimal(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
