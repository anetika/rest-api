package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    GiftCertificateDto add(GiftCertificateDto certificateDto);
    GiftCertificateDto getById(long id);
    List<GiftCertificateDto> getAll(int page, int size, Map<String, String> params);
    void deleteById(long id);
    void deleteAll();
    GiftCertificateDto update(long id, GiftCertificateDto certificateDto);
    List<GiftCertificateDto> getGiftCertificateByTags(List<TagDto> tagDtos, int page, int size);
}
