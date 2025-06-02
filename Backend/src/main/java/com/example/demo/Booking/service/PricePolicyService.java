package com.example.demo.Booking.service;

import com.example.demo.Booking.entity.CustomerCategory;
import java.math.BigDecimal;
import java.util.Map;

public interface PricePolicyService {

    // 고객 유형 단가
    BigDecimal getUnitPrice(CustomerCategory category);

    // 모든 고객 유형과 단가 정책
    Map<CustomerCategory, BigDecimal> getAllPricePolicies();

    // 인원수, 쿠폰 코드를 기반으로 최종 결제 금액 계산
    BigDecimal calculateTotalPrice(Map<CustomerCategory, Integer> customerCounts, String couponCode);

}
