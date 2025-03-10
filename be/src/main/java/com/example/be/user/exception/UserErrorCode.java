package com.example.be.user.exception;

import com.example.be.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 400 BAD REQUEST: 잘못된 요청
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 401 UNAUTHORIZED: 인증 실패
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료됐습니다."),

    // 403 FORBIDDEN: 권한 없음
    UNAUTHORIZED_ROLE(HttpStatus.FORBIDDEN, "작업을 수행할 권한이 없습니다."),
    BLOCKED_USER(HttpStatus.FORBIDDEN, "차단된 사용자입니다."),

    // 404 NOT FOUND: 존재하지 않는 리소스
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // 409 CONFLICT: 중복된 정보
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 가입된 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
