package com.example.demo.Payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentKey;
    private String orderId;
    private int amount;
    private String userId;
    private String orderName;
    private String status; // 예: SUCCESS, FAIL

    private String approvedAt; // 결제 완료 시간 (ISO8601)

    private String method; // 카드, 가상계좌 등
    private String cardCompany; // 카드사 정보
    private String cardNumber;
}