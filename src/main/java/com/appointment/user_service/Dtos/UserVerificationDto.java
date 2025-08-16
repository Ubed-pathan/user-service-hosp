package com.appointment.user_service.Dtos;

public record UserVerificationDto(
        String userId,
        String usersFullName,
        String usersEmail
) {
}
