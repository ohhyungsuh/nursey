package com.example.be.user.service;

import com.example.be.user.exception.AuthException;
import com.example.be.user.exception.UserErrorCode;
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

    private String generateEmailCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    public String sendVerificationEmail(String email) throws MessagingException {
        try {
            String code = generateEmailCode();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@nursey.com", "Nursey");
            helper.setTo(email);
            helper.setSubject("[서비스 이름] 이메일 인증 코드 안내");
            helper.setText(
                    "<h2>[서비스 이름] 이메일 인증 코드</h2>" +
                            "<p>안녕하세요,</p>" +
                            "<p>요청하신 인증 코드는 다음과 같습니다:</p>" +
                            "<h3 style='color:blue;'>" + code + "</h3>" +
                            "<p>이 코드는 5분 후 만료됩니다.</p>" +
                            "<p>감사합니다.</p>",
                    true
            );

            mailSender.send(message);
            return code;

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("이메일 전송에 실패했습니다. {}", e.getMessage());
            throw new AuthException(UserErrorCode.EMAIL_SEND_FAILED);
        }
    }

}
