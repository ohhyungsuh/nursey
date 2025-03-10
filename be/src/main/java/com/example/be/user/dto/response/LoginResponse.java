package com.example.be.user.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String userId;
    private String nickname;
    private String imageUrl;

    public LoginResponse(String userId, String nickname, String imageUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

}
