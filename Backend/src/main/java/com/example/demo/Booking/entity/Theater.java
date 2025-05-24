package com.example.demo.Booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 영화관 정보를 담는 엔티티
// 각 영화관의 이름, 지역 등의 기본 정보를 관리
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "theaters")
public class Theater {
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY)
    
    private Long id;

    // 영화관 이름
    private String name;

    // 영화관 지역
    private String region;

    
}
