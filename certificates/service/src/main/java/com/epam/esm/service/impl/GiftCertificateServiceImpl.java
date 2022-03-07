package com.epam.esm.service.impl;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String TAG = "tag";
    private static final String SORT = "sort";
    private static final String SEARCH = "search";

    private final TagConverter tagConverter;
    private final GiftCertificateConverter converter;
    private final GiftCertificateDao certificateDao;
    private final TagDao tagDao;

    public GiftCertificateServiceImpl(TagConverter tagConverter, GiftCertificateConverter converter, GiftCertificateDao certificateDao, TagDao tagDao) {
        this.tagConverter = tagConverter;
        this.converter = converter;
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
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
    public GiftCertificateDto getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateDao.findById(id);
        if (optionalGiftCertificate.isPresent()){
            return converter.convertEntityToDto(optionalGiftCertificate.get());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    @Transactional
    public Page<GiftCertificateDto> getAll(Map<String, String> params, Pageable pageable) {
        validateParams(params);
        //paginationUtil.validatePaginationInfo(page, size);
        Page<GiftCertificate> certificates = certificateDao.findAll(params, pageable);
        if (certificates.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return new PageImpl<>(certificates.getContent().stream().map(converter::convertEntityToDto).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (certificateDao.findById(id).isPresent()) {
            certificateDao.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    @Override
    public void deleteAll() {
        tagDao.deleteAll();
    }

    @Override
    @Transactional
    public GiftCertificateDto update(long id, GiftCertificateDto certificateDto) {
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
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> getGiftCertificateByTags(List<TagDto> tagDtos, int page, int size) {
        List<Tag> tags = tagDtos.stream().map(tagConverter::convertDtoToEntity).collect(Collectors.toList());
        for (var tag : tags) {
            Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
            if (optionalTag.isPresent()) {
                tag.setId(optionalTag.get().getId());
            } else {
                throw new ResourceNotFoundException("Resource not found");
            }
        }
        List<GiftCertificate> giftCertificates = certificateDao.findGiftCertificatesByTags(tags, page, size);
        if (giftCertificates.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return giftCertificates.stream().map(converter::convertEntityToDto).collect(Collectors.toList());
    }

    private void validateParams(Map<String, String> params) {
        validateTagParameter(params.get(TAG));
        validateSortParameter(params.get(SORT));
        validateSearchParameter(params.get(SEARCH));
    }

    private void validateSearchParameter(String search) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher matcher = pattern.matcher(search);
        if (!search.equals("") && !matcher.matches()) {
            throw new IllegalArgumentException("Incorrect search parameter");
        }
    }

    private void validateSortParameter(String sort) {
        Pattern pattern = Pattern.compile("^(ASC|DESC)$");
        Matcher matcher = pattern.matcher(sort);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Incorrect sort parameter");
        }
    }

    private void validateTagParameter(String tag) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_]{3,16}$");
        Matcher matcher = pattern.matcher(tag);
        if (!tag.equals("") && !matcher.matches()) {
            throw new IllegalArgumentException("Incorrect tag parameter");
        }
    }
}
