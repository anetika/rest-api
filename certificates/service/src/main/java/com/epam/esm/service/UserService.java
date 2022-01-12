package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto add(UserDto user);
    UserDto getById(long id);
    Page<UserDto> getAll(Pageable pageable);
    OrderDto buyCertificate(long userId, long certificateId);
    TagDto getMostWidelyUsedTag();
}
