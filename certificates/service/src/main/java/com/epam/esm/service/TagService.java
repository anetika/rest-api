package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @return the list of all tag dtos
     */
    Page<TagDto> getAll(Pageable pageable);

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
