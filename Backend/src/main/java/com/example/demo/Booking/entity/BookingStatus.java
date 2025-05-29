package com.example.demo.Booking.entity;

// 예매 진핸 상태
public enum BookingStatus {
    PENDING_PAYMENT, //결제 대기중
    CONFIRMED, // 예매 확정
    CANCELLED_BY_USER,  // 사용자 요청으로 취소됨
    CANCELLED_BY_SYSTEM // 시스템에 의해 자동 취소됨 
}
