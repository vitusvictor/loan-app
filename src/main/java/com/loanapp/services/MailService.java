package com.loanapp.services;

import com.loanapp.dtos.SendMailDto;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    String sendMail(SendMailDto sendMailDto) throws MailException;
}

