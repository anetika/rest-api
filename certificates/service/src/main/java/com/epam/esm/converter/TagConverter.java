package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

/**
 * The converter for {@link Tag} and {@link TagDto}.
 */
@Component
public class TagConverter {
    /**
     * Converts tag dto to entity.
     *
     * @param dto the tag dto
     * @return the tag
     */
    public Tag convertDtoToEntity(TagDto dto) {
        return Tag.builder().id(dto.getId()).name(dto.getName()).build();
    }

    /**
     * Converts entity to tag dto.
     *
     * @param tag the tag
     * @return the tag dto
     */
    public TagDto convertEntityToDto(Tag tag) {
        return TagDto.builder().id(tag.getId()).name(tag.getName()).build();
    }
}
