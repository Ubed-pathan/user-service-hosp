package com.appointment.user_service.Config;

import com.appointment.user_service.Entities.UserRegistrationEntity;
import com.appointment.user_service.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${ADMIN_FIRST_NAME}")
    private String adminFirstName;

    @Value("${ADMIN_MIDDLE_NAME}")
    private String adminMiddleName;

    @Value("${ADMIN_LAST_NAME}")
    private String adminLastName;

    @Value("${ADMIN_AGE}")
    private String adminAge;

    @Value("${ADMIN_PHONE}")
    private String adminPhone;

    @Value("${ADMIN_ADDRESS}")
    private String adminAddress;

    @Value("${ADMIN_CITY}")
    private String adminCity;

    @Value("${ADMIN_STATE}")
    private String adminState;

    @Value("${ADMIN_COUNTRY}")
    private String adminCountry;

    @Value("${ADMIN_ZIP}")
    private String adminZip;

    @PostConstruct
    public void createAdminUserIfNotExists() {
        System.out.println(adminUsername);
        String username = adminUsername;
        String email = adminEmail;
        String password = adminPassword;

        if (username == null || email == null || password == null) {
            throw new RuntimeException("❌ Admin credentials not set in environment variables or properties");
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
        admin.setFirstName(adminFirstName);
        admin.setMiddleName(adminMiddleName);
        admin.setLastName(adminLastName);
        admin.setAge(Integer.parseInt(adminAge));
        admin.setPhoneNumber(adminPhone);
        admin.setAddress(adminAddress);
        admin.setCity(adminCity);
        admin.setState(adminState);
        admin.setCountry(adminCountry);
        admin.setZipCode(adminZip);
        admin.setRoles(Set.of(UserRegistrationEntity.Role.ADMIN, UserRegistrationEntity.Role.USER));

        userRepository.save(admin);
        System.out.println("✔ First admin user created successfully.");
    }
}
