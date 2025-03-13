package com.example.be.user.controller;

import com.example.be.global.response.ApiResponse;
import com.example.be.user.dto.request.EmailCodeRequest;
import com.example.be.user.dto.request.EmailVerifyRequest;
import com.example.be.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/email/verify")
    @Operation(summary = "이메일 인증 코드 발송", description = "이메일로 인증 코드를 발송합니다.")
    public ApiResponse<String> sendEmailCode(@Valid @RequestBody EmailCodeRequest emailRequest) throws MessagingException {
        authService.sendEmailCode(emailRequest.getEmail());

        return new ApiResponse<>(OK, "인증 코드가 발송되었습니다.");
    }

    @PostMapping("/email/confirm")
    @Operation(summary = "이메일 인증 코드 확인", description = "이메일로 발송된 인증 코드를 확인합니다.")
    public ApiResponse<String> verifyEmailCode(@Valid @RequestBody EmailVerifyRequest verifyRequest) {
        authService.verifyEmailCode(verifyRequest);

        return new ApiResponse<>(OK, "인증이 완료되었습니다.");
    }
}
