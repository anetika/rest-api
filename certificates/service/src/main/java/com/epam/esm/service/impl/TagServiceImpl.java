package com.epam.esm.service.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagConverter converter;
    private final TagDao tagDao;
    private final PaginationUtil paginationUtil;

    public TagServiceImpl(TagConverter converter, TagDao tagDao, PaginationUtil paginationUtil) {
        this.converter = converter;
        this.tagDao = tagDao;
        this.paginationUtil = paginationUtil;
    }


    @Override
    @Transactional
    public TagDto add(TagDto dto) {
        Tag tag = converter.convertDtoToEntity(dto);
        Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
        if (optionalTag.isEmpty()) {
            return converter.convertEntityToDto(tagDao.save(tag));
        }
        return converter.convertEntityToDto(optionalTag.get());
    }

    @Override
    public TagDto getById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (optionalTag.isPresent()) {
            return converter.convertEntityToDto(optionalTag.get());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    public Page<TagDto> getAll(Pageable pageable) {
        Page<Tag> tags = tagDao.findAll(pageable);
        if (tags.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return new PageImpl<>(tags.getContent().stream().map(converter::convertEntityToDto).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (tagDao.findById(id).isPresent()) {
            tagDao.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    @Override
    public void deleteAll() {
        tagDao.deleteAll();
    }
}
