package com.epam.esm.service.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.dao.TagDao;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao repository;
    private final TagConverter converter;
    private final TagValidator validator;

    public TagServiceImpl(TagDao repository) {
        this.repository = repository;
        this.converter = TagConverter.getInstance();
        this.validator = TagValidator.getInstance();
    }

    @Override
    @Transactional
    public TagDto add(TagDto item) throws ServiceException, ValidationException {
        if (!validator.validateTag(item)){
            throw new ValidationException("TagDto isn't valid!");
        }
        Tag tag = converter.convertDtoToEntity(item);
        try{
            try {
                Tag initialTag = repository.getByName(tag.getName());
                return converter.convertEntityToDto(initialTag);
            }catch (ResourceNotFoundException e) {
                return converter.convertEntityToDto(repository.add(tag));
            }
        } catch (RepositoryException e){
            throw new ServiceException("Unable to handle add request in TagServiceImpl", e);
        }
    }

    @Override
    public TagDto getById(long id) throws ServiceException, ResourceNotFoundServiceException {
        try {
            return converter.convertEntityToDto(repository.getById(id));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundServiceException("Resource not found in TagServiceImpl", e);
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle getById request in TagServiceImpl", e);
        }
    }

    @Override
    public List<TagDto> getAll() throws ServiceException {
        try {
            return repository.getAll().stream().map(converter::convertEntityToDto).collect(Collectors.toList());
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle getAll request in TagServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public void delete(long id) throws ServiceException, ResourceNotFoundServiceException {
        try {
            try{
                repository.getById(id);
                repository.delete(id);
            } catch (ResourceNotFoundException e){
                throw new ResourceNotFoundServiceException("Resource not found in TagServiceImpl", e);
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle delete request in TagServiceImpl", e);
        }
    }

    @Override
    public void deleteAll() throws ServiceException {
        try {
            repository.deleteAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle deleteAll request in TagServiceImpl", e);
        }
    }


}
