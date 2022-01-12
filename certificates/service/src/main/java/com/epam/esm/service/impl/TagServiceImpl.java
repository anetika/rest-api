package com.epam.esm.service.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.dao.DataAccessException;
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

    public TagServiceImpl(TagConverter converter, TagDao tagDao) {
        this.converter = converter;
        this.tagDao = tagDao;
    }

    @Transactional
    @Override
    public TagDto add(TagDto dto) {
        Tag tag = converter.convertDtoToEntity(dto);
        try {
            Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
            if (optionalTag.isEmpty()) {
                return converter.convertEntityToDto(tagDao.save(tag));
            }
            return converter.convertEntityToDto(optionalTag.get());
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle add request in TagServiceImpl", e);
        }
    }

    @Override
    public TagDto getById(long id) {
        try {
            Optional<Tag> optionalTag = tagDao.findById(id);
            if (optionalTag.isPresent()) {
                return converter.convertEntityToDto(optionalTag.get());
            }
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getById request in TagServiceImpl", e);
        }
    }

    @Override
    public Page<TagDto> getAll(Pageable pageable) {
        try {
            Page<Tag> tags = tagDao.findAll(pageable);
            if (tags.isEmpty()) {
                throw new ResourceNotFoundException("Resource not found");
            }
            return new PageImpl<>(tags.stream().map(converter::convertEntityToDto).collect(Collectors.toList()));
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getAll request in TagServiceImpl", e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            if (tagDao.findById(id).isPresent()) {
                tagDao.deleteById(id);
            } else {
                throw new ResourceNotFoundException("Resource not found");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle deleteById request in TagServiceImpl", e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            tagDao.deleteAll();
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle deleteAll request in TagServiceImpl", e);
        }
    }
}
