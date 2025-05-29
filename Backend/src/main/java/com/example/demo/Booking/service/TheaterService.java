package com.example.demo.Booking.service;

import com.example.demo.Booking.dto.response.TheaterDto;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.TheaterRepository;
import com.example.demo.Booking.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList; 
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
    private final RegionService regionService;

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

    @PostConstruct
    @Transactional
    public void initializeTheaters() {
        log.info("Initializing theaters for each region...");
        List<String> regions = regionService.getAllRegions();

        for (String region : regions) {
            List<Theater> existingTheatersInRegion = theaterRepository.findByRegion(region);
            int currentTheaterCount = existingTheatersInRegion.size();
            int theatersToCreateTarget = 4; // 각 지역별 목표 극장 수

            if (currentTheaterCount < theatersToCreateTarget) {
                List<String> exampleNamesForRegion = EXAMPLE_THEATER_NAMES_BY_REGION.getOrDefault(region, new ArrayList<>());
                List<String> existingNames = existingTheatersInRegion.stream().map(Theater::getName).collect(Collectors.toList());
                int namesCreated = 0;

                // 1. 예시 목록에서 아직 없는 이름으로 생성
                for (String exampleName : exampleNamesForRegion) {
                    if (namesCreated >= (theatersToCreateTarget - currentTheaterCount)) break; 
                    if (!existingNames.contains(exampleName)) {
                        Theater newTheater = Theater.builder()
                                .name(exampleName)
                                .region(region)
                                .build();
                        theaterRepository.save(newTheater);
                        log.info("Created theater: {} in {}", exampleName, region);
                        namesCreated++;
                    }
                }

                // 2. 그래도 부족하면 더미 이름으로 추가 생성
                int stillNeedToCreate = (theatersToCreateTarget - currentTheaterCount) - namesCreated;
                for (int i = 0; i < stillNeedToCreate; i++) {
                    String theaterName = region + " 임시 " + (currentTheaterCount + namesCreated + i + 1) + "호점";
                    if (!theaterRepository.findByName(theaterName).isEmpty()) { // 이름 중복 체크
                       theaterName = region + " 임시 " + (currentTheaterCount + namesCreated + i + 1) + "호점_" + System.currentTimeMillis()%1000;
                    }

                    Theater newTheater = Theater.builder()
                            .name(theaterName)
                            .region(region)
                            .build();
                    theaterRepository.save(newTheater);
                    log.info("Created dummy theater: {} in {}", theaterName, region);
                }
            }
        }
        log.info("Theater initialization complete.");
    }
    
public List<TheaterDto> getTheatersByRegion(String region) {
        List<Theater> theaters = theaterRepository.findByRegion(region);
        return theaters.stream()
                .map(TheaterDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TheaterDto getTheaterById(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 극장을 찾을 수 없습니다: " + theaterId));
        return TheaterDto.fromEntity(theater);
    }

    public List<TheaterDto> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(TheaterDto::fromEntity)
                .collect(Collectors.toList());
    }
    
}
