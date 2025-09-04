package com.appointment.user_service.Controllers;

import com.appointment.user_service.Dtos.UserDto;
import com.appointment.user_service.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@AllArgsConstructor
public class UserFunctionsController {

    private final UserService userServices;

    @GetMapping("/loadOnRefresh")
    public ResponseEntity<?> loadOnRefresh(@RequestHeader("X-User-Id") String userId) {
        System.out.println("User ID from header: " + userId);
        if (userId.isEmpty()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        UUID uuid = UUID.fromString(userId);
        UserDto userDto = userServices.getUserDetails(uuid);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userServices.getAllUsers());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try{
            UUID uuid = UUID.fromString(id);
            userServices.deleteUser(uuid);
            return ResponseEntity.ok("User deleted successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Invalid User ID");
        }
    }

}
