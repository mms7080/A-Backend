package com.example.demo.Booking.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Booking.dto.TheaterDto;
import com.example.demo.Booking.entity.Theater;
import com.example.demo.Booking.repository.TheaterRepository;

import jakarta.annotation.PostConstruct;



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

    // 특정 지역(region)에 해당하는 모든 영화관 목록을 TheaterDto 형태로 조회
    @Transactional(readOnly = true)
    public List<TheaterDto> getTheaterByRegion(String region){
        List<Theater> theaters = theaterRepository.findByRegion(region);
        return theaters.stream()
                .map(TheaterDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 ID에 해당하는 영화관(Theater) 엔티티를 조회
    @Transactional(readOnly = true)
    public Theater getTheaterById(Long id){
        return theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found with id" + id));
    }

    // 애플리케이션 시작 시점에 초기 영화관 데이터를 데이터베이스에 생성
    // 개발 및 테스트 환경에서 초기 데이터를 편리하게 설정하기 위해 사용
    @PostConstruct
    @Transactional
    public void initTheaters(){
        if (theaterRepository.count() == 0){
            List<Theater> theaters = Arrays.asList(
                new Theater(null, "강남점", "서울"), // ID는 null로 설정하여 데이터베이스가 자동 생성
                    new Theater(null, "홍대입구점", "서울"), 
                    new Theater(null, "코엑스점", "서울"), 
                    new Theater(null, "수원역점", "경기/인천"), 
                    new Theater(null, "인천터미널점", "경기/인천"), 
                    new Theater(null, "대전중앙로점", "충청/대전"), 
                    new Theater(null, "광주상무점", "전라/광주")
                
            );
            theaterRepository.saveAll(theaters);
            System.out.println("초기 영화관 데이터가 "+ theaters.size() +"개 생성되었습니다.");
        }
    }
}
