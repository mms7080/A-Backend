package com.example.demo.Mypage;

import com.example.demo.Annotations.Auth;
import com.example.demo.Booking.dto.common.ApiResponseDto;
import com.example.demo.Booking.dto.response.BookingResponseDto;
import com.example.demo.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/mypage/bookings")
@RequiredArgsConstructor
public class MypageBookingControoler{

    private final MyPageBookingService myPageBookingService;

    // 예매 조회
    @GetMapping
    public ApiResponseDto<List<BookingResponseDto>> getMyBookings(@Auth User user) {
        if (user == null){
            return ApiResponseDto.error("예매 내역을 조회하려면 로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

        }
        List<BookingResponseDto> bookings = myPageBookingService.getUserBooking(user.getId());
        return ApiResponseDto.success(bookings, "예매 내역이 성공적으로 조회되었습니다.");
    }

    // 예매 취소
   @PostMapping("/{bookingId}/cancel")
    public ApiResponseDto<?> cancelBooking(@PathVariable Long bookingId, @Auth User user) {
        if (user == null) {
            return ApiResponseDto.error("예매를 취소하려면 로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }
        myPageBookingService.cancelBooking(bookingId, user.getId());
        return ApiResponseDto.success(null, "예매가 성공적으로 취소되었습니다.");
    }
   

}