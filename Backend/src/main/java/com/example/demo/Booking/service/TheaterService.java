package com.example.demo.Booking.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.response.TheaterDto;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.TheaterRepository;





// 영화관 정보 조회, 지역별 영화관 목록 조회 등의 기능을 제공
@Service
public class TheaterService {
    
    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository){
        this.theaterRepository = theaterRepository;
    }

    // 등록된 모든 영화관 지역 목록 조회
    @Transactional(readOnly = true)
    public List<String> getAllRegions(){
        return Arrays.asList("서울", "경기/인천","충청/대전","전라/광주","경남/부산","강원","제주");
    }

    

    
}
