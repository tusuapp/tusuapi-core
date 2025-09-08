package com.tusuapp.coreapi.config;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SendGridConfig created by Rithik S(coderithik@gmail.com)
 **/
@Configuration
@EnableConfigurationProperties
class SendGridConfig {

    @Value("${TUSU_SENDGRID_API}")
    private String apiKey;

    private final String fromEmail = "verification@tusuapp.com";

    private final String fromName = "Tusu";


    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(apiKey);
    }

    @Bean
    public Email fromEmail() {
        return new Email(fromEmail, fromName);
    }
}