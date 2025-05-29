package com.example.demo.Booking.dto.response;

import com.example.demo.Booking.entity.Seat;       
import com.example.demo.Booking.entity.SeatStatus; 
import lombok.Getter;
import lombok.NoArgsConstructor;

// 좌석 정보전달
@Getter
@NoArgsConstructor
public class SeatDto {
    
    private Long seatId;
    private String seatRow;
    private int seatNumber;
    private String fullSeatName;
    private SeatStatus status;

    public SeatDto(Seat seat){
        this.seatId = seat.getId();
        this.seatRow = seat.getSeatRow();
        this.seatNumber = seat.getSeatNumber();
        this.fullSeatName = seat.getFullSeatName();
        this.status = seat.getStatus();
    }
    
    public SeatDto(Long seatId, String seatRow, int seatNumber, String fullSeatName, SeatStatus status){
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatNumber  = seatNumber;
        this.fullSeatName = fullSeatName;
        this.status = status;
    }

    public static SeatDto fromEntity(Seat seat){
        if(seat == null){
            return null;
        }
        return new SeatDto(
            seat.getId(),
            seat.getSeatRow(),
            seat.getSeatNumber(),
            seat.getFullSeatName(),
            seat.getStatus()
        );
    }
}
