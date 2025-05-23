package com.example.demo.Booking.dto;

import com.example.demo.Booking.entity.Screening;
import com.example.demo.Booking.entity.Seat;
import com.example.demo.Booking.entity.SeatStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    
	private Long id; // 좌석 고유 식별자
	private String seatRow; // 좌석 행
	private Integer seatNumber; // 좌석 번호
	private SeatStatus status; // 좌석 상태
	private Long screeningId; // 연관된 상영회차 ID

	// 엔티티 -> DTO 변환
	public static SeatDto fromEntity(Seat seat){
		return new SeatDto(
			seat.getId(),
			seat.getSeatRow(),
			seat.getSeatNumber(),
			seat.getStatus(),
			seat.getScreening() !=null
				? seat.getScreening().getId() : null
		);
	}
		// DTo-> 엔티티 변환
		public Seat toEntity(){
			Seat seat = new Seat();
			seat.setId(this.id);
			seat.setSeatRow(this.seatRow);
			seat.setSeatNumber(this.seatNumber);
			seat.setStatus(this.status);

			if(this.screeningId !=null){
				Screening scr = new Screening();
				scr.setId(this.screeningId);
				seat.setScreening(scr);
			}
			return seat;
		}
	
}
