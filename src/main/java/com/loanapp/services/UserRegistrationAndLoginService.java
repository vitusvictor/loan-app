package com.loanapp.services;

import com.loanapp.dtos.LoginDto;
import com.loanapp.dtos.UserRegistrationDto;
import com.loanapp.entities.User;
import com.loanapp.entities.Wallet;
import com.loanapp.validations.ConfirmationToken;

public interface UserRegistrationAndLoginService {
    String registerUser(UserRegistrationDto userRegistrationDto);

    void saveToken(String token, User user);

    Wallet generateWallet(User user);

    void enableUser(String email);

    void deleteUnverifiedToken(ConfirmationToken token);

    void sendMailVerificationLink(String name, String email, String link);

    void resendVerificationEmail(User user);

    String confirmToken(String token);

    String login(LoginDto loginDto);

}
