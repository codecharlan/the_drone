package com.codecharlan.thedrone.exception;

public class RegisterDroneLimitExceededException extends RuntimeException {
    public RegisterDroneLimitExceededException(String s) {
        super(s);
    }
}
