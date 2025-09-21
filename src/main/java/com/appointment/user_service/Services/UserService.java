package com.appointment.user_service.Services;

import com.appointment.user_service.Config.JwtUtil;
import com.appointment.user_service.Dtos.*;
import com.appointment.user_service.Entities.UserRegistrationEntity;
import com.appointment.user_service.Repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public void register(@Valid UserRegistrationDto dto) {

        if(userRepository.existsByUsername(dto.username())){
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

    public UserVerificationDto isUserValid(UserVerificationDto dto) {
        UUID uuid = UUID.fromString(dto.userId());
        UserRegistrationEntity user = userRepository.findById(uuid).orElse(null);

        if (user == null) {
            return new UserVerificationDto(null, null, null);
        }

        String fullName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
        if (user.getEmail().equals(dto.usersEmail()) && fullName.equals(dto.usersFullName())) {
            return new UserVerificationDto(user.getId().toString(), fullName, user.getEmail());
        } else {
            return new UserVerificationDto(null, null, null);
        }
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
                user.getRoles()
            );
    }

    public UserDto getUserDetails(UUID userId) {


        UserRegistrationEntity user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null; // or throw an exception
        }

        return new UserDto(
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getId().toString(),
                user.getRoles(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getCountry(),
                user.getZipCode(),
                user.getAge(),
                user.getGender()
        );
    }

    public UserDto getUserDetails(String userName) {
        if (userName.isEmpty()) {
            return null;
        }

        UserRegistrationEntity user = userRepository.findByUsername(userName);

        return new UserDto(
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getId().toString(),
                user.getRoles(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getCountry(),
                user.getZipCode(),
                user.getAge(),
                user.getGender()
        );
    }

    public void registerDoctor(UserRegistrationDto dto) {
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
        entity.getRoles().add(UserRegistrationEntity.Role.DOCTOR);         entity.setAge(dto.age());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setAddress(dto.address());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setCountry(dto.country());
        entity.setZipCode(dto.zipCode());

        userRepository.save(entity);
    }

    public List<UserDto> getAllUsers() {

        List<UserRegistrationEntity> users = userRepository.findAll();
        return users.stream().map(user -> {
            return new UserDto(
                    user.getFirstName(),
                    user.getMiddleName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getId().toString(),
                    user.getRoles(),
                    user.getPhoneNumber(),
                    user.getAddress(),
                    user.getCity(),
                    user.getState(),
                    user.getCountry(),
                    user.getZipCode(),
                    user.getAge(),
                    user.getGender()
            );
        }).toList();
    }

    public void deleteUser(UUID uuid) {
        if (userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid);
        } else {
            throw new IllegalArgumentException("User with ID " + uuid + " does not exist.");
        }
    }

    public boolean updateUser(UUID uuid, UserDto userDto) {
        try{
            UserRegistrationEntity user = userRepository.findById(uuid).orElse(null);

            if (user == null) {
                throw new IllegalArgumentException("User with ID " + uuid + " does not exist.");
            }

            user.setFirstName(userDto.firstName());
            user.setMiddleName(userDto.middleName());
            user.setLastName(userDto.lastName());
            user.setPhoneNumber(userDto.phoneNumber());
            user.setAddress(userDto.address());
            user.setCity(userDto.city());
            user.setState(userDto.state());
            user.setCountry(userDto.country());
            user.setZipCode(userDto.zipCode());
            user.setAge(userDto.age());
            user.setGender(userDto.gender());
            userRepository.save(user);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean updatePassword(@Valid ForgetPasswordDto request) {
        UserRegistrationEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User with email " + request.email() + " does not exist."));
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return true;
    }

    public void changePassword(String username, ChangePasswordDto request) {
        UserRegistrationEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User with username " + username + " does not exist.");
        }
        if(passwordEncoder.matches(request.newPassword(), user.getPassword())){
            throw new IllegalArgumentException("New password must be different from the old password.");
        }
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
