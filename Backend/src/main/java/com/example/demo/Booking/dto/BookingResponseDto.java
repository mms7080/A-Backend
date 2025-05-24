package com.example.demo.Booking.dto;

import com.example.demo.Booking.entity.Booking; // Booking 엔티티 참조
import com.example.demo.Booking.entity.CustomerCategory; // CustomerCategory Enum 참조
import com.example.demo.Booking.entity.Showtime;

// import com.example.demo.Payment.Payment; // Payment 엔티티는 PaymentDto를 통해 간접 참조
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 예매(Booking) 처리 결과 또는 예매 상세 정보를 클라이언트에게 응답으로 전달할 때 사용하는 DTO 클래스.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {

    private Long bookingId; // 예매 고유 ID
    private Long userId; // 예매한 사용자의 ID
    private String movieTitle; // 예매한 영화의 제목
    private String theaterName; // 예매한 영화관의 이름
    private String auditoriumName; // 예매한 상영관 이름
    private LocalDateTime showtimeStartTime; // 예매한 상영 시작 시간
    private List<String> selectedSeatInfo; // 선택된 좌석 정보 문자열 목록 (예: ["A1", "A2", "B5"])
    private Map<CustomerCategory, Integer> passengerCounts; // 고객 유형별 인원수
    private Double totalPrice; // 총 결제 금액
    private String bookingStatus; // 예매 상태 (BookingStatus Enum의 이름을 문자열로 변환)
    private LocalDateTime bookingTime; // 예매 시도/확정 시간
    private PaymentDto paymentInfo; // 관련된 결제 정보 DTO (결제가 완료된 경우)

    
    public static BookingResponseDto fromEntity(Booking booking) { //
        if (booking == null) {
            return null;
        }

        // 선택된 좌석 정보를 "A1", "B2" 와 같은 문자열 리스트로 변환
        List<String> seatInfo = booking.getSelectedSeats().stream()
                .map(seat -> seat.getSeatRow() + seat.getSeatNumber()) 
                .collect(Collectors.toList());

        
        Long userIdResult = (booking.getUser() != null) ? booking.getUser().getId() : null; 
        String movieTitleResult = "정보 없음";
        String theaterNameResult = "정보 없음";
        String auditoriumNameResult = "정보 없음";
        LocalDateTime showtimeStartTimeResult = null;

        if (booking.getShowtime() != null) { //
            Showtime showtime = booking.getShowtime(); //
            showtimeStartTimeResult = showtime.getStartTime();
            auditoriumNameResult = showtime.getAuditoriumName();
            if (showtime.getMovie() != null) { //
                movieTitleResult = showtime.getMovie().getTitle(); //
            }
            if (showtime.getTheater() != null) { //
                theaterNameResult = showtime.getTheater().getName(); //
            }
        }

        return new BookingResponseDto(
                booking.getId(),
                userIdResult,
                movieTitleResult,
                theaterNameResult,
                auditoriumNameResult,
                showtimeStartTimeResult,
                seatInfo,
                booking.getPassengerCounts(), 
                booking.getTotalPrice(),
                booking.getStatus().name(), 
                booking.getBookingTime(),
                booking.getPayment() != null ? PaymentDto.fromEntity(booking.getPayment()) : null
        );
    }
}