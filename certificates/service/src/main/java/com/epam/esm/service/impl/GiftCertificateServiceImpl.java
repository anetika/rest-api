package com.epam.esm.service.impl;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        this.converter = new GiftCertificateConverter(new TagConverter());
        this.validator = GiftCertificateValidator.getInstance();
    }

    @Override
    @Transactional
    public GiftCertificateDto add(GiftCertificateDto item) throws ServiceException, ValidationException {
        validator.validateGiftCertificate(item);
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
        validator.validateUpdatedGiftCertificate(certificateDto);
        try {
            GiftCertificate certificate = converter.convertDtoToEntity(certificateDto);
            GiftCertificate initialCertificate = certificateRepository.getById(id);
            String initialName = initialCertificate.getName();
            String updatedName = certificate.getName();
            if (updatedName != null && !Objects.equals(initialName, updatedName)) {
                initialCertificate.setName(updatedName);
            }

            String initialDescription = initialCertificate.getDescription();
            String updatedDescription = certificate.getDescription();
            if (updatedDescription != null && !Objects.equals(initialDescription, updatedDescription)) {
                initialCertificate.setDescription(updatedDescription);
            }

            BigDecimal initialPrice = initialCertificate.getPrice();
            BigDecimal updatedPrice = certificate.getPrice();
            if (updatedPrice != null && !Objects.equals(initialPrice, updatedPrice)) {
                initialCertificate.setPrice(updatedPrice);
            }

            int initialDuration = initialCertificate.getDuration();
            int updatedDuration = certificate.getDuration();
            if (updatedDuration != 0 && !Objects.equals(initialDuration, updatedDuration)) {
                initialCertificate.setDuration(updatedDuration);
            }

            LocalDateTime initialCreateDate = initialCertificate.getCreateDate();
            LocalDateTime updatedCreateDate = initialCertificate.getCreateDate();
            if (updatedCreateDate != null && !initialCreateDate.equals(updatedCreateDate)) {
                initialCertificate.setCreateDate(updatedCreateDate);
            }

            LocalDateTime initialLastUpdateDate = initialCertificate.getLastUpdateDate();
            LocalDateTime updatedLastUpdateDate = certificate.getLastUpdateDate();
            if (updatedLastUpdateDate != null && !initialLastUpdateDate.equals(updatedLastUpdateDate)) {
                initialCertificate.setLastUpdateDate(updatedLastUpdateDate);
            }

            if (certificate.getTags() != null){
                deleteOldTags(initialCertificate.getId(), tagRepository.getAllTagsByCertificateId(initialCertificate.getId()));
                initialCertificate.setTags(certificate.getTags());
            }
            GiftCertificate updatedCertificate = certificateRepository.update(id, initialCertificate);
            if (updatedCertificate.getTags() != null){
                updatedCertificate.setTags(addTagsIfNotExist(updatedCertificate.getTags(), updatedCertificate.getId()));
            }
            return converter.convertEntityToDto(updatedCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException("Unable to handle update request in GiftCertificateServiceImpl", e);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundServiceException("Resource not found in GiftCertificateServiceImpl", e);
        }
    }

    private void deleteOldTags(long certificateId, List<Tag> tags){
        for (var tag : tags){
            certificateRepository.deleteGiftCertificateTagConnection(certificateId, tag.getId());
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
