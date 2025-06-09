package com.example.demo.Reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // 사용자 식별자 (username)

    private String type; // "GENERAL_TICKET" 또는 "DISCOUNT"

    private int discountAmount; // 할인 금액 (일반권은 0, 할인권은 1000 등)

    @Column(columnDefinition = "CHAR(1)")
    private boolean used;

    private String description; // 예: "일반 관람권", "1000원 할인 쿠폰"
}
