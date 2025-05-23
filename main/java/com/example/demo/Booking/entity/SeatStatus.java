package com.example.demo.Booking.entity;

// 좌석 상태를 나타내는 열거형(enum)
public enum SeatStatus {
    AVAILABLE, // 예약가능
    HELD,   // 임시 예약
    RESERVED // 결제완료
}
