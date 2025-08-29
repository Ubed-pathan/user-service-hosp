package com.appointment.user_service.Dtos;

public record UserDto(
        String fullName,
        String email,
        String username,
        String id
){
}