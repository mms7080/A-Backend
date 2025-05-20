package com.example.demo.Booking.entity;


import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 좌석(행 (row), 열(number), 예약 가능 여부)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seats")
@Entity
@SequenceGenerator(
    name = "seq_seats",
    sequenceName = "seq_seats",
    allocationSize = 1
)
public class Seat {
    
    @Id
    @GeneratedValue(
        generator = "seq_seats",
        strategy = GenerationType.SEQUENCE
    )

    private Long id; // 좌석 고유 ID

    @Column(name = "seat_row", nullable = false)
    private String seatRow; // 좌석 행(row)

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber; // 좌석 번호

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status; // 좌석 상태 (예약 가능 좌석, 임시예약 좌석, 결제 완료 좌석)

    @ManyToOne(fetch = FetchType.LAZY) // N:1관계, 지연 로딩
    @JoinColumn(name = "screening_id") // 외래 키 컬럼 이름
    private Screening screening;  // 이 좌석이 속한 Screeing
}
