package com.example.demo.Booking.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// 영화관 정보를 담는 엔티티
// 각 영화관의 이름, 지역 등의 기본 정보를 관리
@Data
@Getter
@Setter
@Entity
@Table(name = "theaters")
public class Theater {
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY)
    
    @Column(name = "theater_id")
    private Long id;

    // 영화관 이름
    @Column(nullable = false, length = 100)
    private String name;

    // 영화관 지역
    @Column(nullable = false, length = 50)
    private String region;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Showtime> showtimes = new ArrayList<>();

    @Builder // Lombok의 빌더 패턴 자동 생성
    public Theater(String name, String region) {
        this.name = name;
        this.region = region;
    }

    public void addShowtime(Showtime showtime) {
        this.showtimes.add(showtime);
        if (showtime.getTheater() != this) { // 무한루프 방지
            showtime.setTheater(this);
        }
    }
    
}
