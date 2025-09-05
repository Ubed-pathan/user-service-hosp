package com.appointment.user_service.Dtos;

import java.util.Set;

public record UserDto(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String username,
        String id,
        Set<com.appointment.user_service.Entities.UserRegistrationEntity.Role> roles,
        String phoneNumber,
        String address,
        String city,
        String state,
        String country,
        String zipCode,
        int age,
        String gender
){
}