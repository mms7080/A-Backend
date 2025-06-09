package com.example.demo.Reservation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Long movieId;
    private String region;
    private String theater;

    @Column(name = "show_date")
    private String date;

    @Column(name = "show_time")
    private String time;

    private String seats; // "A1,B2,C3"처럼 문자열로 저장

    private int adultCount;
    private int teenCount;
    private int seniorCount;
    private int specialCount;

    private int totalPrice;
    private String orderId; // 결제와 연결되는 ID

    @Column(name = "approved_at")
    private String approvedAt; // ✅ 추가: 결제 승인 시각 (예: 2025-06-09T09:14:27+09:00)

    @Column(name = "status")
    private String status; //환불권한  예: "CONFIRMED", "CANCELED"


}
