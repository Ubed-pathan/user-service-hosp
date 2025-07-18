package com.appointment.user_service.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record UserRegistrationDto(

        String firstName,

        String middleName,

        String lastName,

        @NotBlank(message = "Username cannot be blank")
        String username,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password,

        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 120, message = "Age must be at most 120")
        int age,

        @NotBlank(message = "Phone number cannot be blank")
        String phoneNumber,

        @NotBlank(message = "Address cannot be blank")
        String address,

        @NotBlank(message = "City cannot be blank")
        String city,

        @NotBlank(message = "State cannot be blank")
        String state,

        @NotBlank(message = "Country cannot be blank")
        String country,

        @NotBlank(message = "Zip code cannot be blank")
        String zipCode

) {
}
