package com.appointment.user_service.Dtos;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDto(
        @NotBlank(message = "Current password cannot be blank")
        String currentPassword,
        @NotBlank(message = "New password cannot be blank")
        String newPassword
) {
}
