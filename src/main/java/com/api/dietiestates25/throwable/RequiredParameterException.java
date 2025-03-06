package com.api.dietiestates25.throwable;

public class RequiredParameterException extends RuntimeException {
    public RequiredParameterException(String message) {
        super(message);
    }
}
