package com.example.be.global.exception;

import com.example.be.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> customExceptionHandler(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ApiResponse<>(e));
    }

    // 잘못된 HTTP 메소드 요청
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(GlobalErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(new ApiResponse<>(GlobalErrorCode.METHOD_NOT_ALLOWED.getHttpStatus()));
    }

    // @Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ApiResponse<>(e.getStatusCode().value(), defaultMessage));
    }

}
