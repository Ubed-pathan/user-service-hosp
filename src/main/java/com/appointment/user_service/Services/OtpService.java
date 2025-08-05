package com.appointment.user_service.Services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    private final Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);
        return otp;
    }

    public boolean verifyOtp(String email, String inputOtp) {
        String savedOtp = otpStorage.get(email);
        return savedOtp != null && savedOtp.equals(inputOtp);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}
