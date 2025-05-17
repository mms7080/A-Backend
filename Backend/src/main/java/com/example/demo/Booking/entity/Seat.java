package com.example.demo.booking.entity;


import jakarta.persistence.Id;
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
@Table(name = "seat")
@Entity
@SequenceGenerator(
    name = "seq_seats",
    sequenceName = "seq_seat",
    allocationSize = 1
)
public class Seat {
    
    @Id
    @GeneratedValue(
        generator = "seq_seat",
        strategy = GenerationType.SEQUENCE
    )

    private Long id; // 좌석 고유 ID

    private String row; // 좌석 행(row)

    private Integer number; // 좌석 번호

    @Enumerated(EnumType.STRING)
    private SeatStatus status; // 좌석 상태 (예약 가능 좌석, 임시예약 좌석, 결제 완료 좌석)

    @ManyToOne(fetch = FetchType.LAZY) // N:1관계, 지연 로딩
    @JoinColumn(name = "SCREENING_ID") // 외래 키 컬럼 이름
    private Screening screening;  // 이 좌석이 속한 Screeing
}
