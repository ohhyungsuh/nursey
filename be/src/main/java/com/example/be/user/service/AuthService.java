package com.example.be.user.service;

import com.example.be.redis.service.EmailCodeService;
import com.example.be.user.dto.request.EmailVerifyRequest;
import com.example.be.user.exception.AuthException;
import com.example.be.user.exception.UserErrorCode;
import com.example.be.user.exception.UserException;
import com.example.be.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final EmailCodeService emailCodeService;

    private String generateEmailCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    public void sendEmailCode(String email) throws MessagingException {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        try {
            String code = generateEmailCode();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@nursey.com", "Nursey");
            helper.setTo(email);
            helper.setSubject("[Nursey] 이메일 인증 코드 안내");
            helper.setText(
                    "<h2>[Nursey] 이메일 인증 코드</h2>" +
                            "<p>안녕하세요,</p>" +
                            "<p>요청하신 인증 코드는 다음과 같습니다:</p>" +
                            "<h3 style='color:blue;'>" + code + "</h3>" +
                            "<p>이 코드는 5분 후 만료됩니다.</p>" +
                            "<p>감사합니다.</p>",
                    true
            );

            mailSender.send(message);

            emailCodeService.saveEmailCode(email, code);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("이메일 전송에 실패했습니다. {}", e.getMessage());
            throw new AuthException(UserErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public void verifyEmailCode(EmailVerifyRequest verifyRequest) {
        emailCodeService.verifyEmailCode(verifyRequest);
    }
}
