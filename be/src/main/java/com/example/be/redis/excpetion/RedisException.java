package com.example.be.redis.excpetion;

import com.example.be.global.exception.CustomException;

public class RedisException extends CustomException {
    public RedisException(EmailErrorCode errorCode) {
        super(errorCode);
    }
}
