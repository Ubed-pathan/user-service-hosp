package com.appointment.user_service.Controllers;

import com.appointment.user_service.Dtos.*;
import com.appointment.user_service.Repositories.UserRepository;
import com.appointment.user_service.Services.EmailService;
import com.appointment.user_service.Services.OtpService;
import com.appointment.user_service.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    private final Set<String> verifiedEmails = new HashSet<>();

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody EmailForOtpDto req) throws IOException {
        String email = req.email();

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
        return ResponseEntity.ok("OTP sent to email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpDto req) {
        String email = req.email();
        String otp = req.otp();

        if (otpService.verifyOtp(email, otp)) {
            verifiedEmails.add(email);
//            otpService.clearOtp(email);
            return ResponseEntity.ok("OTP verified.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationDto userDto) {
        String email = userDto.email();

        if (!otpService.isEmailVerified(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not verified. Please verify OTP before registering.");
        }

        try {
            userService.register(userDto);
            otpService.clearOtp(email); // Optional: clear after successful registration
            return ResponseEntity.ok("User registered successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto userDto,
                                       HttpServletResponse response) {
        try {
            String jwtToken = userService.login(userDto);
            // Set token in HTTP-only cookie
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(true)
                    .secure(true) // Set to true in production with HTTPS
                    .path("/")
                    .maxAge(3600 * 24 * 30)
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            return ResponseEntity.ok("Login successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("error"+e.getMessage());
        }
    }


    @PostMapping("/isValid")
    public ResponseEntity<?> isUserValid(@Valid @RequestBody UserVerificationDto dto) {
        try {
            UserVerificationDto exists = userService.isUserValid(dto);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong: " + e.getMessage());
        }
    }
}

