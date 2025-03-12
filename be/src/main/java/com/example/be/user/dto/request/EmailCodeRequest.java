package com.example.be.user.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailVerifyRequest {
    @Email(message = "올바른 이메일을 입력해주세요.")
    private String email;
}
