package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HateoasUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService service;
    private final HateoasUtil hateoasUtil;

    public UserController(UserService service, HateoasUtil hateoasUtil) {
        this.service = service;
        this.hateoasUtil = hateoasUtil;
    }

    @PostMapping("/users/{userId}/certificates/{certificateId}")
    public ResponseEntity<OrderDto> buyCertificate(@PathVariable long certificateId, @PathVariable long userId) {
        OrderDto dto = service.buyCertificate(userId, certificateId);
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
    public ResponseEntity<List<UserDto>> getAll(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        List<UserDto> resultDtos = service.getAll(page, size);
        resultDtos.forEach(hateoasUtil::attacheUserLink);
        return new ResponseEntity<>(resultDtos, HttpStatus.OK);
    }
}
