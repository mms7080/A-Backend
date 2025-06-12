package com.example.demo.Booking.controller;

import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.dto.response.SeatDto;
import com.example.demo.Booking.entity.SeatStatus;
import com.example.demo.Booking.service.SeatService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SeatController {
	
    private final SeatService seatService;

    @GetMapping("/showtimes/{showtimeId}/seats")
    public ApiResponseDto<List<SeatDto>> getSeatsByShowtime(@PathVariable Long showtimeId) {
        List<SeatDto> seats = seatService.getSeatsByShowtime(showtimeId);
        return ApiResponseDto.success(seats, "좌석 정보가 성공적으로 조회되었습니다.");
    }
    
    @GetMapping("/showtimes/{showtimeId}/seat/{seatName}/{status}")
    public ApiResponseDto<List<SeatDto>> setSeatsBySeatName(@PathVariable Long showtimeId, @PathVariable String seatName, @PathVariable SeatStatus status) {
        List<SeatDto> seats = seatService.setSeatsByShowtime(showtimeId, seatName, status);
        return ApiResponseDto.success(seats, "좌석 정보가 성공적으로 변경되었습니다.");
    }
}
