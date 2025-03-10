package com.example.be.user.controller;

import com.example.be.global.vo.BaseResponse;
import com.example.be.redis.service.EmailCodeService;
import com.example.be.user.dto.request.EmailRequest;
import com.example.be.user.dto.request.EmailVerifyRequest;
import com.example.be.user.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final EmailCodeService emailVerificationService;
    private final AuthService emailService;

    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<String>> verifyEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        String code = emailService.sendVerificationEmail(emailRequest.getEmail());

        emailVerificationService.saveEmailCode(emailRequest.getEmail(), code);

        return ResponseEntity
                .status(OK)
                .body(new BaseResponse<>(OK, "인증 코드가 발송되었습니다."));

    }

    @PostMapping("/email/confirm")
    public ResponseEntity<BaseResponse<String>> confirmEmail(@RequestBody EmailVerifyRequest verifyRequest) {

        emailVerificationService.verifyEmailCode(verifyRequest);

        return ResponseEntity
                .status(OK)
                .body(new BaseResponse<>(OK, "인증이 완료되었습니다."));
    }
}
