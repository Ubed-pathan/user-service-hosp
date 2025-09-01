package com.appointment.user_service.Dtos;

import java.util.Set;

public record UserDto(
        String fullName,
        String email,
        String username,
        String id,
        Set<com.appointment.user_service.Entities.UserRegistrationEntity.Role> roles,
        String phoneNumber
){
}