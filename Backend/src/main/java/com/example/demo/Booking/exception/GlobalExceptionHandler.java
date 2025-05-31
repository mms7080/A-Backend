package com.example.demo.Booking.exception;

import com.example.demo.Booking.dto.common.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.error(ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomBookingException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleCustomBookingException(CustomBookingException ex) {
        ApiResponseDto<Void> response = ApiResponseDto.error(ex.getMessage(), ex.getHttpStatus());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 유효성 검사 실패 시 첫 번째 오류 메시지를 사용
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiResponseDto<Void> response = ApiResponseDto.error(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGeneralException(Exception ex) {
        // 모든 예상치 못한 예외 처리
        // 실제 운영 환경에서는 ex.getMessage() 대신 좀 더 일반적인 메시지를 사용하거나 로깅만 할 수 있습니다.
        ApiResponseDto<Void> response = ApiResponseDto.error("서버 내부 오류가 발생했습니다: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}