package com.epam.esm.service;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateDao certificateDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private GiftCertificateConverter certificateConverter;

    private final List<GiftCertificateDto> dtos = new ArrayList<>();

    private final List<GiftCertificate> certificates = new ArrayList<>();

    @BeforeEach
    public void setUpDtos(){
        GiftCertificateConverter converter = new GiftCertificateConverter(new TagConverter());

        GiftCertificateDto dto1 = new GiftCertificateDto();
        dto1.setId(1);
        dto1.setName("Holiday");
        dto1.setDescription("description 1");
        dto1.setPrice(new BigDecimal(1000));
        dto1.setDuration(10);
        dto1.setCreateDate(LocalDateTime.now());
        dto1.setLastUpdateDate(LocalDateTime.now());
        dto1.setTags(new HashSet<>());

        GiftCertificateDto dto2 = new GiftCertificateDto();
        dto2.setId(2);
        dto2.setName("Birthday");
        dto2.setDescription("description 2");
        dto2.setPrice(new BigDecimal(2000));
        dto2.setDuration(14);
        dto2.setCreateDate(LocalDateTime.now());
        dto2.setLastUpdateDate(LocalDateTime.now());
        dto2.setTags(new HashSet<>());

        GiftCertificateDto dto3 = new GiftCertificateDto();
        dto3.setId(3);
        dto3.setName("Pet");
        dto3.setDescription("description 3");
        dto3.setPrice(new BigDecimal(100000));
        dto3.setDuration(100);
        dto3.setCreateDate(LocalDateTime.now());
        dto3.setLastUpdateDate(LocalDateTime.now());
        dto3.setTags(new HashSet<>());

        dtos.add(dto1);
        dtos.add(dto2);
        dtos.add(dto3);

        GiftCertificate certificate1 = converter.convertDtoToEntity(dto1);
        GiftCertificate certificate2 = converter.convertDtoToEntity(dto2);
        GiftCertificate certificate3 = converter.convertDtoToEntity(dto3);

        certificates.add(certificate1);
        certificates.add(certificate2);
        certificates.add(certificate3);
    }

    @Test
    public void add_CertificateWithValidInfo_Success() {
        when(certificateConverter.convertDtoToEntity(dtos.get(0))).thenReturn(certificates.get(0));
        when(certificateDao.save(any())).thenReturn(certificates.get(0));
        when(certificateConverter.convertEntityToDto(certificates.get(0))).thenReturn(dtos.get(0));
        GiftCertificateDto certificateDto = service.add(dtos.get(0));
        assertEquals(certificateDto, dtos.get(0));
    }

    @Test
    public void getById_ValidId_Success() {
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificates.get(0)));
        when(certificateConverter.convertEntityToDto(certificates.get(0))).thenReturn(dtos.get(0));
        GiftCertificateDto certificateDto = service.getById(1);
        assertEquals(certificateDto, dtos.get(0));
    }

    @Test
    public void getAll_Success() {
        when(certificateDao.findAll(1, 3)).thenReturn(certificates);
        when(certificateConverter.convertEntityToDto(certificates.get(0))).thenReturn(dtos.get(0));
        when(certificateConverter.convertEntityToDto(certificates.get(1))).thenReturn(dtos.get(1));
        when(certificateConverter.convertEntityToDto(certificates.get(2))).thenReturn(dtos.get(2));
        List<GiftCertificateDto> list = service.getAll(1, 3);
        assertEquals(list, dtos);
    }

    @Test
    public void deleteById_CorrectId_Success() {
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificates.get(0)));
        doAnswer(invocation -> {
            dtos.remove(0);
            certificates.remove(0);
            return null;
        }).when(certificateDao).deleteById(1);
        service.deleteById(1);
        when(certificateDao.findAll(1, 2)).thenReturn(certificates);
        when(certificateConverter.convertEntityToDto(certificates.get(0))).thenReturn(dtos.get(0));
        when(certificateConverter.convertEntityToDto(certificates.get(1))).thenReturn(dtos.get(1));
        List<GiftCertificateDto> certificateDtoList = service.getAll(1, 2);
        assertEquals(certificateDtoList, dtos);
    }

    @Test
    public void deleteAll_Success() {
        int page = 0;
        int size = 3;
        doAnswer(invocation -> {
            certificates.clear();
            return null;
        }).when(tagDao).deleteAll();
        service.deleteAll();
        dtos.clear();
        when(certificateDao.findAll(page, size)).thenReturn(certificates);
        assertThrows(ResourceNotFoundException.class, () -> service.getAll(page, size));
    }

    @Test
    public void update_ValidCertificateInfo_Success() {
        when(certificateConverter.convertDtoToEntity(dtos.get(0))).thenReturn(certificates.get(0));
        when(certificateDao.update(certificates.get(0))).thenReturn(certificates.get(1));
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificates.get(0)));
        when(certificateConverter.convertEntityToDto(certificates.get(1))).thenReturn(dtos.get(1));
        GiftCertificateDto certificateDto = service.update(1, dtos.get(0));
        assertEquals(certificateDto, dtos.get(1));
    }

    @Test
    public void updateDuration_ValidCertificateInfo_Success() {
        int duration = 15;
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificates.get(0)));
        doAnswer(invocation -> {
            certificates.get(0).setDuration(duration);
            return null;
        }).when(certificateDao).updateCertificateDuration(any(Long.class), any(Integer.class), any(LocalDateTime.class));
        dtos.get(0).setDuration(duration);
        when(certificateConverter.convertEntityToDto(certificates.get(0))).thenReturn(dtos.get(0));
        GiftCertificateDto resultDto = service.updateCertificateDuration(1, duration);
        assertEquals(resultDto, dtos.get(0));
    }

    @Test
    public void updatePrice_ValidCertificateInfo_Success() {
        BigDecimal price = new BigDecimal(1000);
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificates.get(0)));
        doAnswer(invocation -> {
            certificates.get(0).setPrice(price);
            return null;
        }).when(certificateDao).updateCertificatePrice(any(Long.class), any(BigDecimal.class), any(LocalDateTime.class));
        dtos.get(0).setPrice(price);
        when(certificateConverter.convertEntityToDto(certificates.get(0))).thenReturn(dtos.get(0));
        GiftCertificateDto resultDto = service.updateCertificatePrice(1, price);
        assertEquals(resultDto, dtos.get(0));
    }
}
