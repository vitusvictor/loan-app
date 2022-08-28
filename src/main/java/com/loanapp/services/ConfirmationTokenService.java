package com.loanapp.services;

import com.loanapp.validations.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getToken(String token);
    void setConfirmedAt(String token);
}
