package com.appointment.user_service.Services;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
//import jakarta.validation.constraints.Email;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void sendOtp(String toEmail, String otp) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        String subject = "Verify Your Email - OTP Inside";

        String emailBody = String.format("""
                Hello,

                Thank you for signing up! To complete your registration, please use the following One-Time Password (OTP):

                🔐 Your OTP: %s

                This OTP is valid for the next 10 minutes. Do not share this code with anyone.

                If you didn’t request this, please ignore this email.

                Best regards,
                Team Nexore
                """, otp);

        Content content = new Content("text/plain", emailBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());

        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
