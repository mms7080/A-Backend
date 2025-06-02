package com.example.demo.Booking.service;

import com.example.demo.Booking.entity.CustomerCategory;
import com.example.demo.Booking.exception.CustomBookingException; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultPricePolicyService implements PricePolicyService  {
    
    private static final Map<CustomerCategory, BigDecimal> PRICE_POLICY_MAP = Map.of(
        CustomerCategory.ADULT, new BigDecimal("15000"),
        CustomerCategory.YOUTH, new BigDecimal("12000"),
        CustomerCategory.SENIOR, new BigDecimal("10000"), 
        CustomerCategory.DISABLED, new BigDecimal("8000")  
    );

    private static final String VALID_COUPON_CODE = "FILM10"; // 10%할인 쿠폰폰
    private static final BigDecimal DISCOUNT_PERCENTAGE = new BigDecimal("0.10"); 

    @Override
    public BigDecimal getUnitPrice(CustomerCategory category) {
        BigDecimal price = PRICE_POLICY_MAP.get(category);
        if (price == null) {
            throw new CustomBookingException("알 수 없는 고객 유형에 대한 가격 정책이 없습니다: " + category, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return price;
    }

    @Override
    public Map<CustomerCategory, BigDecimal> getAllPricePolicies() {
        return Collections.unmodifiableMap(PRICE_POLICY_MAP); // 외부에서 변경 불가능
    }

    @Override
    public BigDecimal calculateTotalPrice(Map<CustomerCategory, Integer> customerCounts, String couponCode) {
        if (customerCounts == null || customerCounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal grossPrice = customerCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // 인원수가 0보다 큰 경우만 계산
                .map(entry -> {
                    BigDecimal unitPrice = getUnitPrice(entry.getKey()); 
                    return unitPrice.multiply(new BigDecimal(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 할인 적용
        if (VALID_COUPON_CODE.equalsIgnoreCase(couponCode)) {
            BigDecimal discountAmount = grossPrice.multiply(DISCOUNT_PERCENTAGE);
            return grossPrice.subtract(discountAmount).setScale(0, RoundingMode.HALF_UP); // 원단위 반올림
        }
        
        return grossPrice.setScale(0, RoundingMode.HALF_UP); // 할인 없을 시 최종 금액 처리
    }
}
