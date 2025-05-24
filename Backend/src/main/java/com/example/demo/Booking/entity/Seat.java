package com.example.demo.Booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//개별 좌석 정보
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking_seats")
public class Seat {
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    // 좌석 행
    @Column(name = "seat_row", nullable = false)
    private String seatRow;

    // 좌석 번호
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    // 좌석의 현재 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    // 좌석이 속한 상영시간 정보
    // showtime과 ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;
}
