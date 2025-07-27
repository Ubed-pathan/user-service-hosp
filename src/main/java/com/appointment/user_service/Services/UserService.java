package com.appointment.user_service.Services;

import com.appointment.user_service.Dtos.UserRegistrationDto;
import com.appointment.user_service.Entities.UserRegistrationEntity;
import com.appointment.user_service.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(@Valid UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        UserRegistrationEntity entity = new UserRegistrationEntity();

        entity.setFirstName(dto.firstName());
        entity.setMiddleName(dto.middleName());
        entity.setLastName(dto.lastName());
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        entity.setAge(dto.age());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setAddress(dto.address());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setCountry(dto.country());
        entity.setZipCode(dto.zipCode());

        userRepository.save(entity);
    }

    public boolean isUserExists(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        UUID uuid = UUID.fromString(userId);
        return userRepository.existsById(uuid);
    }
}
