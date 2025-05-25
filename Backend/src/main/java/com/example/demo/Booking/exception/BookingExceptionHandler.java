package com.example.demo.Booking.exception;

import org.slf4j.Logger; // 로깅을 위한 SLF4J Logger 인터페이스
import org.slf4j.LoggerFactory; // SLF4J Logger 인스턴스를 생성하기 위한 팩토리
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice; // 전역 예외 처리를 위한 어노테이션
import org.springframework.web.bind.annotation.ExceptionHandler; // 특정 예외를 처리하는 메서드를 지정하는 어노테이션
import org.springframework.web.context.request.WebRequest; // 현재 웹 요청에 대한 정보를 제공하는 인터페이스

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * "com.example.demo.Booking" 패키지 내의 컨트롤러에서 발생하는 예외들을
 * 전역적으로 처리하는 핸들러 클래스입니다.
 * @ControllerAdvice("com.example.demo.Booking") 어노테이션은 이 클래스가
 * 지정된 패키지 범위 내의 컨트롤러에만 적용되도록 합니다.
 * 이를 통해 예외 처리 로직을 특정 도메인에 국한시킬 수 있습니다.
 */
// @Slf4j // Lombok 어노테이션: private static final Logger log = LoggerFactory.getLogger(BookingExceptionHandler.class); 자동 생성
@ControllerAdvice("com.example.demo.Booking") // 이 핸들러가 "com.example.demo.Booking" 패키지 내의 컨트롤러에만 적용됨
public class BookingExceptionHandler {

    // Logger 인스턴스를 수동으로 생성 (또는 @Slf4j 사용)
    private static final Logger log = LoggerFactory.getLogger(BookingExceptionHandler.class);

    /**
     * ResourceNotFoundException 타입의 예외가 발생했을 때 호출되는 핸들러 메서드입니다.
     * HTTP 404 Not Found 상태 코드와 함께 JSON 형태의 오류 응답을 생성하여 반환합니다.
     * @param ex 발생한 ResourceNotFoundException 객체입니다.
     * @param request 현재 HTTP 요청에 대한 정보(예: 요청 URI)를 담고 있는 WebRequest 객체입니다.
     * @return HTTP 404 상태 코드와 오류 상세 정보를 담은 ResponseEntity<Object> 객체입니다.
     */
    @ExceptionHandler(ResourceNotFoundException.class) // ResourceNotFoundException 예외를 처리하도록 지정
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) { //
        // 오류 로깅: 어떤 예외가 발생했는지 서버 로그에 기록합니다 (디버깅 및 모니터링에 유용).
        log.error("Resource not found exception: {}", ex.getMessage(), ex); // 예외 메시지와 스택 트레이스를 함께 로깅합니다.
        
        // 클라이언트에게 반환할 오류 응답 본문을 Map 형태로 구성합니다.
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now()); // 오류 발생 시각
        body.put("status", HttpStatus.NOT_FOUND.value()); // HTTP 상태 코드 (404)
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase()); // HTTP 상태 메시지 (예: "Not Found")
        body.put("message", ex.getMessage()); // 예외 객체에 담긴 구체적인 오류 메시지
        body.put("path", request.getDescription(false).replace("uri=", "")); // 오류가 발생한 요청 경로 (URI)
        
        // 구성된 응답 본문과 HTTP 상태 코드를 사용하여 ResponseEntity 객체를 생성하여 반환합니다.
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * InvalidRequestException 타입의 예외가 발생했을 때 호출되는 핸들러 메서드입니다.
     * HTTP 400 Bad Request 상태 코드와 함께 JSON 형태의 오류 응답을 생성하여 반환합니다.
     * @param ex 발생한 InvalidRequestException 객체입니다.
     * @param request 현재 HTTP 요청에 대한 정보를 담고 있는 WebRequest 객체입니다.
     * @return HTTP 400 상태 코드와 오류 상세 정보를 담은 ResponseEntity<Object> 객체입니다.
     */
    @ExceptionHandler(InvalidRequestException.class) // InvalidRequestException 예외를 처리하도록 지정
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) { //
        log.error("Invalid request exception: {}", ex.getMessage(), ex); // 오류 로깅
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // HTTP 상태 코드 (400)
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase()); // HTTP 상태 메시지 (예: "Bad Request")
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }    

    /**
     * IllegalArgumentException 타입의 예외가 발생했을 때 호출되는 핸들러 메서드입니다.
     * (예: 서비스 메서드에 잘못된 인자 값이 전달되었을 때 발생)
     * HTTP 400 Bad Request 상태 코드와 함께 JSON 형태의 오류 응답을 생성하여 반환합니다.
     * @param ex 발생한 IllegalArgumentException 객체입니다.
     * @param request 현재 HTTP 요청에 대한 정보를 담고 있는 WebRequest 객체입니다.
     * @return HTTP 400 상태 코드와 오류 상세 정보를 담은 ResponseEntity<Object> 객체입니다.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.warn("Illegal argument exception: {}", ex.getMessage()); // WARN 레벨로 로깅 (클라이언트 요청 오류이므로)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage()); // 예외 메시지를 그대로 전달
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 위에서 명시적으로 처리되지 않은 모든 기타 RuntimeException 타입의 예외가 발생했을 때 호출되는 핸들러 메서드입니다.
     * 이는 예기치 않은 서버 내부 오류를 처리하기 위한 최후의 방어선 역할을 합니다.
     * HTTP 500 Internal Server Error 상태 코드와 함께 일반적인 오류 메시지를 JSON 형태로 반환합니다.
     * @param ex 발생한 RuntimeException 객체 (또는 그 하위 타입 예외)입니다.
     * @param request 현재 HTTP 요청에 대한 정보를 담고 있는 WebRequest 객체입니다.
     * @return HTTP 500 상태 코드와 일반적인 오류 메시지를 담은 ResponseEntity<Object> 객체입니다.
     */
    @ExceptionHandler(RuntimeException.class) // 좀 더 포괄적인 RuntimeException 처리
    public ResponseEntity<Object> handleGenericRuntimeException(RuntimeException ex, WebRequest request) {
        // 심각한 오류이므로 ERROR 레벨로 로깅하고, 스택 트레이스도 함께 기록합니다.
        log.error("Unhandled runtime exception occurred: {}", ex.getMessage(), ex); 
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // HTTP 상태 코드 (500)
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()); // HTTP 상태 메시지 (예: "Internal Server Error")
        // 실제 운영 환경에서는 상세한 내부 오류 메시지를 클라이언트에게 직접 노출하지 않는 것이 보안상 좋습니다.
        // 대신 "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요." 와 같은 일반적인 메시지를 사용합니다.
        body.put("message", "An unexpected internal server error occurred. Please try again later."); 
        // body.put("message", ex.getMessage()); // 개발 중에는 구체적인 메시지 확인을 위해 이 줄을 사용할 수 있습니다.
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}