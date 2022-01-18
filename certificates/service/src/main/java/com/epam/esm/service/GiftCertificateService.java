package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import java.math.BigDecimal;
import java.util.List;

public interface GiftCertificateService {
    GiftCertificateDto add(GiftCertificateDto certificateDto);
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getAll(int page, int size);
    void deleteById(long id);
    void deleteAll();
    GiftCertificateDto update(long id, GiftCertificateDto certificateDto);
    GiftCertificateDto updateCertificateDuration(long id, int duration);
    GiftCertificateDto updateCertificatePrice(long id, BigDecimal price);
    List<GiftCertificateDto> getGiftCertificateByTags(List<TagDto> tagDtos, int page, int size);
}
