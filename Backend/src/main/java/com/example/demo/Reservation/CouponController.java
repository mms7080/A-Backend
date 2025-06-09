package com.example.demo.Reservation;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CouponController {

    private final CouponRepository couponRepository;

    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @GetMapping
    public List<Coupon> getUserCoupons(@RequestParam String userId) {
        return couponRepository.findByUserIdAndUsedFalse(userId); // 사용되지 않은 것만 반환
    }

    // (선택) 테스트용 쿠폰 발급
    @PostMapping("/issue")
    public Coupon issueTestCoupon(@RequestBody Coupon request) {
        request.setUsed(false);
        return couponRepository.save(request);
    }
}