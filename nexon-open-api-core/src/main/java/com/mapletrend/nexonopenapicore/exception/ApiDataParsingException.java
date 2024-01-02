package com.mapletrend.nexonopenapicore.exception;

public class ApiDataParsingException extends RuntimeException {

    public ApiDataParsingException(String message) {
        super(message);
    }

    public ApiDataParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
