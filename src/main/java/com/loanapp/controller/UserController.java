package com.loanapp.controller;

import com.loanapp.dtos.LoginDto;
import com.loanapp.dtos.UserRegistrationDto;
import com.loanapp.services.UserRegistrationAndLoginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserRegistrationAndLoginService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.registerUser(userRegistrationDto);
    }

    @GetMapping("/confirm-token")
    public String confirmToken(@RequestParam String token) {
        return userService.confirmToken(token);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }
}
