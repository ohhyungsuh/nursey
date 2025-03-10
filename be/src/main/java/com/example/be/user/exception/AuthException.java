package com.example.be.user.exception;

import com.example.be.global.exception.CustomException;

public class AuthException extends CustomException {
    public AuthException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
