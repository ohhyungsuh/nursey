package com.example.be.redis.repository;

import com.example.be.redis.entity.EmailCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailCodeRepository extends CrudRepository<EmailCode, String> {
    Optional<EmailCode> findByEmail(String email);
}