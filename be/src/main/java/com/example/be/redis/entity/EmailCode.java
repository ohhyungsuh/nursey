package com.example.be.redis.entity;

import jakarta.persistence.Transient;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "emailCode")
public class EmailCode {

    @Id
    private String email;

    private String code;

    @TimeToLive
    private Long ttl;

    public EmailCode(String email, String code, Long ttl) {
        this.email = email;
        this.code = code;
        this.ttl = ttl;
    }

}
