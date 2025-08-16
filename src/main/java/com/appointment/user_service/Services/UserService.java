package com.appointment.user_service.Services;

import com.appointment.user_service.Config.JwtUtil;
import com.appointment.user_service.Dtos.LoginDto;
import com.appointment.user_service.Dtos.UserRegistrationDto;
import com.appointment.user_service.Dtos.UserVerificationDto;
import com.appointment.user_service.Entities.UserRegistrationEntity;
import com.appointment.user_service.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

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

    public boolean isUserValid(UserVerificationDto dto) {
        UUID uuid = UUID.fromString(dto.userId());
        UserRegistrationEntity user  = userRepository.findById(uuid).orElse(null);

        if(user == null) {
            return false;
        }

        return user.getEmail().equals(dto.usersEmail()) && (user.getFirstName() + " " + user.getMiddleName()+ " "+ user.getLastName()).equals(dto.usersFullName());
    }

    public String login(LoginDto userDto) {
        Optional<UserRegistrationEntity> optionalUser = Optional.ofNullable(userRepository.findByUsername(userDto.username()));

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        UserRegistrationEntity user = optionalUser.get();

        if (!passwordEncoder.matches(userDto.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Create and return JWT
        return jwtUtil.generateToken(
                user.getUsername(),
                user.getId().toString(),
                user.getEmail(),
                "USER" // Replace with actual role if available
        );
    }
}
