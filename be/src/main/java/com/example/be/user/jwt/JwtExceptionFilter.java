package com.example.be.user.jwt;

import com.example.be.global.response.ApiResponse;
import com.example.be.user.exception.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            // AuthException 처리
            log.error("AuthException에 들어옴: {}", e.getErrorCode());
            handleException(response, e.getErrorCode().getHttpStatus(), e.getErrorCode().getMessage());
        } catch (AccessDeniedException e) {
            // AccessDeniedException 처리
            handleException(response, HttpStatus.FORBIDDEN, e.getMessage());
        } catch (AuthenticationException e) {
            // AuthenticationException 처리
            handleException(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            handleException(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private void handleException(HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        ApiResponse<?> apiResponse = new ApiResponse<>(httpStatus.value(), message);

        // 응답 설정
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}
