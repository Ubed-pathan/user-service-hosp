package com.appointment.user_service.Services;

import com.appointment.user_service.Services.components.OtpDetails;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, OtpDetails> otpStorage = new HashMap<>();

    private final int OTP_VALIDITY_MINUTES = 10;

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(OTP_VALIDITY_MINUTES);

        OtpDetails otpDetails = otpStorage.get(email);

        if (otpDetails == null || otpDetails.isVerified()) {
            otpDetails = new OtpDetails(otp, now, expiryTime, false);
        } else {
            otpDetails.setOtp(otp);
            otpDetails.setGeneratedAt(now);
            otpDetails.setExpiresAt(expiryTime);
            otpDetails.setVerified(false);
        }

        otpStorage.put(email, otpDetails);
        return otp;
    }

    public boolean verifyOtp(String email, String inputOtp) {
        OtpDetails otpDetails = otpStorage.get(email);

        if (otpDetails == null) {
            return false;
        }

        if (otpDetails.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);
            return false;
        }

        boolean isMatch = otpDetails.getOtp().equals(inputOtp);
        if (isMatch) {
            otpDetails.setVerified(true);
        }

        return isMatch;
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    public boolean isEmailVerified(String email) {
        OtpDetails otpDetails = otpStorage.get(email);
        return otpDetails != null && otpDetails.isVerified();
    }

    public OtpDetails getOtpDetails(String email) {
        return otpStorage.get(email);
    }

    // ✅ Auto-cleanup task: runs every 10 minutes
    @Scheduled(fixedRate = 10 * 60 * 1000) // every 10 minutes
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        Iterator<Map.Entry<String, OtpDetails>> iterator = otpStorage.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, OtpDetails> entry = iterator.next();
            OtpDetails otp = entry.getValue();

            if (!otp.isVerified() || otp.getExpiresAt().isBefore(now)) {
                iterator.remove(); // safe removal during iteration
            }
        }
    }
}
