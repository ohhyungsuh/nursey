package com.example.be.user.controller;

import com.example.be.global.response.ApiResponse;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.dto.response.ProfileResponse;
import com.example.be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    public ApiResponse<String> signup(@Valid @RequestBody SignupRequest signupRequest) {

        userService.signup(signupRequest);

        return new ApiResponse<>(CREATED, "회원가입이 완료됐습니다.");
    }

    // 로그아웃


    // 유저 정보 조회
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    public ApiResponse<ProfileResponse> getMyProfile(Principal principal) {

        ProfileResponse profile = userService.getMyProfile(principal.getName());

        return new ApiResponse<>(OK, profile);
    }

}
