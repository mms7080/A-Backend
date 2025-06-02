package com.example.demo.Mypage;

import com.example.demo.Booking.dto.response.BookingResponseDto;
import com.example.demo.Booking.entity.Booking;
import com.example.demo.Booking.entity.BookingStatus;
import com.example.demo.Booking.entity.CustomerCategory; 
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.exception.CustomBookingException;
import com.example.demo.Booking.exception.ResourceNotFoundException;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.Booking.repository.SeatRepository;
import com.example.demo.Booking.service.PricePolicyService; 
import com.example.demo.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; 
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageBookingService {

    private static final Logger log = LoggerFactory.getLogger(MyPageBookingService.class); 

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final PricePolicyService pricePolicyService;
    
    // 사용자의 예매 내역 조회
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBooking(Long userId){
        if(!userRepository.existsById(userId)){
            log.warn("존재하지 않는 사용자 ID로 예매 내역 조회를 시도: {}",userId);
            throw new ResourceNotFoundException("사용자를 찾을 수 없습니다." + userId);
        }

        List<Booking> bookings = bookingRepository.findAllByUserIdOrderByBookingTimeDesc(userId);
        log.info("사용자 ID '{}' 에 대해 총 {} 건의 예매 내역을 찾았습니다.",userId, bookings.size());
        
        Map<CustomerCategory, BigDecimal> currentPricePolicy = pricePolicyService.getAllPricePolicies();

        return bookings.stream()
                .map(booking -> BookingResponseDto.fromEntity(booking, currentPricePolicy)) // 💡 수정
                .collect(Collectors.toList());
    }

    // 예매 취소
    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        log.info("사용자 ID '{}'가 예매 ID '{}'의 취소를 시도합니다.", userId, bookingId);

        Booking booking = bookingRepository.findByIdAndUser_Id(bookingId, userId)
                .orElseThrow(() -> {
                    log.warn("예매 내역을 찾을 수 없거나 사용자 불일치. 예매 ID: {}, 사용자 ID: {}", bookingId, userId);
                    return new ResourceNotFoundException("해당 예매 내역을 찾을 수 없거나 취소 권한이 없습니다. Booking ID: " + bookingId);
                });

        if (booking.getStatus() == BookingStatus.CANCELLED_BY_USER || booking.getStatus() == BookingStatus.CANCELLED_BY_SYSTEM) {
            log.warn("예매 ID '{}'는 이미 취소된 상태입니다. 현재 상태: {}", bookingId, booking.getStatus());
            throw new CustomBookingException("이미 취소된 예매입니다.", HttpStatus.BAD_REQUEST);
        }

    // LocalDateTime showtimeStartTime = booking.getShowtime().getStartTime();
    // if (LocalDateTime.now().isAfter(showtimeStartTime.minusMinutes(30))){
    //     log.warn("예매 ID '{}'의 취소가 거부되었습니다: 상영 시간 임박.", bookingId);
    //     throw new CustomBookingException("상영 시간이 임박하여 예매를 취소할 수 없습니다.", HttpStatus.FORBIDDEN);
    // }

    booking.setStatus(BookingStatus.CANCELLED_BY_USER);

        if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
            List<Seat> seatsToMakeAvailable = new ArrayList<>(booking.getSelectedSeats());
            log.info("예매 ID '{}'와 연결된 좌석 {}개의 상태를 AVAILABLE로 변경합니다.", bookingId, seatsToMakeAvailable.size());
            for (Seat seat : seatsToMakeAvailable) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }
        } else {
            log.info("예매 ID '{}'에 연결된 좌석이 없어 상태를 변경할 좌석이 없습니다.", bookingId);
        }
        log.info("사용자 ID '{}'에 의해 예매 ID '{}'가 성공적으로 취소되었습니다.", userId, bookingId);
    }


}

