package com.example.demo.Booking.dto;

import com.example.demo.Booking.entity.Seat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    
    private Long id;
    private String seatRow;
    private Integer seatNumber;
    private String status;
    private Long showtimeId;

    public static SeatDto fromEntity(Seat seat){
        if (seat == null){
            return null;
        }

        Long showtimeIdResult = (seat.getShowtime() != null) ? seat.getShowtime().getId() : null;

        return new SeatDto(
            seat.getId(),
            seat.getSeatRow(),
            seat.getSeatNumber(),
            seat.getStatus().name(),
            showtimeIdResult
        );
    }
}
