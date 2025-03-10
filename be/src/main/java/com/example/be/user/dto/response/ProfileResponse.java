package com.example.be.user.dto.response;

import com.example.be.user.domain.Gender;
import lombok.Data;

@Data
public class ProfileResponse {
    private String email;
    private String nickname;
    private String imageUrl;
    private Gender gender;
}
