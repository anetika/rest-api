package com.epam.esm.dao;

import com.epam.esm.configuration.TestCoreConfiguration;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = TestCoreConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
public class GiftCertificateDaoTest {
    @Autowired
    GiftCertificateDao repository;

    @Test
    public void shouldAddCertificateWhenExists() throws RepositoryException {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "food"));
        GiftCertificate certificate = new GiftCertificate(0, "activities", "Good leisure",
                new BigDecimal(1000), 14, LocalDateTime.parse("2005-08-09T18:31:42"),
                LocalDateTime.parse("2005-08-09T18:31:42"), tags);
        GiftCertificate result  = repository.add(certificate);
        certificate.setId(2);
        assertEquals(certificate, result);
    }

    @Test
    public void shouldGetCertificateByIdWhenExists() throws RepositoryException, ResourceNotFoundException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setName("Restaurant");
        certificate.setDescription("The best dishes");
        certificate.setPrice(new BigDecimal(1000));
        certificate.setDuration(14);
        certificate.setCreateDate(LocalDateTime.parse("2005-08-09T18:31:42"));
        certificate.setLastUpdateDate(LocalDateTime.parse("2005-08-09T18:31:42"));
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "food"));
        certificate.setTags(tags);
        GiftCertificate result = repository.getById(1);
        result.setTags(tags);
        assertEquals(certificate, result);
    }

    @Test
    public void shouldGetAllCertificatesWhenExist() throws RepositoryException {
        List<GiftCertificate> certificates = new ArrayList<>();

        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setName("Restaurant");
        certificate.setDescription("The best dishes");
        certificate.setPrice(new BigDecimal(1000));
        certificate.setDuration(14);
        certificate.setCreateDate(LocalDateTime.parse("2005-08-09T18:31:42"));
        certificate.setLastUpdateDate(LocalDateTime.parse("2005-08-09T18:31:42"));
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "food"));
        certificate.setTags(tags);
        certificates.add(certificate);
        Map<String, String> map = new HashMap<>();
        map.put("tag", "");
        map.put("search", "");
        map.put("sort", "");
        List<GiftCertificate> result = repository.getAll(map);
        result.get(0).setTags(tags);
        assertEquals(certificates, result);
    }

    @Test
    public void shouldDeleteCertificateByIdWhenExists() throws RepositoryException {
        repository.delete(1);
        Map<String, String> map = new HashMap<>();
        map.put("tag", "");
        map.put("search", "");
        map.put("sort", "");
        assertTrue(repository.getAll(map).isEmpty());
    }

    @Test
    public void shouldDeleteAllCertificatesWhenExist()throws RepositoryException {
        repository.deleteAll();
        Map<String, String> map = new HashMap<>();
        map.put("tag", "");
        map.put("search", "");
        map.put("sort", "");
        assertTrue(repository.getAll(map).isEmpty());
    }

    @Test
    public void shouldUpdateCertificateByIdWhenExists() throws RepositoryException, ResourceNotFoundException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setName("Restaurant");
        certificate.setDescription("The best dishes");
        certificate.setPrice(new BigDecimal(1000));
        certificate.setDuration(14);
        certificate.setCreateDate(LocalDateTime.parse("2005-08-09T18:31:42"));
        certificate.setLastUpdateDate(LocalDateTime.parse("2005-08-09T18:31:42"));
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "food"));
        certificate.setTags(tags);
        GiftCertificate updatedCertificate = repository.update(1, certificate);
        assertEquals(certificate, updatedCertificate);
    }
}
