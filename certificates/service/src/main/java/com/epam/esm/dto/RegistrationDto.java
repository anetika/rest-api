package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationDto {

    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$")
    private String password;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String lastName;

    @Email
    @NotNull
    private String email;
}
