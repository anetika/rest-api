package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

/**
 * The interface that contains functionality for TagService.
 */
public interface TagService {
    /**
     * Adds tag dto.
     *
     * @param dto the tag dto
     * @return the tag dto
     */
    TagDto add(TagDto dto);

    /**
     * Gets a tag dto by id.
     *
     * @param id the id of the tag dto
     * @return the tag dto
     */
    TagDto getById(long id);

    /**
     * Gets all tag dtos.
     *
     * @param page the page
     * @param size the size
     * @return the list of all tag dtos
     */
    List<TagDto> getAll(int page, int size);

    /**
     * Deletes a tag dto by id.
     *
     * @param id the id of the tag dto
     */
    void deleteById(long id);

    /**
     * Deletes all tag dtos.
     */
    void deleteAll();
}
