package com.example.be.user.exception;

import com.example.be.global.exception.CustomException;

public class UserException extends CustomException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
