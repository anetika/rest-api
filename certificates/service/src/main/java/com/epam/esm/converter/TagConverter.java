package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagConverter {
    public Tag convertDtoToEntity(TagDto dto) {
        return Tag.builder().id(dto.getId()).name(dto.getName()).build();
    }

    public TagDto convertEntityToDto(Tag tag) {
        return TagDto.builder().id(tag.getId()).name(tag.getName()).build();
    }
}
