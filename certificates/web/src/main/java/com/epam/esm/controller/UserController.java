package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HateoasUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService service;
    private final HateoasUtil hateoasUtil;

    public UserController(UserService service, HateoasUtil hateoasUtil) {
        this.service = service;
        this.hateoasUtil = hateoasUtil;
    }

    @PostMapping("/orders")
    @PreAuthorize("#buyCertificateDto.userId == principal.id")
    public ResponseEntity<OrderDto> buyCertificate(@RequestBody BuyCertificateDto buyCertificateDto) {
        OrderDto dto = service.buyCertificate(buyCertificateDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> add(@Valid @RequestBody UserDto userDto) {
        UserDto resultDto = service.add(userDto);
        hateoasUtil.attacheUserLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto resultDto = service.getById(id);
        hateoasUtil.attacheUserLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @GetMapping("/users/tags")
    public ResponseEntity<TagDto> getMostWidelyUsedTag() {
        TagDto resultDto = service.getMostWidelyUsedTag();
        hateoasUtil.attacheTagLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getAll(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        Page<UserDto> resultDtos = service.getAll(PageRequest.of(page, size));
        resultDtos.forEach(hateoasUtil::attacheUserLink);
        return new ResponseEntity<>(resultDtos, HttpStatus.OK);
    }
}
