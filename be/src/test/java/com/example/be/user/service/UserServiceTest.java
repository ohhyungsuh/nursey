package com.example.be.user.service;

import com.example.be.user.domain.Gender;
import com.example.be.user.domain.User;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.ProfileResponse;
import com.example.be.user.exception.UserException;
import com.example.be.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Spy
    private ModelMapper modelMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("test@gmail.com", "1q2w3e4r!!", "test", Gender.MALE);
    }

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
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        // given
        SignupRequest signupRequest = createSignupRequest("test@gmail.com", "1q2w3e4r!!", "1q2w3e4r!!");

        // PasswordEncoder mock 설정
        when(passwordEncoder.encode("1q2w3e4r!!")).thenReturn("encodedPassword");

        // UserRepository mock 설정
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testUser));

        // when
        userService.signup(signupRequest);

        // then
        Optional<User> findUser = userRepository.findByEmail("test@gmail.com");

        assertTrue(findUser.isPresent());
        assertThat(findUser.get().getEmail()).isEqualTo("test@gmail.com");
        assertThat(findUser.get().getNickname()).isEqualTo("test");
    }

    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    @Test
    void 비밀번호_불일치() {
        // given
        SignupRequest signupRequest = createSignupRequest("test@gmail.com", "1q2w3e4r!!", "1q2w3e4r!");

        // when & then
        assertThrows(UserException.class, () -> userService.signup(signupRequest));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void 이메일_중복() {
        // given
        SignupRequest signupRequest = createSignupRequest("test@gmail.com", "1q2w3e4r!!", "1q2w3e4r!!");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        // when & then
        assertThrows(UserException.class, () -> userService.signup(signupRequest));
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void 닉네임_중복() {
        // given
        SignupRequest signupRequest = createSignupRequest("test@gmail.com", "1q2w3e4r!!", "1q2w3e4r!!");

        when(userRepository.existsByNickname("test")).thenReturn(true);

        // when & then
        assertThrows(UserException.class, () -> userService.signup(signupRequest));
    }

    @Test
    @DisplayName("프로필 조회")
    void 프로필_조회() {
        // given
        String userId = testUser.getUserIdAsString();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(testUser));

        // when
        ProfileResponse profileResponse = userService.getMyProfile(userId);

        // then
        assertThat(profileResponse).isNotNull();
        assertThat(profileResponse.getEmail()).isEqualTo("test@gmail.com");
        assertThat(profileResponse.getNickname()).isEqualTo("test");
    }
}