package com.epam.esm.service.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public List<TagDto> getAll(int page, int size) {
        try {
            List<Tag> tags = tagDao.findAll(page, size);
            if (tags.isEmpty()) {
                throw new ResourceNotFoundException("Resource not found");
            }
            return tags.stream().map(converter::convertEntityToDto).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getAll request in TagServiceImpl", e);
        }
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteAll() {
        try {
            tagDao.deleteAll();
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle deleteAll request in TagServiceImpl", e);
        }
    }
}
