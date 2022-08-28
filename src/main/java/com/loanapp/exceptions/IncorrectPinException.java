package com.loanapp.exceptions;

public class IncorrectPinException extends RuntimeException{

    public IncorrectPinException(String message) {
        super(message);
    }
}
