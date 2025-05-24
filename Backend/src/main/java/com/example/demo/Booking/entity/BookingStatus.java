package com.example.demo.Booking.entity;

// 예매 진핸 상태
public enum BookingStatus {
    PENDING_PAYMENT, //결제 대기중
    CONFIRMED, // 예매 확정
    CANCELED, // 예매 취소
    FAILED // 예매 실패
}
