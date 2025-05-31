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
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping("/showtimes")
    public ApiResponseDto<List<ShowtimeDto>> getShowtimes(
            @RequestParam Long movieId,
            @RequestParam Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponseDto.success(showtimeService.getShowtimes(movieId, theaterId, date));
    }

    @GetMapping("/available-dates")
    public ApiResponseDto<List<LocalDate>> getAvailableDates(
            @RequestParam Long movieId,
            @RequestParam Long theaterId) {
        return ApiResponseDto.success(showtimeService.getAvailableDates(movieId, theaterId));
    }
}