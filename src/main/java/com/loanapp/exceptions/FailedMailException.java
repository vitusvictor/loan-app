package com.loanapp.exceptions;

public class FailedMailException extends RuntimeException{

    public FailedMailException(String message) {
        super(message);
    }
}

