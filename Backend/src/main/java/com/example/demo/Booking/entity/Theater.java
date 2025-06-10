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
import lombok.NoArgsConstructor;
import lombok.Setter;

// 영화관 정보를 담는 엔티티
// 각 영화관의 이름, 지역 등의 기본 정보를 관리
@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
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

    // 영화관 주소
    @Column(length = 100)
    private String address;

    // 지하철 안내
    @Column(length = 700)
    private String subway;

    // 버스 안내
    @Column(length = 700)
    private String bus;

    // 주차 안내
    @Column(name = "park_guide", length = 700)
    private String parkGuide;

    // 주차 확인 안내
    @Column(name = "park_check", length = 200)
    private String parkCheck;

    // 주차 요금 안내
    @Column(name = "park_fee", length = 500)
    private String parkFee;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Showtime> showtimes = new ArrayList<>();

    @Builder // Lombok의 빌더 패턴 자동 생성
    public Theater(String name, String region, String address, String subway, 
                   String bus, String parkGuide, String parkCheck, String parkFee) {
        this.name = name;
        this.region = region;
        this.address = address;
        this.subway = subway;
        this.bus = bus;
        this.parkGuide = parkGuide;
        this.parkCheck = parkCheck;
        this.parkFee = parkFee;
    }

    public void addShowtime(Showtime showtime) {
        this.showtimes.add(showtime);
        if (showtime.getTheater() != this) { // 무한루프 방지
            showtime.setTheater(this);
        }
    }
    
}
