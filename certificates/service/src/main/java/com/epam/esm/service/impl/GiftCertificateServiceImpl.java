package com.epam.esm.service.impl;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao certificateRepository;
    private final TagDao tagRepository;
    private final GiftCertificateConverter converter;
    private final GiftCertificateValidator validator;

    public GiftCertificateServiceImpl(GiftCertificateDao certificateRepository, TagDao tagRepository) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.converter = GiftCertificateConverter.getInstance();
        this.validator = GiftCertificateValidator.getInstance();
    }

    @Override
    @Transactional
    public GiftCertificateDto add(GiftCertificateDto item) throws ServiceException, ValidationException {
        if (!validator.validateGiftCertificate(item)){
            throw new ValidationException("GiftCertificateDto isn't valid");
        }
        try {
            GiftCertificate certificate = converter.convertDtoToEntity(item);
            certificate.setCreateDate(LocalDateTime.now());
            certificate.setLastUpdateDate(LocalDateTime.now());
            GiftCertificate addedCertificate = certificateRepository.add(certificate);
            for (var tag : addedCertificate.getTags()){
                try{
                    tag.setId(tagRepository.getByName(tag.getName()).getId());
                } catch (ResourceNotFoundException e) {
                    tag.setId(tagRepository.add(tag).getId());
                }
                certificateRepository.addGiftCertificateTagConnection(addedCertificate.getId(), tag.getId());
            }
            return converter.convertEntityToDto(addedCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle add request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto getById(long id) throws ServiceException, ResourceNotFoundServiceException {
        try {
            GiftCertificate certificate = certificateRepository.getById(id);
            certificate.setTags(tagRepository.getAllTagsByCertificateId(certificate.getId()));
            return converter.convertEntityToDto(certificate);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundServiceException("Resource not found");
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle getById request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public void delete(long id) throws ServiceException, ResourceNotFoundServiceException {
        try {
            certificateRepository.getById(id);
            certificateRepository.delete(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle delete request in GiftCertificateServiceImpl", e);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundServiceException("Resource not found");
        }
    }

    @Override
    @Transactional
    public void deleteAll() throws ServiceException {
        try {
            certificateRepository.deleteAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle deleteAll request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> getAll(Map<String, String> params) throws ServiceException {
        try {
            List<GiftCertificate> certificates = certificateRepository.getAll(params);
            for (var certificate : certificates) {
                certificate.setTags(tagRepository.getAllTagsByCertificateId(certificate.getId()));
            }
            return certificates.stream().map(converter::convertEntityToDto).collect(Collectors.toList());
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle getAll request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto certificateDto) throws ServiceException, ResourceNotFoundServiceException, ValidationException {
        if (!validator.validateGiftCertificate(certificateDto)){
            throw new ValidationException("GiftCertificateDto isn't valid!");
        }
        try {
            GiftCertificate certificate = converter.convertDtoToEntity(certificateDto);
            GiftCertificate updatedCertificate = certificateRepository.update(id, certificate);
            updatedCertificate.setTags(addTagsIfNotExist(updatedCertificate.getTags(), updatedCertificate.getId()));
            return converter.convertEntityToDto(updatedCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle update request in GiftCertificateServiceImpl", e);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundServiceException("Resource not found in GiftCertificateServiceImpl", e);
        }
    }

    private List<Tag> addTagsIfNotExist(List<Tag> tags, long certificateId) throws RepositoryException {
        List<Tag> tagList = new ArrayList<>();
        for (var tag : tags){
            try {
                Tag initialTag = tagRepository.getByName(tag.getName());
                tagList.add(initialTag);
                certificateRepository.addGiftCertificateTagConnection(certificateId, initialTag.getId());
            } catch (ResourceNotFoundException e) {
                Tag updatedTag = tagRepository.add(tag);
                tagList.add(updatedTag);
                certificateRepository.addGiftCertificateTagConnection(certificateId, updatedTag.getId());
            }
        }
        return tagList;
    }
}
