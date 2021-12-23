package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

public class TagConverter {

    private static final TagConverter instance = new TagConverter();

    private TagConverter(){}

    public static TagConverter getInstance(){
        return instance;
    }

    public Tag convertDtoToEntity(TagDto dto){
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());
        return tag;
    }

    public TagDto convertEntityToDto(Tag tag){
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
