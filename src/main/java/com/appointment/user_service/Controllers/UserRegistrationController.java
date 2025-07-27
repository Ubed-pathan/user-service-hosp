package com.appointment.user_service.Controllers;

import com.appointment.user_service.Dtos.UserRegistrationDto;
import com.appointment.user_service.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

