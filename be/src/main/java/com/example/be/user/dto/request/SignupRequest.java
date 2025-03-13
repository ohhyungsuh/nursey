package com.example.be.user.dto.request;

import com.example.be.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SignupRequest {

    @NotBlank
    @Email(message = "올바른 이메일을 입력해주세요.")
    private String email;

    @NotBlank
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^])[a-zA-Z\\d!@#$%^]+$",
            message = "비밀번호는 영어, 숫자, 특수문자(!@#$%^)를 포함해야 합니다."
    )
    @Schema(example = "string")
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해야 합니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]+$",
            message = "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다."
    )
    @Schema(example = "string")
    private String nickname;

    @NotNull
    @Schema(example = "string")
    private Gender gender;
}