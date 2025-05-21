package com.example.demo.Booking.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.Booking.entity.Reservation;
import com.example.demo.Booking.entity.Seat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id; // 예약 고유 식별자
    private String customerName; // 예약자 이름
    private String customerCatagory; // 고객분류 (성인/청소년/경로/장애인 등)
    private LocalDateTime reservedAt; // 예약 시간
    private List<Long> seatIds; // 예약된 좌석 ID 등록

    // 엔티티 -> DTO 변환
    public static ReservationDto fromEntity(Reservation reservation){
        // 엔티티의 Seat 리스트에만 ID 추출
        List<Long> ids = reservation.getSeats() != null ? reservation.getSeats().stream()
            .map(Seat::getId)
            .collect(Collectors.toList())
            : List.of();
        return new ReservationDto(
            reservation.getId(),
            reservation.getCustomerName(),
            reservation.getCustomerCategory(),
            reservation.getReservedAt(),
            ids
        );
    }

    // DTo -> Reservation 엔티티 반환
    public Reservation toEntity(){
        Reservation reservation = new Reservation();
        reservation.setId(this.id);
        reservation.setCustomerName(this.customerName);
        reservation.setCustomerCategory(this.customerCatagory);
        reservation.setReservedAt(this.reservedAt);
        // DTO에 담긴 seatId로 Seat 엔티티를 생성하여 설정
        if(this.seatIds != null){
            List<Seat> seats = this.seatIds.stream()
                .map(seatId ->{
                    Seat s = new Seat();
                    s.setId(seatId);
                    return s;
                })
                .collect(Collectors.toList());
                reservation.setSeats(seats);
        }
        return reservation;
    }



    
}
