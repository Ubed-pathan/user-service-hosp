package com.appointment.user_service.Controllers;

import com.appointment.user_service.Dtos.LoginDto;
import com.appointment.user_service.Dtos.UserRegistrationDto;
import com.appointment.user_service.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationDto userDto) {
        try {
            userService.register(userDto); // Service does logic, throws error if needed
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


    @GetMapping("/exists/{userId}")
    public ResponseEntity<?> isUserExist(@PathVariable String userId) {
        try {
            boolean exists = userService.isUserExists(userId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong: " + e.getMessage());
        }
    }
}

