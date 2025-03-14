package com.example.be.global.response;

import com.example.be.global.exception.CustomException;
import com.example.be.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Schema(description = "API 응답")
public class ApiResponse<T> {

    @Schema(description = "응답 상태")
    private final Status status;

    @JsonInclude(NON_EMPTY)
    @Schema(description = "응답 메타데이터")
    private Metadata metadata;

    @JsonInclude(NON_EMPTY)
    @Schema(description = "응답 데이터")
    private T result;

    // 응답 코드만 반환
    public ApiResponse(HttpStatus httpStatus) {
        this.status = new Status(httpStatus);
    }

    // 데이터 반환
    public ApiResponse(HttpStatus httpStatus, T result) {
        this.status = new Status(httpStatus);
        this.result = result;

        if (result instanceof List<?>) {
            this.metadata = new Metadata(((List<?>) result).size());
        }
    }

    // 커스텀 에러 처리
    public ApiResponse(CustomException e) {
        this.status = new Status(e.getErrorCode());
    }

    // 커스텀 + @Valid 에러 처리
    public ApiResponse(int code, String message) {
        this.status = new Status(code, message);
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

        // @Valid 에러 코드 처리
        public Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

    }

    @Getter
    @AllArgsConstructor
    private static class Metadata {
        private int resultCount = 0;
    }

}
