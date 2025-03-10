package com.example.be.user.repository;

import com.example.be.global.util.CustomUUIDGenerator;
import com.example.be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(byte[] userId);

    default Optional<User> findByUserId(String userId) {
        return findByUserId(CustomUUIDGenerator.toBytes(UUID.fromString(userId)));
    }

}