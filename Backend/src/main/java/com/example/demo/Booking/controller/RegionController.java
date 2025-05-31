package com.example.demo.Booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.service.RegionService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class RegionController {
	
    private final RegionService regionService;

    @GetMapping("/regions")
    public ApiResponseDto<List<String>> getAllRegions() {
        return ApiResponseDto.success(regionService.getAllRegions());
    }
    
}
