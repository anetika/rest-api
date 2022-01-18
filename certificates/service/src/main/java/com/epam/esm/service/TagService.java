package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto add(TagDto dto);
    TagDto getById(long id);
    List<TagDto> getAll(int page, int size);
    void deleteById(long id);
    void deleteAll();
}
