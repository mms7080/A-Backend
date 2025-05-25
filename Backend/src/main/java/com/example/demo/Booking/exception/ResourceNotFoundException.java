package com.example.demo.Booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 요청한 리소스를 찾을 수 없을 때 발생하는 커스텀 예외 클래스입니다.
 * 이 예외가 발생하면 HTTP 404 Not Found 상태 코드가 클라이언트에게 반환됩니다.
 * @ResponseStatus(HttpStatus.NOT_FOUND) 어노테이션이 이 역할을 수행합니다.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // 이 예외 발생 시 HTTP 상태 코드를 404로 설정
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 간단한 오류 메시지를 받는 생성자입니다.
     * @param message 클라이언트에게 전달될 오류 메시지 내용입니다.
     */
    public ResourceNotFoundException(String message) {
        super(message); // 부모 클래스(RuntimeException)의 생성자를 호출하여 메시지를 설정합니다.
    }
    
    /**
     * 리소스 이름, 필드 이름, 필드 값을 받아 좀 더 상세한 오류 메시지를 생성하는 생성자입니다.
     * 예: "User not found with id : '123'" 와 같은 메시지를 생성할 수 있습니다.
     * @param resourceName 찾을 수 없는 리소스의 이름 (예: "User", "Booking").
     * @param fieldName 리소스를 찾는 데 사용된 필드의 이름 (예: "id", "username").
     * @param fieldValue 리소스를 찾는 데 사용된 필드의 실제 값.
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // String.format을 사용하여 포맷화된 오류 메시지를 생성합니다.
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}