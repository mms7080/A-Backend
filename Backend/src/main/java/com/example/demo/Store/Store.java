package com.example.demo.Store;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "store_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_seq")
    @SequenceGenerator(name = "store_seq", sequenceName = "store_seq", allocationSize = 1)
    private Long id;

    private String category;      // 예: 메가티켓, 콤보, 포인트몰
    private String title;         // 상품명
    private String subtitle;      // 부제목 (선택)
    private String price;         // 현재가
    private String originalPrice; // 원래 가격 (선택)
    private String badge;         // NEW, 추천, 쿠폰 등
    private String badgeColor;    // 배지 색상
    private String imgUrl;        // 이미지 주소
}
