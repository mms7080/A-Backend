package com.example.demo.booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Login.signinController;
import com.example.demo.booking.dto.ScreeningDto;
import com.example.demo.booking.entity.Screening;
import com.example.demo.booking.service.ScreeningService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




// 상영회차(Booking) REST API 컨트롤러
@RestController
@RequestMapping("/booking")
public class BookingController {

    private final ScreeningService screeningService;
    
    public BookingController(ScreeningService screeningService){
        this.screeningService = screeningService;
    }

    // 전체 상영회차 목록 조회
    // get /booking 
    @GetMapping
    public List<ScreeningDto> getAllScreenings() {
        return screeningService.getAllScreenings().stream()
                .map(this::toDto) 
                .collect(Collectors.toList());
    }

    // get /booking/{id}
    // 단일 상영회차 조회
    @GetMapping("/{id}")
    public ScreeningDto getScreeningById(@PathVariable Long id) {
        Screening screening = screeningService.getScreeningById(id);
        return toDto(screening);
    }
    
    
    // post /booking
    // 새로운 상영회차 등록
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScreeningDto createScreening(@RequestBody ScreeningDto dto){
        // DTO -> Entity로 변환
        Screening toCreate = toEntity(dto);
        Screening created = screeningService.createScreening(toCreate);
        return toDto(created);
    }

    // put /booking/{id}
    // 기존 상영회차 수정
    @PutMapping("/{id}")
    public ScreeningDto updateScreening(@PathVariable Long id,
                                        @RequestBody ScreeningDto dto) {
        // DTO -> Entity로 변환
        Screening updateEntity = toEntity(dto);
        Screening updated = screeningService.updateScreening(id, updateEntity);
        return toDto(updated);
    }
    
    // delete /booking/{id}
    // 상영회차 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScreening(@PathVariable Long id){
        screeningService.deleteScreening(id);
    }
    
    // Entity <-> DTO 변환 헬퍼 메서드

    // Screeing Entity -> ScreeingDto
    private ScreeningDto toDto(Screening s){
        return new ScreeningDto(
            s.getId(),
            s.getMovieTitle(),
            s.getTheaterName(),
            s.getStartTime()
        );
    }
    
    // ScreeningDto -> Screeing Entity
    private Screening toEntity(ScreeningDto dto){
        Screening s = new Screening();
        //ID는 생성 시 무시하고, 수정 시 서비스에서 id로 구분
        s.setMovieTitle(dto.getMovieTitle());
        s.setTheaterName(dto.getTheaterName());
        s.setStartTime(dto.getStarTime());
        return s;
    }
   
    
}
