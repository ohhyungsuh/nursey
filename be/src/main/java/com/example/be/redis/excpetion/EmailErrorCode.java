package com.example.be.redis.excpetion;

import com.example.be.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {

    INVALID_CODE(HttpStatus.UNAUTHORIZED, "인증 코드가 일치하지 않습니다."),
    EXPIRED_CODE(HttpStatus.UNAUTHORIZED, "만료된 이메일 코드입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
