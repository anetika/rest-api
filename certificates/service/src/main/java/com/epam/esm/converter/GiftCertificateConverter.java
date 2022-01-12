package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GiftCertificateConverter {
    private final TagConverter tagConverter;

    public GiftCertificateConverter(TagConverter tagConverter) {
        this.tagConverter = tagConverter;
    }


    public GiftCertificate convertDtoToEntity(GiftCertificateDto certificateDto) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(certificateDto.getId());
        certificate.setName(certificateDto.getName());
        certificate.setDescription(certificateDto.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        certificate.setDuration(certificateDto.getDuration());
        certificate.setCreateDate(certificateDto.getCreateDate());
        certificate.setLastUpdateDate(certificateDto.getLastUpdateDate());
        if (certificateDto.getTags() != null){
            certificate.setTags(certificateDto.getTags().stream().map(tagConverter::convertDtoToEntity).collect(Collectors.toSet()));
        }
        return certificate;
    }

    public GiftCertificateDto convertEntityToDto(GiftCertificate certificate) {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setId(certificate.getId());
        certificateDto.setName(certificate.getName());
        certificateDto.setDescription(certificate.getDescription());
        certificateDto.setPrice(certificate.getPrice());
        certificateDto.setDuration(certificate.getDuration());
        certificateDto.setCreateDate(certificate.getCreateDate());
        certificateDto.setLastUpdateDate(certificate.getLastUpdateDate());
        if (certificate.getTags() != null){
            certificateDto.setTags(certificate.getTags().stream().map(tagConverter::convertEntityToDto).collect(Collectors.toSet()));
        }
        return certificateDto;
    }
}
