package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class AuthenticationDto {

    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$")
    private String password;
}
