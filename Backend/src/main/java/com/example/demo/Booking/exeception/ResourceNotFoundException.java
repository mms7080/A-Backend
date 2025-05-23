package com.example.demo.Booking.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 요청한 리소스를 찾을 수 없을때 발생하는 예외
// 404 (Not Found) 에러를 발생시킴
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	// 리소스명, 필드명, 필드값을 받아서 예외 메시지를 생성하는 생성자
	// 예를 들어, "User not found with id : '1'" 과 같은 메시지를 생성
	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
	}


}