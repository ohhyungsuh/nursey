package com.example.be.user.filter;

import com.example.be.global.response.ApiResponse;
import com.example.be.user.domain.User;
import com.example.be.user.dto.PrincipalUserDetails;
import com.example.be.user.dto.request.LoginRequest;
import com.example.be.user.dto.response.LoginResponse;
import com.example.be.user.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public CustomLoginFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            log.info("loginRequest: {}", loginRequest);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), new ArrayList<>());

            return getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            log.error("ObjectMapper IO 에러: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalUserDetails principal = (PrincipalUserDetails) authResult.getPrincipal();
        User user = principal.getUser();

        String accessToken = jwtProvider.generateAccessToken(authResult, user.getUserIdAsString());

        LoginResponse loginResponse = new LoginResponse(user.getUserIdAsString(), user.getEmail(), user.getImageUrl());

        ApiResponse<LoginResponse> baseResponse = new ApiResponse<>(OK, loginResponse);

        // ResponseEntity 반환
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader("Authorization", "Bearer " + accessToken);

        new ObjectMapper().writeValue(response.getWriter(), baseResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ApiResponse<?> apiResponse = new ApiResponse<>(UNAUTHORIZED.value(), "이메일 또는 비밀번호가 일치하지 않습니다.");

        // 응답 설정
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
