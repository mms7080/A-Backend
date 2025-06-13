package com.example.demo.Reservation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CouponController {

    private final CouponRepository couponRepository;

    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    // ✅ 사용되지 않은 쿠폰 목록 조회
    @GetMapping
    public List<Coupon> getUserCoupons(@RequestParam String userId) {
        return couponRepository.findByUserIdAndUsed(userId, 0); // 사용되지 않은 것만 반환
    }

    // 🧪 테스트용 쿠폰 발급
    @PostMapping("/issue")
    public Coupon issueTestCoupon(@RequestBody Coupon request) {
        request.setUsed(false);
        return couponRepository.save(request);
    }

    // ✅ 쿠폰 사용 처리
    @PostMapping("/use")
    @Transactional
    public ResponseEntity<String> useCoupon(@RequestBody Map<String, Object> payload) {
        try {
            Long couponId = Long.parseLong(payload.get("couponId").toString());

            Optional<Coupon> couponOpt = couponRepository.findById(couponId);
            if (couponOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("쿠폰을 찾을 수 없습니다");
            }

            Coupon coupon = couponOpt.get();
            if (coupon.isUsed()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용된 쿠폰입니다");
            }

            coupon.setUsed(true);
            couponRepository.saveAndFlush(coupon); 


            return ResponseEntity.ok("쿠폰 사용 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
}
