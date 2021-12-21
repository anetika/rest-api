package com.epam.esm.service;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ActiveProfiles("service")
@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateRepository repository;

    @Mock
    private TagRepository tagRepository;

    private final List<GiftCertificateDto> dtos = new ArrayList<>();

    private final List<GiftCertificate> certificates = new ArrayList<>();

    private static final Map<String, String> map = new HashMap<>();

    @BeforeAll
    public static void setUp(){
        map.put("tag", "");
        map.put("sort", "");
        map.put("search", "");
    }

    @BeforeEach
    public void setUpDtos(){
        GiftCertificateConverter converter = GiftCertificateConverter.getInstance();

        GiftCertificateDto dto1 = new GiftCertificateDto();
        dto1.setId(1);
        dto1.setName("Holiday");
        dto1.setDescription("description 1");
        dto1.setPrice(new BigDecimal(1000));
        dto1.setDuration(10);
        dto1.setCreateDate(LocalDateTime.now());
        dto1.setLastUpdateDate(LocalDateTime.now());
        dto1.setTags(new ArrayList<>());

        GiftCertificateDto dto2 = new GiftCertificateDto();
        dto2.setId(2);
        dto2.setName("Birthday");
        dto2.setDescription("description 2");
        dto2.setPrice(new BigDecimal(2000));
        dto2.setDuration(14);
        dto2.setCreateDate(LocalDateTime.now());
        dto2.setLastUpdateDate(LocalDateTime.now());
        dto2.setTags(new ArrayList<>());

        GiftCertificateDto dto3 = new GiftCertificateDto();
        dto3.setId(3);
        dto3.setName("Pet");
        dto3.setDescription("description 3");
        dto3.setPrice(new BigDecimal(100000));
        dto3.setDuration(100);
        dto3.setCreateDate(LocalDateTime.now());
        dto3.setLastUpdateDate(LocalDateTime.now());
        dto3.setTags(new ArrayList<>());

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
    public void shouldAddCertificateWhenExists() throws RepositoryException, ServiceException {
        when(repository.add(any())).thenReturn(certificates.get(0));
        GiftCertificateDto certificateDto = service.add(dtos.get(0));
        assertEquals(certificateDto, dtos.get(0));
    }

    @Test
    public void shouldGetCertificateByIdWhenExists() throws ResourceNotFoundException, RepositoryException, ServiceException, ResourceNotFoundServiceException {
        when(repository.getById(1)).thenReturn(certificates.get(0));
        when(tagRepository.getAllTagsByCertificateId(any(Long.class))).thenReturn(new ArrayList<>());
        GiftCertificateDto certificateDto = service.getById(1);
        assertEquals(certificateDto, dtos.get(0));
    }

    @Test
    public void shouldGetAllCertificatesWhenExist() throws RepositoryException, ServiceException {
        when(repository.getAll(any())).thenReturn(certificates);
        when(tagRepository.getAllTagsByCertificateId(any(Long.class))).thenReturn(new ArrayList<>());
        List<GiftCertificateDto> list = service.getAll(map);

        assertEquals(list, dtos);
    }

    @Test
    public void shouldDeleteCertificateWhenExists() throws RepositoryException, ServiceException, ResourceNotFoundServiceException {
        doAnswer(invocation -> {
            dtos.remove(0);
            certificates.remove(0);
            return null;
        }).when(repository).delete(1);
        when(repository.getAll(any())).thenReturn(certificates);
        when(tagRepository.getAllTagsByCertificateId(any(Long.class))).thenReturn(new ArrayList<>());
        service.delete(1);
        List<GiftCertificateDto> certificateDtoList = service.getAll(map);
        assertEquals(certificateDtoList, dtos);
    }

    @Test
    public void shouldDeleteAllWhenExist() throws RepositoryException, ServiceException {
        doAnswer(invocation -> {
            dtos.clear();
            return null;
        }).when(repository).deleteAll();
        service.deleteAll();
        assertTrue(dtos.isEmpty());
    }

    @Test
    public void shouldUpdateCertificateWhenExists() throws ResourceNotFoundException, RepositoryException, ServiceException, ResourceNotFoundServiceException {
        when(repository.update(1, certificates.get(0))).thenReturn(certificates.get(1));
        GiftCertificateDto certificateDto = service.update(1, dtos.get(0));
        assertEquals(certificateDto, dtos.get(1));
    }

}
