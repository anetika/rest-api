package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * The converter for {@link GiftCertificate} and {@link GiftCertificateDto}.
 */
@Component
public class GiftCertificateConverter {
    private final TagConverter tagConverter;

    public GiftCertificateConverter(TagConverter tagConverter) {
        this.tagConverter = tagConverter;
    }


    /**
     * Converts gift certificate dto to entity.
     *
     * @param certificateDto the certificate dto
     * @return the gift certificate
     */
    public GiftCertificate convertDtoToEntity(GiftCertificateDto certificateDto) {
        return GiftCertificate.builder()
                .id(certificateDto.getId())
                .name(certificateDto.getName())
                .description(certificateDto.getDescription())
                .price(certificateDto.getPrice())
                .duration(certificateDto.getDuration())
                .createDate(certificateDto.getCreateDate())
                .lastUpdateDate(certificateDto.getLastUpdateDate())
                .tags(certificateDto.getTags() == null ? new HashSet<>() : certificateDto.getTags()
                        .stream().map(tagConverter::convertDtoToEntity).collect(Collectors.toSet()))
                .build();
    }

    /**
     * Converts entity to gift certificate dto.
     *
     * @param certificate the certificate
     * @return the gift certificate dto
     */
    public GiftCertificateDto convertEntityToDto(GiftCertificate certificate) {
        return GiftCertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .duration(certificate.getDuration())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .tags(certificate.getTags() == null ? new HashSet<>() : certificate.getTags()
                        .stream().map(tagConverter::convertEntityToDto).collect(Collectors.toSet()))
                .build();
    }
}
