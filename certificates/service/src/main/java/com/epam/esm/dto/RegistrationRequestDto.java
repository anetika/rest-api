package com.epam.esm.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
