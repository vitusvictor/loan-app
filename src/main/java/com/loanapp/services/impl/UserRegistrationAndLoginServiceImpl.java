package com.loanapp.services.impl;

import com.loanapp.dtos.LoginDto;
import com.loanapp.dtos.SendMailDto;
import com.loanapp.dtos.UserRegistrationDto;
import com.loanapp.entities.User;
import com.loanapp.entities.Wallet;
import com.loanapp.enums.UserStatus;
import com.loanapp.exceptions.EmailAlreadyConfirmedException;
import com.loanapp.exceptions.TokenNotFoundException;
import com.loanapp.exceptions.UserNotFoundException;
import com.loanapp.repositories.ConfirmationTokenRepo;
import com.loanapp.repositories.UserRepo;
import com.loanapp.services.ConfirmationTokenService;
import com.loanapp.services.MailService;
import com.loanapp.services.UserRegistrationAndLoginService;
import com.loanapp.services.WalletService;
import com.loanapp.util.Constant;
import com.loanapp.validations.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;


@Service @RequiredArgsConstructor @Slf4j
public class UserRegistrationAndLoginServiceImpl implements UserRegistrationAndLoginService {
    private final UserRepo userRepository;
    private final MailService mailService;
    private final ConfirmationTokenRepo confirmationTokenRepository;
    private final ConfirmationTokenService confirmTokenService;
    private final WalletService walletService;
    private final HttpSession httpSession;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String registerUser(UserRegistrationDto userRegistrationDto) {
        User user = User.builder()
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .email(userRegistrationDto.getEmail())
                .pin(bCryptPasswordEncoder.encode(userRegistrationDto.getPin()))
                .password(userRegistrationDto.getPassword())
                .phoneNumber(userRegistrationDto.getPhoneNumber())
                .userStatus(UserStatus.INACTIVE)
                .previousOperationsCount(0L)
                .qualified(true)
                .build();

        Wallet wallet = walletService.createWallet(user);
        user.setWallet(wallet);

        String token = UUID.randomUUID().toString();
        saveToken(token, user);

        userRepository.save(user);

        String link = Constant.EMAIL_VERIFICATION_LINK + token;
        sendMailVerificationLink(userRegistrationDto.getFirstName(), userRegistrationDto.getEmail(), link);

        return "Please check your email for account activation link.";
    }

    @Override
    public void saveToken(String token, User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                user
        );
        confirmTokenService.saveConfirmationToken(confirmationToken);
    }

    @Override
    public Wallet generateWallet(User user) {
        return walletService.createWallet(user);
    }

    @Override
    public void enableUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->  new UserNotFoundException("Users not found."));
        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void deleteUnverifiedToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    @Override
    public void sendMailVerificationLink(String name, String email, String link) {
        String subject = "Email Verification";
        String body = "Click the link below to verify your email \n" + link;
        SendMailDto sendMailDto = new SendMailDto(email, name, subject, body);
        mailService.sendMail(sendMailDto);
    }

    @Override
    public void resendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        String link = Constant.EMAIL_VERIFICATION_LINK + token;
        sendMailVerificationLink(user.getFirstName(), user.getEmail(), link);

        saveToken(token, user);
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmTokenService.getToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found."));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException("Email already confirmed.");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            User user = userRepository.findByEmail(confirmationToken.getUser().getEmail()).orElseThrow(
                    ()-> new UserNotFoundException("Users not found"));

            deleteUnverifiedToken(confirmationToken);

            resendVerificationEmail(user);
            return "Previous verification token expired. Check email for new token.";
        }

        confirmTokenService.setConfirmedAt(token);
        enableUser(confirmationToken.getUser().getEmail());

        return "Email confirmed!";
    }

    @Override
    public String login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).
                orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        if (user.getUserStatus() != UserStatus.ACTIVE){
            ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByUser(user)
                            .orElseThrow(()-> new TokenNotFoundException("Token not found"));
            if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) { // already expired
                deleteUnverifiedToken(confirmationToken);
                resendVerificationEmail(user);
            }

            return "Please verify your account from your email";
        }

        if (user.getPassword().equals(bCryptPasswordEncoder.encode(loginDto.getPassword()))) {
            System.out.println(user.getPassword());
            System.out.println(loginDto.getPassword());

            httpSession.setAttribute("user_id", user.getId());
            return "Login successful";
        }

        return "Bad credentials";
    }
}
