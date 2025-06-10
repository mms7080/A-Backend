package com.example.demo.Booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.dto.response.TheaterDto;
import com.example.demo.Booking.service.TheaterService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TheaterController {
	
    private final TheaterService theaterService;

    @GetMapping("/theaters") // 사용시 ?region=서울(예)
    public ApiResponseDto<List<TheaterDto>> getTheartersByRegion(@RequestParam String region) {
        return ApiResponseDto.success(theaterService.getTheatersByRegion(region));
    }

    @GetMapping("theaters/all")
    public ApiResponseDto<List<TheaterDto>> getTheaters() {
        return ApiResponseDto.success(theaterService.getAllTheaters());
    }
    
}
