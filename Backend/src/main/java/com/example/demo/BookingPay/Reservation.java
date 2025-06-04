package com.example.demo.BookingPay;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;
    private String movieTitle;
    private String region;
    private String theater;
    private String date;
    private String time;
    private String seats; // "A1,B2" 이런 문자열로 저장

    private int adult;
    private int teen;
    private int senior;
    private int special;

    private int totalPrice;

    private String userId;

    private String paymentId; // paymentKey 또는 orderId 등과 연결
}
