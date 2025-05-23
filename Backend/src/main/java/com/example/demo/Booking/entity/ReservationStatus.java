package com.example.demo.Booking.entity;

// 예약의 상태를 나타내는 enum 클래스
public enum ReservationStatus {
	ATTEMPTED,       // 예매 시도 (좌석 HELD, 결제 전 또는 결제 모듈 연동 전 임시 상태)
    CONFIRMED,       // 예매 확정 (결제 완료 가정 또는 결제 기능 없을 시 이 상태로 바로 변경)
    CANCELED,        // 예매 취소
    FAILED           // 예매 시도 실패 (좌석 확보 실패 등, 결제 실패와는 별개일 수 있음)
}
