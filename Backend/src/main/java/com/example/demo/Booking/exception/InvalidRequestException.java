package com.example.demo.Booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 클라이언트의 요청이 유효하지 않거나 비즈니스 규칙을 위반했을 때 발생하는 커스텀 예외 클래스입니다.
 * 이 예외가 발생하면 HTTP 400 Bad Request 상태 코드가 클라이언트에게 반환됩니다.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // 이 예외 발생 시 HTTP 상태 코드를 400으로 설정
public class InvalidRequestException extends RuntimeException {

    /**
     * 간단한 오류 메시지를 받는 생성자입니다.
     * @param message 클라이언트에게 전달될 오류 메시지 내용입니다.
     */
    public InvalidRequestException(String message) {
        super(message);
    }
    
    /**
     * 오류 메시지와 함께 원인이 된 다른 예외(Throwable cause)를 받는 생성자입니다.
     * 예외 체이닝(exception chaining)을 통해 근본 원인을 추적하는 데 도움이 됩니다.
     * @param message 클라이언트에게 전달될 오류 메시지 내용입니다.
     * @param cause 이 예외를 발생시킨 원인 예외 객체입니다.
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause); // 부모 클래스 생성자를 호출하여 메시지와 원인 예외를 설정합니다.
    }
}
