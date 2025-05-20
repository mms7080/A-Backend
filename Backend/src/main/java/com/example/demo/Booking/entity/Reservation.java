package com.example.demo.Booking.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 예약 엔티티
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
@SequenceGenerator(
    name = "seq_reservations",
    sequenceName = "seq_reservations",
    allocationSize = 1
)
public class Reservation {
    
    @Id
    @GeneratedValue(
        generator = "seq_reservations",
        strategy = GenerationType.SEQUENCE
    )

    private Long id;

    private String customerName; //예약자 이름
 
    private String customerCategory; // 성인/ 청소년/ 경로 / 장애인

    private LocalDateTime reservedAt; // 예약 시간

    @ManyToMany
    @JoinTable(
        name = "reservation_seats",
        joinColumns = @JoinColumn(name ="reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "seat_id")
    )

    private List<Seat> seats; // 예약된 좌석들
}
