package com.example.be.redis.service;

import com.example.be.redis.entity.EmailCode;
import com.example.be.redis.excpetion.EmailErrorCode;
import com.example.be.redis.excpetion.RedisException;
import com.example.be.redis.repository.EmailCodeRepository;
import com.example.be.user.dto.request.EmailVerifyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCodeService {

    private final EmailCodeRepository emailCodeRepository;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private Long expirationMillis;


    // 인증 코드 생성 및 저장
    public void saveEmailCode(String email, String code) {
        EmailCode emailCode = new EmailCode(email, code, expirationMillis / 1000);

        emailCodeRepository.save(emailCode);
    }

    // 인증 코드 검증
    public void verifyEmailCode(EmailVerifyRequest verifyRequest) {
        EmailCode verification = emailCodeRepository.findByEmail(verifyRequest.getEmail())
                .orElseThrow(() -> new RedisException(EmailErrorCode.EXPIRED_CODE));

        if (verification.getCode().equals(verifyRequest.getCode())) {
            emailCodeRepository.deleteById(verifyRequest.getEmail()); // 인증 성공 후 삭제
            return;
        }

        throw new RedisException(EmailErrorCode.INVALID_CODE);
    }

}
