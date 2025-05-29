package com.example.demo.Booking.dto.response;

 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class ShowtimeDto {

    // 상영 시간의 고유 ID (Showtime 엔티티의 기본 키와 매칭) 
    private Long id;

    /** 상영되는 영화의 ID */
    private Long movieId;

    /** 상영이 이루어지는 극장의 ID */
    private Long theaterId;

    //상영이 이루어지는 극장의 이름.
    private String theaterName;

    /** 실제 상영이 이루어지는 상영관의 이름 또는 번호 (예: "1관", "IMAX관") */
    private String screenNameOrNumber;

    /** 영화 상영 시작 시간 */
    private LocalDateTime startTime;

    /** 영화 상영 종료 시간 (선택적이며, 시작 시간과 영화 러닝타임으로 계산될 수도 있음) */
    private LocalDateTime endTime;

    /** 이 상영 시간의 기본 티켓 가격 */
    private Double ticketPrice;

    /** 현재 예매 가능한 좌석 수 */
    private Integer availableSeats;

    
   
}