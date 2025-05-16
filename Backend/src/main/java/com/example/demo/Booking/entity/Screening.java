package com.example.demo.Booking.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "screenings")
@SequenceGenerator(
    name = "seq_screenings",
    allocationSize = 1,
    sequenceName = "seq_screenings"
)

// 상영회차 정보
public class Screening {

    @Id
    @GeneratedValue(
        generator = "seq_screenings",
        strategy = GenerationType.SEQUENCE
    )
    private Long id; // 상영회차 고유 식별자

    private String movieTitle; // 영화 제목

    private String theaterName; // 상영관 이름
 
    private LocalDateTime startTime; // 상영 시작 시간 

    @OneToMany(
        mappedBy = "screenings", // seat 엔티티의 screenings 필드가 외래키 관리
        fetch = FetchType.LAZY, // 실제 사용할 때 조회
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }, // 저장, 수정만 전파
        orphanRemoval = true // 리스트에서 빠진 좌석은 자동 삭제
    )
    // 상영회자에 속한 좌석 목록
    private List<Seat> seats; // 연결된 좌석 목록



    
}
