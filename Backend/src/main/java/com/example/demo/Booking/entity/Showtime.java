package com.example.demo.Booking.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Movie.Movie;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "showtimes")
// 상영 시간표 정보 엔티티
public class Showtime {
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )

    private Long id; 

    // 상영시간에 상영될 영화 정보
    // Movie엔티티와 ManyToOne 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    // 상영시간이 진행될 영화관 정보
    // Theater 엔티티와 ManyToOne 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    // 영화 상영 시작 정확한 날짜와 시간
    private LocalDateTime startTime;

    // 실제 영화가 상영되는 상영관 이름
    private String auditoriumName;

    // 상영 회차의 기본 성인 티켓 가격
    private Double basePrice;

    // 상영시간에 할당된 모든 좌석 목록
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>(); //초기에는 빈 리스트로 생성
}
