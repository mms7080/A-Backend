package com.example.demo.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 사용자 ID로 쿠폰 목록 조회
    List<Coupon> findByUserId(String userId);

    // 사용되지 않은 쿠폰 목록 조회 (used = 0)
    List<Coupon> findByUserIdAndUsed(String userId, int used);

    // 사용된 쿠폰 목록 조회 (optional)
    List<Coupon> findByUserIdAndUsedNot(String userId, int used); // 예: used ≠ 0
}
