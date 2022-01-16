package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto user);
    UserDto getById(long id);
    List<UserDto> getAll(int page, int size);
    OrderDto buyCertificate(long userId, long certificateId);
    TagDto getMostWidelyUsedTag();
}
