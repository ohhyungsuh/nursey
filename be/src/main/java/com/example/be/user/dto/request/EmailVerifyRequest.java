package com.example.be.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailVerifyRequest {

    @Email(message = "올바른 이메일을 입력해주세요.")
    private String email;

    @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
    private String code;
}
