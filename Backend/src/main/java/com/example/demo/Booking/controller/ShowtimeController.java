package com.example.demo.Booking.controller;

import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.dto.response.ShowtimeDto;
import com.example.demo.Booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping("/showtimes") // 예 ?movieId=1&heaterId=5&date2025-06-02
    public ApiResponseDto<List<ShowtimeDto>> getShowtimes(
            @RequestParam Long movieId,
            @RequestParam Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponseDto.success(showtimeService.getShowtimes(movieId, theaterId, date));
    }

    @GetMapping("/available-dates") // 예 ?movieId=1&theaterId=5
    public ApiResponseDto<List<LocalDate>> getAvailableDates(
            @RequestParam Long movieId,
            @RequestParam Long theaterId) {
        return ApiResponseDto.success(showtimeService.getAvailableDates(movieId, theaterId));
    }
}