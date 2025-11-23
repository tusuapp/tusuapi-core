package com.tusuapp.coreapi.services.notifications;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * EmailService created by Rithik S(coderithik@gmail.com)
 **/
@Service
public class EmailService {


    public static final String SGKEY = "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";

    @Autowired
    private SendGrid sendGrid;

    @Autowired
    private Email fromEmail;

    @Value("${TUSU_GODADDY_SMTP_PASSWORD}")
    private String smtpPassword;

    public void sendVerificationEmail(String to, String verificationLink) {
        String body = """
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; color: #333; margin: 0; padding: 0; background-color: #f8f9fa; }
                        .container { max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 30px; }
                        h2 { color: #4CAF50; text-align: center; }
                        p { line-height: 1.6; }
                        .verify-button { display: block; width: fit-content; margin: 25px auto; padding: 12px 24px; background-color: #4CAF50; color: #fff; text-decoration: none; border-radius: 6px; font-weight: bold; }
                        .verify-button:hover { background-color: #43a047; }
                        .footer { font-size: 12px; color: #999; text-align: center; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <h2>Verify Your Email – Tusu App</h2>
                        <p>Hi there,</p>
                        <p>Thank you for registering with <strong>Tusu App</strong>! Please verify your email address by clicking the button below.</p>
                        <a href='%s' class='verify-button'>Verify Email</a>
                        <p>If you didn’t create a Tusu account, you can safely ignore this email.</p>
                        <div class='footer'>
                            <p>&copy; 2025 Tusu App. All rights reserved.</p>
                            <p>This is an automated message, please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(verificationLink);

        try {
            sendEmail(to, "Verify Your Email - Tusu App", body, null);
            System.out.println("Verification email sent to: " + to);
        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
            System.out.println("Error sending verification email: " + ex.getMessage());
        }
    }

    public void sendForgotPasswordEmail(String to, String resetLink) {
        String body = """
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; color: #333; margin: 0; padding: 0; background-color: #f8f9fa; }
                        .container { max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 30px; }
                        h2 { color: #4CAF50; text-align: center; }
                        p { line-height: 1.6; }
                        .reset-button { display: block; width: fit-content; margin: 25px auto; padding: 12px 24px; background-color: #4CAF50; color: #fff; text-decoration: none; border-radius: 6px; font-weight: bold; }
                        .reset-button:hover { background-color: #43a047; }
                        .footer { font-size: 12px; color: #999; text-align: center; margin-top: 20px; }
                        .warning { font-size: 13px; color: #666; font-style: italic; }
                    </style>
                </head>
                <body>
                    <div class='container'>
                        <h2>Reset Your Password</h2>
                        <p>Hi there,</p>
                        <p>We received a request to reset the password for your <strong>Tusu App</strong> account. No changes have been made to your account yet.</p>
                        <p>You can reset your password by clicking the button below:</p>
                        <a href='%s' class='reset-button'>Reset Password</a>
                        <p class='warning'>If you did not request a password reset, you can safely ignore this email. Your password will remain the same.</p>
                        <div class='footer'>
                            <p>&copy; 2025 Tusu App. All rights reserved.</p>
                            <p>This is an automated message, please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(resetLink);

        try {
            sendEmail(to, "Reset Your Password - Tusu App", body, null);
            System.out.println("Forgot password email sent to: " + to);
        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
            System.out.println("Error sending forgot password email: " + ex.getMessage());
        }
    }


    @Async
    public void sendEmail(String to, String subject, String text, File attachment) throws MessagingException, UnsupportedEncodingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.office365.com");
        mailSender.setPort(587);
        mailSender.setUsername("bilal@tusuapp.com");
        mailSender.setPassword(smtpPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.office365.com");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set FROM with display name
        helper.setFrom("bilal@tusuapp.com", "Tusuapp Verification OTP");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        if (attachment != null) {
            helper.addAttachment(attachment.getName(), attachment);
        }
        mailSender.send(message);
        if (attachment != null) {
            attachment.delete();
        }
    }

}
