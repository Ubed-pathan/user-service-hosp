package com.appointment.user_service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify Your Email - OTP Inside");

        String emailBody = """
                Hello,

                Thank you for signing up! To complete your registration, please use the following One-Time Password (OTP):

                🔐 Your OTP: %s

                This OTP is valid for the next 10 minutes. Do not share this code with anyone.

                If you didn’t request this, please ignore this email.

                Best regards,
                Team Nexore
                """.formatted(otp);

        message.setText(emailBody);
        mailSender.send(message);
    }
}
