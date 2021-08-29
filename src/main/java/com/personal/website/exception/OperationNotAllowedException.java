package com.personal.website.exception;

public class OperationNotAllowedException extends RuntimeException
{public OperationNotAllowedException(String message)
    {
        super(message);
    }
}
