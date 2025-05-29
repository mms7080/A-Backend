package com.example.demo.Booking.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Movie.Movie;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "showtimes")
// 상영 시간표 정보 엔티티
public class Showtime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // 여러 상영시간표(Showtime)는 하나의 극장(Theater)에 해당
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(nullable = false)
    private LocalDateTime startTime; // 상영 시작 시간
    

    @Column(nullable = false, length = 50)
    private String auditoriumName; // 상영관 이름

    // 하나의 상영시간표(Showtime)는 여러 좌석(Seat)을 가짐
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    // 하나의 상영시간표(Showtime)는 여러 예매(Booking) 정보를 가짐
    @OneToMany(mappedBy = "showtime", fetch = FetchType.LAZY) 
    private List<Booking> bookings = new ArrayList<>();

    @Builder
    public Showtime(Movie movie, Theater theater, LocalDateTime startTime, String auditoriumName) {
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.auditoriumName = auditoriumName;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
        if (seat.getShowtime() != this) { // 무한루프 방지
            seat.setShowtime(this);
        }
    }

     public void addBooking(Booking booking) {
        this.bookings.add(booking);
        if (booking.getShowtime() != this) { // 무한루프 방지
            booking.setShowtime(this);
        }
    }
}
