package com.example.demo.Booking.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus; 


@Getter
@NoArgsConstructor 
public class ApiResponseDto<T> {

    private boolean success;    
    private T data;             
    private String message;     
    private ErrorDetails error; 

    
    public ApiResponseDto(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.error = null; 
    }

    
    public ApiResponseDto(boolean success, String message, ErrorDetails error) {
        this.success = success;
        this.data = null; 
        this.message = message;
        this.error = error;
    }

    
    public static <T> ApiResponseDto<T> success(T data, String message) {
        return new ApiResponseDto<>(true, data, message);
    }

    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(true, data, "요청에 성공했습니다."); 
    }

    
    public static <T> ApiResponseDto<T> error(String message, String errorCode, HttpStatus httpStatus) {
        return new ApiResponseDto<>(false, message, new ErrorDetails(errorCode, httpStatus.value()));
    }

    public static <T> ApiResponseDto<T> error(String message, HttpStatus httpStatus) {
        return new ApiResponseDto<>(false, message, new ErrorDetails(String.valueOf(httpStatus.value()), httpStatus.value()));
    }


    
    @Getter
    private static class ErrorDetails {
        private String code;
		private int status;     

        public ErrorDetails(String code, int status) {
            this.code = code;
            this.status = status;
        }
    }
}