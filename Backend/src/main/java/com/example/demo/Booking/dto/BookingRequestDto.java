package com.example.demo.Booking.dto;

import java.util.List;
import java.util.Map;

import com.example.demo.Booking.entity.CustomerCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    
    private Long userId;

    private Long showtimeId;
    private Map<CustomerCategory, Integer> passengerCounts;
    private List<Long> seatIds;
}
