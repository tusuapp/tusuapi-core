package com.tusuapp.coreapi.services.notifications;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SMSService created by Rithik S (coderithik@gmail.com)
 **/
@Service
public class SMSService {

    @Value("${twilio.accountSid}")
    private String ACCOUNT_SID;

    @Value("${twilio.authToken}")
    private String AUTH_TOKEN;

    @Value("${twilio.fromNumber}")
    private String FROM_NUMBER;

    public void sendPhoneOtp(String to, String otp) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String messageBody = String.format(
                    "Your Tusu verification code is: %s. Do not share this code with anyone. It expires in 10 minutes.",
                    otp
            );
            if(!to.startsWith("+")){
                to = "+" + to;
            }
            Message message = Message.creator(
                    new PhoneNumber(to),         // To phone number
                    new PhoneNumber(FROM_NUMBER), // From Twilio number
                    messageBody
            ).create();
            System.out.println("OTP sent successfully to " + to + ". SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send OTP to " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
