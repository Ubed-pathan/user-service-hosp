package com.appointment.user_service.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;


    @Column(
            name = "f_name",
            length = 25

    )
    String firstName;

    @Column(
            name = "m_name",
            length = 25

    )
    String middleName;

    @Column(
            name = "l_name",
            length = 25

    )
    String lastName;

    @Column(
            unique = true,
            nullable = false
    )
    @NotBlank
    String username;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;


    @Column(
            nullable = false
    )
    @NotBlank
    String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    {
        roles.add(Role.USER);
    }

    public enum Role {
        USER,
        DOCTOR,
        ADMIN
    }

    @Column(nullable = false)
    @Min(1)
    @Max(100)
    int age;

    String gender;

    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(
            nullable = false,
            length = 100
    )
    String address;

    @Column(
            nullable = false,
            length = 50
    )
    String city;

    @Column(
            nullable = false,
            length = 50
    )
    String state;

    @Column(
            nullable = false,
            length = 50
    )
    String country;

    @Column(
            nullable = false,
            length = 10
    )
    String zipCode;


    @Column(
            updatable = false,
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            insertable = false
    )
    private LocalDateTime lastModified;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = LocalDateTime.now();
    }

}
