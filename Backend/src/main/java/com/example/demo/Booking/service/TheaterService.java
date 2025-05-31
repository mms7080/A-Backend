package com.example.demo.Booking.service;

import com.example.demo.Booking.dto.response.TheaterDto;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.TheaterRepository;

import jakarta.annotation.PostConstruct;

import com.example.demo.Booking.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Collections; 
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TheaterService {
    
    private static final Logger log = LoggerFactory.getLogger(TheaterService.class); // 로거 선언
    private final TheaterRepository theaterRepository;
    

    private static final Map<String, List<String>> EXAMPLE_THEATER_NAMES_BY_REGION;

    static {
        Map<String, List<String>> tempMap = new HashMap<>();
        tempMap.put("서울", Arrays.asList("CGV 강남", "롯데시네마 월드타워", "메가박스 코엑스", "대한극장", "CGV 용산아이파크몰"));
        tempMap.put("경기/인천", Arrays.asList("CGV 판교", "롯데시네마 수원", "메가박스 송도", "CGV 인천터미널", "메가박스 킨텍스"));
        tempMap.put("충청/대전", Arrays.asList("CGV 대전터미널", "롯데시네마 대전센트럴", "메가박스 대전중앙로", "CGV 청주지웰시티"));
        tempMap.put("전라/광주", Arrays.asList("CGV 광주터미널", "롯데시네마 광주광산", "메가박스 전주객사", "CGV 광주상무"));
        tempMap.put("경남/부산", Arrays.asList("CGV 센텀시티", "롯데시네마 부산본점", "메가박스 해운대(장산)", "CGV 서면", "영화의전당"));
        tempMap.put("강원", Arrays.asList("CGV 춘천", "롯데시네마 원주무실", "메가박스 강릉", "CGV 강릉"));
        tempMap.put("제주", Arrays.asList("CGV 제주", "롯데시네마 제주아라", "메가박스 제주삼화", "CGV 제주노형"));
        EXAMPLE_THEATER_NAMES_BY_REGION = Collections.unmodifiableMap(tempMap);
    }

    // 💡 서버 시작 시 더미 극장 데이터 자동 삽입
    @PostConstruct
    @Transactional 
    public void initTheaters() {
        if (theaterRepository.count() == 0) { // DB에 극장 데이터가 없을 경우에만 실행
            log.info("Initializing dummy theater data...");
            EXAMPLE_THEATER_NAMES_BY_REGION.forEach((region, theaterNames) -> {
                theaterNames.forEach(name -> {
                    theaterRepository.save(Theater.builder().name(name).region(region).build());
                });
            });
            log.info("Dummy theater data initialization complete.");
        }
    }
    
public List<TheaterDto> getTheatersByRegion(String region) {
        log.debug("Fetching theaters for region: {}", region); 
        List<Theater> theaters = theaterRepository.findByRegion(region);
        if (theaters.isEmpty()) {
            log.info("No theaters found for region: {}", region);
        }
        return theaters.stream()
                .map(TheaterDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public TheaterDto getTheaterById(Long theaterId) {
        log.debug("Fetching theater by ID: {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> {
                    log.warn("Theater not found with ID: {}", theaterId);
                    return new ResourceNotFoundException("해당 ID의 극장을 찾을 수 없습니다: " + theaterId);
                });
        return TheaterDto.fromEntity(theater);
    }

}
