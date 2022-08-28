package com.loanapp.exceptions;

public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(String message) {
        super(message);
    }
}
