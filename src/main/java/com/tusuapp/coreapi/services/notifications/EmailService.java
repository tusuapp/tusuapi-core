package com.tusuapp.coreapi.services.notifications;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
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



    @Autowired
    private SendGrid sendGrid;

    @Autowired
    private Email fromEmail;

    public void sendEmail(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        Email toEmail = new Email(to);
        Content content = new Content("text/html", body); // use "text/plain" if no HTML
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            System.out.println("Email sent! Status Code: " + response.getStatusCode());
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error sending email: " + ex.getMessage());
        }
    }


}
