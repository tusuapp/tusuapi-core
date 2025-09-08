package com.tusuapp.coreapi.services.notifications;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testEmailSend() throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail("tubeviral88@gmail.com", "OTP", "hello");
    }

}