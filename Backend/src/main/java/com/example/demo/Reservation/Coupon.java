package com.example.demo.Reservation;

import jakarta.persistence.*;
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

    private String userId;

    private String type;

    private int discountAmount;

    @Column(name = "used")
    private int used; // Oracle에서는 int로 처리

    private String description;

    // 편의용 getter/setter (boolean처럼 사용 가능)
    public boolean isUsed() {
        return this.used == 1;
    }

    public void setUsed(boolean value) {
        this.used = value ? 1 : 0;
    }
}
