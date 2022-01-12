package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface GiftCertificateService {
    GiftCertificateDto add(GiftCertificateDto certificateDto);
    GiftCertificateDto getById(long id);
    Page<GiftCertificateDto> getAll(Pageable pageable);
    void deleteById(long id);
    void deleteAll();
    GiftCertificateDto update(long id, GiftCertificateDto certificateDto);
    GiftCertificateDto updateCertificateDuration(long id, int duration);
    GiftCertificateDto updateCertificatePrice(long id, BigDecimal price);
    Page<GiftCertificateDto> getGiftCertificateByTags(List<TagDto> tagDtos, Pageable pageable);
}
