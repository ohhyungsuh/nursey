package com.example.be.user.service;

import com.example.be.user.domain.User;
import com.example.be.user.dto.request.SignupRequest;
import com.example.be.user.dto.response.ProfileResponse;
import com.example.be.user.exception.UserErrorCode;
import com.example.be.user.exception.UserException;
import com.example.be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        checkIsValidSignup(signupRequest);

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .email(signupRequest.getEmail())
                .nickname(signupRequest.getNickname())
                .gender(signupRequest.getGender())
                .build();

        userRepository.save(user);
    }

    private void checkIsValidSignup(SignupRequest signupRequest) {
        if(!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new UserException(UserErrorCode.MISMATCH_PASSWORD);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        if (userRepository.existsByNickname(signupRequest.getNickname())) {
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }
    }

    public ProfileResponse getMyProfile(String userId) {
        log.info("ProfileResponse getMyProfile: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXIST_USER));

        return modelMapper.map(user, ProfileResponse.class);
    }
}
