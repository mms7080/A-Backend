package com.example.demo.Booking.entity;

// 좌석 상태
public enum SeatStatus {
    AVAILABLE, // 예매가능
    RESERVED, // 예매완료
    SElECTED, // 선택된 상태 (예매 대기 중)
    UNAVAILABLE // 예매 불가능 (예: 장애인석 등)    
}
