package com.example.demo.Booking.entity;

// 좌석 상태를 나타내는 열거형(enum)
public enum SeatStatus {
    AVAILABLE, //예매 가능 좌석
    HELD,   // 임시 예약(결제 전 잠금된 상태)좌석
    RESERVED // 결제 완료로 확정된 좌석
}
