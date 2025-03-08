package com.example.be.global.exception;

import com.example.be.global.vo.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<?>> customExceptionHandler(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new BaseResponse<>(e));
    }

    // 잘못된 HTTP 메소드 요청
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(GlobalErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(new BaseResponse<>(GlobalErrorCode.METHOD_NOT_ALLOWED.getHttpStatus()));
    }

}
