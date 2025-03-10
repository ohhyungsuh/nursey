package com.example.be.user.controller;

import com.example.be.global.vo.BaseResponse;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.ProfileResponse;
import com.example.be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<String>> signup(@RequestBody SignupRequest signupRequest) {

        userService.signup(signupRequest);

        return ResponseEntity
                .status(CREATED)
                .body(new BaseResponse<>(CREATED));
    }

    // 로그아웃


    // 유저 정보 조회
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<ProfileResponse>> getMyProfile(Principal principal) {

        ProfileResponse profile = userService.getMyProfile(principal.getName());

        return ResponseEntity
                .status(OK)
                .body(new BaseResponse<>(OK, profile));
    }


}
