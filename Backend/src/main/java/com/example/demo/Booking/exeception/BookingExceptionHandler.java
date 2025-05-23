package com.example.demo.Booking.exeception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;



// Bokking 관련 예외를 처리하는 클래스 
@Slf4j
@ControllerAdvice( "com.example.demo.Booking") // Booking 패키지 내 컨트롤러에만 적용
public class BookingExceptionHandler {
	// 404에러
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request ) {
		log.error("Resource not found exception: {}", ex.getMessage(), ex); // 예외 스택 트레이스 로깅 추가
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

// 400 에러를 발생시키는 예외 처리
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
		log.error("Invalid request exception: {}", ex.getMessage(), ex); // 예외 스택 트레이스 로깅 추가
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
		body.put("message", ex.getMessage());
		body.put("path", request.getDescription(false).replace("uri=", ""));
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}	

// 500 에러를 발생시키는 예외 처리
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex); // 예외 스택 트레이스 로깅 추가
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        // 실제 운영 환경에서는 상세한 내부 오류 메시지를 사용자에게 노출하지 않는 것이 좋습니다.
        body.put("message", "An unexpected internal server error occurred. Please try again later.");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
