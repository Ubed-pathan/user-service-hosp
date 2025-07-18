package com.appointment.user_service.Services;

import com.appointment.user_service.Dtos.UserRegistrationDto;
import com.appointment.user_service.Entities.UserRegistrationEntity;
import com.appointment.user_service.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        entity.setPassword(dto.password()); // 🔐 Consider hashing the password
        entity.setAge(dto.age());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setAddress(dto.address());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setCountry(dto.country());
        entity.setZipCode(dto.zipCode());

        userRepository.save(entity);
    }
}
