package com.example.be.global.response;

import com.example.be.global.exception.CustomException;
import com.example.be.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
public class ApiResponse<T> {

    private final Status status;

    @JsonInclude(NON_EMPTY)
    private Metadata metadata;

    @JsonInclude(NON_EMPTY)
    private Object result;

    // 응답 코드만 반환
    public ApiResponse(HttpStatus httpStatus) {
        this.status = new Status(httpStatus);
    }

    // 단일 데이터 반환
    public ApiResponse(HttpStatus httpStatus, T result) {
        this.status = new Status(httpStatus);
        this.result = result;
    }

    // 다중 데이터 반환
    public ApiResponse(HttpStatus httpStatus, List<T> result) {
        this.status = new Status(httpStatus);
        this.metadata = new Metadata(result.size());
        this.result = result;
    }

    // 커스텀 에러 처리
    public ApiResponse(CustomException e) {
        this.status = new Status(e.getErrorCode());
    }

    @Getter
    static class Status {
        private final int code;
        private final String message;

        // 커스텀 에러 코드 처리
        public Status(ErrorCode errorCode) {
            this.code = errorCode.getHttpStatus().value();
            this.message = errorCode.getMessage();
        }

        // 일반 응답 반환(OK, CREATED, ACCEPTED 등)
        public Status(HttpStatus httpStatus) {
            this.code = httpStatus.value();
            this.message = httpStatus.getReasonPhrase();
        }

    }

    @Getter
    @AllArgsConstructor
    private static class Metadata {
        private int resultCount = 0;
    }

}
