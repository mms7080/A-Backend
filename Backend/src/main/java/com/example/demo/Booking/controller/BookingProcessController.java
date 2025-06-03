package com.example.demo.Booking.controller;

import com.example.demo.Annotations.Auth;
import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.dto.request.BookingRequestDto;
import com.example.demo.Booking.dto.response.BookingResponseDto;
import com.example.demo.Booking.service.BookingProcessService;
import com.example.demo.User.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; 

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BookingProcessController {

    private final BookingProcessService bookingProcessService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) 
    public ApiResponseDto<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingRequestDto requestDto,
            @Auth User user) {
        
        if (user == null) {
            return ApiResponseDto.error("예매를 생성하려면 로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        BookingResponseDto responseDto = bookingProcessService.createBooking(requestDto, user.getId());
        return ApiResponseDto.success(responseDto, "예매가 성공적으로 생성되었습니다.");
    }
}