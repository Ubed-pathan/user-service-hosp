package com.appointment.user_service.Repositories;

import com.appointment.user_service.Entities.UserRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserRegistrationEntity, UUID> {

    // Method to find a user by username
    UserRegistrationEntity findByUsername(String username);

    // Method to find a user by email
    UserRegistrationEntity findByEmail(String email);

    // Method to check if a username already exists
    boolean existsByUsername(String username);

    // Method to check if an email already exists
    boolean existsByEmail(String email);

    boolean existsById(UUID id);

}
