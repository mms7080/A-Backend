package com.example.demo.Event;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    //  기본 키 (자동 증가)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  이벤트 제목
    private String title;

    //  이벤트 기간 (예: "2025.06.01 ~ 2025.06.30")
    @Column(name = "date_range")
    private String date;

    //  이벤트 카테고리 (예: 추천, 메가Pick 등)
    private String category;

    //  이미지 목록 (1:N 구조) → 별도 테이블 생성됨
    @ElementCollection
    @CollectionTable(
        name = "event_images", // 생성될 테이블 이름
        joinColumns = @JoinColumn(name = "event_id") // 외래 키
    )
    @Column(name = "image_url") // 각 row의 컬럼 이름
    private List<String> images = new ArrayList<>();
}
