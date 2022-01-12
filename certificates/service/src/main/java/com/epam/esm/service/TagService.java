package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
    TagDto add(TagDto dto);
    TagDto getById(long id);
    Page<TagDto> getAll(Pageable pageable);
    void deleteById(long id);
    void deleteAll();
}
