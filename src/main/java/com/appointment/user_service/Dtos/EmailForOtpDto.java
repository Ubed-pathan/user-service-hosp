package com.appointment.user_service.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailForOtpDto(
        @Email( message = "Email should be valid")
        @NotBlank(message = "Email cannot be blank")
        String email
) {
}
