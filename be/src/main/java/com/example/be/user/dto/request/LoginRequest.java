package com.example.be.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    @Email(message = "올바른 이메일을 입력해주세요.")
    private String email;

    @NotBlank
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^])[a-zA-Z\\d!@#$%^]+$",
            message = "비밀번호는 영어, 숫자, 특수문자(!@#$%^)를 포함해야 합니다."
    )
    private String password;

}
