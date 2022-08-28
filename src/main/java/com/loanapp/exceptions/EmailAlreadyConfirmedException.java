package com.loanapp.exceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{
    public EmailAlreadyConfirmedException (String message) {
        super(message);
    }
}
