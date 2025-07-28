package com.appointment.user_service.Config;

import com.appointment.user_service.Entities.UserRegistrationEntity;
import com.appointment.user_service.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminUserIfNotExists() {
        String username = System.getenv("ADMIN_USERNAME");
        String email = System.getenv("ADMIN_EMAIL");
        String password = System.getenv("ADMIN_PASSWORD");

        if (username == null || email == null || password == null) {
            throw new RuntimeException("❌ Admin credentials not set in environment variables");
        }

        Optional<UserRegistrationEntity> existingUserOpt = Optional.ofNullable(userRepository.findByUsername(username));

        if (existingUserOpt.isPresent()) {
            UserRegistrationEntity user = existingUserOpt.get();
            if (user.getRoles().contains(UserRegistrationEntity.Role.ADMIN)) {
                System.out.println("ℹ Admin user already exists. Skipping creation.");
                return;
            } else {
                user.getRoles().add(UserRegistrationEntity.Role.ADMIN);
                userRepository.save(user);
                System.out.println("✔ Existing user promoted to ADMIN.");
                return;
            }
        }

        // Create new admin user
        UserRegistrationEntity admin = new UserRegistrationEntity();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFirstName(System.getenv().getOrDefault("ADMIN_FIRST_NAME", "Admin"));
        admin.setMiddleName(System.getenv().getOrDefault("ADMIN_MIDDLE_NAME", ""));
        admin.setLastName(System.getenv().getOrDefault("ADMIN_LAST_NAME", "User"));
        admin.setAge(Integer.parseInt(System.getenv().getOrDefault("ADMIN_AGE", "30")));
        admin.setPhoneNumber(System.getenv().getOrDefault("ADMIN_PHONE", "1234567890"));
        admin.setAddress(System.getenv().getOrDefault("ADMIN_ADDRESS", "Admin Address"));
        admin.setCity(System.getenv().getOrDefault("ADMIN_CITY", "Admin City"));
        admin.setState(System.getenv().getOrDefault("ADMIN_STATE", "Admin State"));
        admin.setCountry(System.getenv().getOrDefault("ADMIN_COUNTRY", "Admin Country"));
        admin.setZipCode(System.getenv().getOrDefault("ADMIN_ZIP", "123456"));
        admin.setRoles(Set.of(UserRegistrationEntity.Role.ADMIN, UserRegistrationEntity.Role.USER));

        userRepository.save(admin);
        System.out.println("✔ First admin user created successfully.");
    }
}
