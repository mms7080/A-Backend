package com.example.demo.Booking.service;

import com.example.demo.Booking.dto.BookingRequestDto;
import com.example.demo.Booking.dto.BookingResponseDto;
import com.example.demo.Booking.entity.*;
import com.example.demo.Booking.repository.BookingRepository;
import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import com.example.demo.Payment.Payment;
import com.example.demo.Payment.PaymentRepository;

import com.example.demo.Booking.exception.ResourceNotFoundException; 
import com.example.demo.Booking.exception.InvalidRequestException; // 권한 없음 등을 위해 사용 가능

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 예매 시도, 예매 확정, 예매 실패 처리, 예매 내역 조회 등의 기능을 제공
@Service
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeService showtimeService;
    private final SeatService seatService;
    private final PaymentRepository paymentRepository;

    private static final int MAX_PASSENGERS_PER_BOOKING = 8;

    public BookingService(BookingRepository bookingRepository, 
                          UserRepository userRepository, 
                          ShowtimeService showtimeService, 
                          SeatService seatService, 
                          PaymentRepository paymentRepository) { 
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showtimeService = showtimeService;
        this.seatService = seatService;
        this.paymentRepository = paymentRepository;
    }

    // 새로운 예매를 시도하는 로직을 처리 (결제 전 단계)
    public BookingResponseDto createBookingAttempt(BookingRequestDto requestDto){
        // 1. 사용자 정보 조회 
        User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(()-> new RuntimeException("User not found with id:" + requestDto.getUserId()));
        
        // 2. 상영시간 정보 조회
        Showtime showtime = showtimeService.getShowtimeById(requestDto.getShowtimeId());

        // 3. 유효성 검사 : 인원수 및 좌석 선택 관련
        int totalPassengers = requestDto.getPassengerCounts().values().stream().mapToInt(Integer::intValue).sum();

        if( totalPassengers == 0){
            throw new IllegalArgumentException("예매할 인원을 선택해야 합니다.");
        }
        if(totalPassengers > MAX_PASSENGERS_PER_BOOKING){
            throw new IllegalArgumentException("최대 예매 가능 인원은 "+ MAX_PASSENGERS_PER_BOOKING + 
            "명입니다. 현재 선택하신 총 인원은 "+ totalPassengers + "명입니다.");
        }
        if(totalPassengers != requestDto.getSeatIds().size()){
            throw new IllegalArgumentException("선택한 좌석 수(" + requestDto.getSeatIds().size() +
            "개)와 예매 인원수 (" + totalPassengers + "명)가 일치하지 않습니다.");
        }

        // 4. 좌석 상태 변경 예약 -> 임시선택
        seatService.holdSeats(requestDto.getSeatIds(), showtime.getId());

        // 5. 총 예매 가격 계산
        double totalPrice = calculateTotalPrice(showtime.getBasePrice(), requestDto.getPassengerCounts());

        // 6. Booking 엔티티 생성 및 필드 값 설정
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShowtime(showtime);

        List<Seat> selectedSeats = requestDto.getSeatIds().stream()
                                            .map(seatService::getSeatById)
                                            .collect(Collectors.toList());
        booking.setSelectedSeats(selectedSeats);
        booking.setPassengerCounts(requestDto.getPassengerCounts());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        booking.setBookingTime(LocalDateTime.now());

        // 7. 생성된 Booking 엔티티를 데이터베이스에 저장
        Booking savedBooking = bookingRepository.save(booking);

        // 8. 저장된 Booking 엔티티를 클라이언트에 응답 BookingResponseDto로 변환하여 반환
        return BookingResponseDto.fromEntity(savedBooking);
    }


    // 고객 유형별 인원수와 해당 상영시간의 기본 티켓 가격을 바탕으로 총 예매 가격을 계산
    private double calculateTotalPrice(Double basePrice, Map<CustomerCategory, Integer> passengerCounts){
        if(basePrice == null || basePrice < 0){
            System.out.println("경고: 기본 티켓 가격(basePrice)이 유효하지 않아 0으로 처리됩니다.");
            basePrice = 0.0;
        }
        double total =0;
        for(Map.Entry<CustomerCategory, Integer> entry : passengerCounts.entrySet()){
            CustomerCategory category = entry.getKey();
            Integer count = entry.getValue();
            if(count == null || count < 0){
                count =0;
            }

            double pricePerCategory = basePrice;
            switch (category) {
                case YOUTH: pricePerCategory *=0.8; break;
                case SENIOR: pricePerCategory *=0.7; break;
                case DISABLED: pricePerCategory *=0.5; break;
                case ADULT : 
                default : break;
            }
            total += pricePerCategory * count;
        }
        return total;
    }

    // 특정 예매건에 대한 소유권을 검증하는 private 헬퍼 메서드
    //해당 bookingId의 예매 정보가 존재하고, 그 예매의 사용자가 현재 로그인한 사용자(currentUserId)와 일치하는지 확인
    private Booking verifyBookingOwnership(Long bookingId, Long currentUserId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booing", "id", bookingId));
        
            if (booking.getUser() == null || !booking.getUser().getId().equals(currentUserId)) {
            throw new InvalidRequestException("해당 예매 내역에 대한 접근 권한이 없습니다. (ID: " + bookingId + ")"); 
        }
        return booking;
    }

    // 결제가 성공적으로 완료된 예매를 최종적으로 확정 처리
    @Transactional
    public BookingResponseDto confirmBooking(Long bookingId, Payment paymentDetails, User currentUser){ 
        // 소유권 검증 
        Booking booking = verifyBookingOwnership(bookingId, currentUser.getId());

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new InvalidRequestException("해당 예매(ID: " + bookingId + ")는 결제 대기 상태가 아니거나 이미 처리된 예매입니다. 현재 상태: " + booking.getStatus());
        }

        Payment savedPayment = paymentRepository.save(paymentDetails);
        booking.setPayment(savedPayment);

        List<Long> seatIds = booking.getSelectedSeats().stream().map(Seat::getId).collect(Collectors.toList());
        seatService.confirmSeats(seatIds, booking.getShowtime().getId());

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingTime(LocalDateTime.now());

        Booking confirmedBooking = bookingRepository.save(booking);
        return BookingResponseDto.fromEntity(confirmedBooking);
    }


    @Transactional
    public void failBooking(Long bookingId, User currentUser){ 
        
        Booking booking = verifyBookingOwnership(bookingId, currentUser.getId());

        if(booking.getStatus() == BookingStatus.PENDING_PAYMENT){
            List<Long> seatIds = booking.getSelectedSeats().stream().map(Seat::getId).collect(Collectors.toList());
            if(!seatIds.isEmpty()){
                seatService.releaseSeats(seatIds, booking.getShowtime().getId());
            }
        } else if (booking.getStatus() != BookingStatus.FAILED) {
            System.err.println("주의: 예매(ID: " + bookingId + ")의 현재 상태(" + booking.getStatus() + ")는 PENDING_PAYMENT가 아니므로 좌석 해제 없이 실패 처리됩니다.");
        }
        
        booking.setStatus(BookingStatus.FAILED);
        bookingRepository.save(booking);
    }

    // 특정 사용자(userId)의 확정된('CONFIRMED') 예매 내역 목록을 BookingResponseDto 형태로 조회
    @Transactional
    public List<BookingResponseDto> getBookingsByUserId(Long userId){
        return bookingRepository.findByUserIdAndStatusOrderByBookingTimeDesc(userId, BookingStatus.CONFIRMED).stream() //
                .map(BookingResponseDto::fromEntity) 
                .collect(Collectors.toList());
    }

    // 특정 예매 ID(bookingId)에 해당하는 예매의 상세 정보를 BookingResponseDto 형태로 조회
     @Transactional(readOnly = true)
    public BookingResponseDto getBookingDetails(Long bookingId, Long userId){
        // 소유권 검증을 여기서 직접 수행
        Booking booking = verifyBookingOwnership(bookingId, userId);
        return BookingResponseDto.fromEntity(booking);
    }

}