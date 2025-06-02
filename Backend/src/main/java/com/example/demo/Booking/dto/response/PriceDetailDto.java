package com.example.demo.Booking.dto.response; 

import com.example.demo.Booking.entity.CustomerCategory; 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDetailDto {
    private String categoryDisplay; // 예: "성인", "청소년"
    private int count;              // 인원수
    private BigDecimal unitPrice;   // 단가
    private BigDecimal subtotal;    // 소계 (인원수 * 단가)
}