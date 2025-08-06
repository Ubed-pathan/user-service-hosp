package com.appointment.user_service.Services.components;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OtpDetails {
    private String otp;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    private boolean isVerified;



//    // Getters and Setters
//    public String getOtp() { return otp; }
//    public void setOtp(String otp) { this.otp = otp; }
//
//    public LocalDateTime getGeneratedAt() { return generatedAt; }
//    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
//
//    public LocalDateTime getExpiresAt() { return expiresAt; }
//    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
//
//    public boolean isVerified() { return isVerified; }
//    public void setVerified(boolean verified) { isVerified = verified; }
}

