package com.example.be.user.jwt;

import com.example.be.user.exception.AuthException;
import com.example.be.user.exception.UserErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final int AUTHORIZATION_HEADER_INDEX = 7;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(AUTHORIZATION_HEADER_INDEX);

        validate(response, accessToken);

        setAuthentication(accessToken);

        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletResponse response, String accessToken) {
        if (!jwtProvider.validateToken(accessToken)) {
            throw new AuthException(UserErrorCode.EXPIRED_TOKEN);
        }
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
