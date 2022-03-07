package com.epam.esm.service;

import com.epam.esm.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface that contains functionality for UserService.
 */
public interface UserService {
    /**
     * Adds a user dto.
     *
     * @param user the user dto
     * @return the user dto
     */
    UserDto add(UserDto user);
    UserDto register(RegistrationDto requestDto);
    /**
     * Gets a user dto by id.
     *
     * @param id the id of the user dto
     * @return the user dto
     */
    UserDto getById(long id);

    /**
     * Gets all user dtos.
     *
     * @return the list of all user dtos
     */
    Page<UserDto> getAll(Pageable pageable);

    UserDto findUserByUserName(String username);
    /**
     * Buys a certificate.
     *
     * @param buyCertificateDto   dto with user id and certificate id
     * @return the order dto
     */
    OrderDto buyCertificate(BuyCertificateDto buyCertificateDto);

    /**
     * Gets most widely used tag dto.
     *
     * @return the most widely used tag dto
     */
    TagDto getMostWidelyUsedTag();

    AuthenticationResponseDto login(AuthenticationDto requestDto);
}
