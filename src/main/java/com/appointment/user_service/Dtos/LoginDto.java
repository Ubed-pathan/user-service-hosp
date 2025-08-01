package com.appointment.user_service.Dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "username cannot be blank")
        String username,

        @NotBlank(message = "password can not be blank")
        String password
) {
}
