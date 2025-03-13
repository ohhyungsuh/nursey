package com.example.be.user.service;

import com.example.be.user.domain.Gender;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.exception.UserException;
import com.example.be.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    private SignupRequest createSignupRequest(String email, String password, String confirmPassword) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        signupRequest.setConfirmPassword(confirmPassword);
        signupRequest.setNickname("test");
        signupRequest.setGender(Gender.MALE);
        return signupRequest;
    }

    @Test
    void 이메일_중복_테스트() {
        // given
        SignupRequest signupRequest = createSignupRequest("test@gmail.com", "1q2w3e4r!!", "1q2w3e4r!!");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        // when & then
        assertThrows(UserException.class, () -> authService.sendEmailCode(signupRequest.getEmail()));
    }
}