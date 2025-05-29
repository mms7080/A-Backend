package com.example.demo.Booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private static final List<String> REGIONS = Arrays.asList(
        "서울",
            "경기/인천",
            "충청/대전",
            "전라/광주",
            "경남/부산",
            "강원",
            "제주"
    );

    public List<String> getAllRegions(){
        return REGIONS;
    }
}
