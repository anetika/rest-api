package com.epam.esm.service.impl;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateUtilDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final TagConverter tagConverter;
    private final GiftCertificateConverter converter;
    private final GiftCertificateDao certificateDao;
    private final TagDao tagDao;
    private final GiftCertificateUtilDao giftCertificateUtilDao;

    public GiftCertificateServiceImpl(TagConverter tagConverter, GiftCertificateConverter converter, GiftCertificateDao certificateDao, TagDao tagDao, GiftCertificateUtilDao giftCertificateUtilDao) {
        this.tagConverter = tagConverter;
        this.converter = converter;
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.giftCertificateUtilDao = giftCertificateUtilDao;
    }

    @Override
    @Transactional
    public GiftCertificateDto add(GiftCertificateDto certificateDto) {
        GiftCertificate certificate = converter.convertDtoToEntity(certificateDto);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        Set<Tag> tags = certificate.getTags();
        for (var tag : tags) {
            Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
            if (optionalTag.isEmpty()) {
                tagDao.save(tag);
            } else {
                Tag existedTag = optionalTag.get();
                tag.setId(existedTag.getId());
            }
        }

        return converter.convertEntityToDto(certificateDao.save(certificate));
    }

    @Override
    @Transactional
    public GiftCertificateDto getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateDao.findById(id);
        if (optionalGiftCertificate.isPresent()){
            return converter.convertEntityToDto(optionalGiftCertificate.get());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> getAll(int page, int size) {
        try {
            List<GiftCertificate> certificates = certificateDao.findAll(page, size);
            if (certificates.isEmpty()) {
                throw new ResourceNotFoundException("Resource not found");
            }
            return certificates.stream().map(converter::convertEntityToDto).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getAll request in GiftCertificateServiceImpl", e);
        }

    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            if (certificateDao.findById(id).isPresent()) {
                certificateDao.deleteById(id);
            } else {
                throw new ResourceNotFoundException("Resource not found");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle deleteById request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        try {
            tagDao.deleteAll();
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle deleteAll request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto certificateDto) {
        try {
            Optional<GiftCertificate> optionalGiftCertificate = certificateDao.findById(id);
            if (optionalGiftCertificate.isEmpty()) {
                throw new ResourceNotFoundException("Resource not found");
            }
            GiftCertificate initialCertificate = optionalGiftCertificate.get();
            GiftCertificate certificate = converter.convertDtoToEntity(certificateDto);
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

            if (certificate.getTags() != null) {
                initialCertificate.setTags(certificate.getTags());
                for (var tag : initialCertificate.getTags()) {
                    Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
                    if (optionalTag.isEmpty()) {
                        tagDao.save(tag);
                    } else {
                        Tag existedTag = optionalTag.get();
                        tag.setId(existedTag.getId());
                    }
                }
            }
            initialCertificate.setId(id);
            GiftCertificate updatedCertificate = certificateDao.update(initialCertificate);
            return converter.convertEntityToDto(updatedCertificate);
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle update request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto updateCertificateDuration(long id, int duration) {
        try {
            Optional<GiftCertificate> optionalCertificate = certificateDao.findById(id);
            if (optionalCertificate.isPresent()) {
                LocalDateTime dateTime = LocalDateTime.now();
                certificateDao.updateCertificateDuration(id, duration, dateTime);
                GiftCertificate certificate = optionalCertificate.get();
                certificate.setDuration(duration);
                certificate.setLastUpdateDate(dateTime);
                return converter.convertEntityToDto(certificate);
            }
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle updateCertificateDuration request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto updateCertificatePrice(long id, BigDecimal price) {
        try {
            Optional<GiftCertificate> optionalCertificate = certificateDao.findById(id);
            if (optionalCertificate.isPresent()) {
                LocalDateTime dateTime = LocalDateTime.now();
                certificateDao.updateCertificatePrice(id, price, dateTime);
                GiftCertificate certificate = optionalCertificate.get();
                certificate.setPrice(price);
                certificate.setLastUpdateDate(dateTime);
                return converter.convertEntityToDto(certificate);
            }
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle updateCertificatePrice request in GiftCertificateServiceImpl", e);
        }
    }

    @Override
    @Transactional
    public Page<GiftCertificateDto> getGiftCertificateByTags(List<TagDto> tagDtos, Pageable pageable) {
        List<Tag> tags = tagDtos.stream().map(tagConverter::convertDtoToEntity).collect(Collectors.toList());
        try {
            for (var tag : tags) {
                Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
                if (optionalTag.isPresent()) {
                    tag.setId(optionalTag.get().getId());
                } else {
                    throw new ResourceNotFoundException("Resource not found");
                }
            }
            List<GiftCertificate> giftCertificates = giftCertificateUtilDao.findGiftCertificatesByTags(tags);
            if (giftCertificates.isEmpty()) {
                throw new ResourceNotFoundException("Resource not found");
            }
            return new PageImpl<>(giftCertificates.stream().map(converter::convertEntityToDto)
                    .collect(Collectors.toList()), pageable, giftCertificates.size());
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getGiftCertificateByTags request in GiftCertificateServiceImpl", e);
        }
    }
}
