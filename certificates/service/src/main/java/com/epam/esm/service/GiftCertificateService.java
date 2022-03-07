package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * The interface that contains functionality for GiftCertificateService.
 */
public interface GiftCertificateService {
    /**
     * Adds a certificate dto.
     *
     * @param certificateDto the certificate dto
     * @return the certificate dto
     */
    GiftCertificateDto add(GiftCertificateDto certificateDto);

    /**
     * Gets a certificate by id.
     *
     * @param id the id of the certificate dto
     * @return the certificate dto
     */
    GiftCertificateDto getById(long id);

    /**
     * Gets all certificate dtos.
     *
     * @param params the params
     * @return the list of all certificate dtos
     */
    Page<GiftCertificateDto> getAll(Map<String, String> params, Pageable pageable);

    /**
     * Deletes a certificate dto by id.
     *
     * @param id the id of the certificate dto
     */
    void deleteById(long id);

    /**
     * Deletes all certificate dtos.
     */
    void deleteAll();

    /**
     * Updates a certificate dto.
     *
     * @param id             the id of the certificate dto
     * @param certificateDto the certificate dto
     * @return the gift certificate dto
     */
    GiftCertificateDto update(long id, GiftCertificateDto certificateDto);

    /**
     * Gets a certificate dto by tags.
     *
     * @param tagDtos the tag dtos
     * @param page    the page
     * @param size    the size
     * @return the certificate dto
     */
    List<GiftCertificateDto> getGiftCertificateByTags(List<TagDto> tagDtos, int page, int size);
}
