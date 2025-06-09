package com.example.demo.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 사용자 ID로 쿠폰 목록 조회
    List<Coupon> findByUserId(String userId);

    // 사용되지 않은 쿠폰 목록 조회
    List<Coupon> findByUserIdAndUsedFalse(String userId);

    // 특정 주문 ID에 연결된 쿠폰 조회 (선택사항)
    // Optional<Coupon> findByOrderId(String orderId);
}