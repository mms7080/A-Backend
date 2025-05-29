package com.example.demo.Booking.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"showtime_id", "seat_row", "seat_number"})
})
@Getter
@Setter
@NoArgsConstructor 
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "seat_row", nullable = false, length = 3)
    private String seatRow;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) 
    private SeatStatus status; 

    @ManyToMany(mappedBy = "selectedSeats", fetch = FetchType.LAZY)
    private Set<Booking> bookings = new HashSet<>();


    
    @Builder
    public Seat(Showtime showtime, String seatRow, int seatNumber, SeatStatus status) {
        this.showtime = showtime;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public String getFullSeatName() {
        return this.seatRow + this.seatNumber;
    }
}