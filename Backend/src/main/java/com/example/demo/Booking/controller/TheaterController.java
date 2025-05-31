package com.example.demo.Booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.dto.response.TheaterDto;
import com.example.demo.Booking.service.TheaterService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class TheaterController {
	
    private final TheaterService theaterService;

    @GetMapping("/theaters")
    public ApiResponseDto<List<TheaterDto>> getTheartersByRegion(@RequestParam String region) {
        return ApiResponseDto.success(theaterService.getTheatersByRegion(region));
    }
    
}
