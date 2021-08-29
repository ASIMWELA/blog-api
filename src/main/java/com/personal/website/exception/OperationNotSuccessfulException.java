package com.personal.website.exception;

public class OperationNotSuccessfulException extends RuntimeException{
    public OperationNotSuccessfulException(String message) {
        super(message);
    }
}
