package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.AuthenticationResponseDto;
import com.epam.esm.dto.RegistrationDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.HateoasUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    private final UserServiceImpl service;
    private final HateoasUtil hateoasUtil;

    public AuthenticationController(UserServiceImpl service, HateoasUtil hateoasUtil) {
        this.service = service;
        this.hateoasUtil = hateoasUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationDto requestDto) {
        AuthenticationResponseDto resultDto = service.login(requestDto);
        return new ResponseEntity<>(resultDto, HttpStatus.FOUND);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationDto requestDto) {
        UserDto resultDto = service.register(requestDto);
        hateoasUtil.attacheUserLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }
}
