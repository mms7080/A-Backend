package com.example.demo.Booking.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 잘못된 요청이나 비즈니스 규칙 위반 시 발생하는 예외
// 400 (Bad Request) 에러를 발생시킴
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {

	public InvalidRequestException(String message) {
		super(message);
	}
	
	// 메시지와 원인 예외를 포함하는 생성자
	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
