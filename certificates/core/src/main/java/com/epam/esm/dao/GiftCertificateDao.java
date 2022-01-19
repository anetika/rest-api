package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {
    GiftCertificate save(GiftCertificate certificate);
    Optional<GiftCertificate> findById(long id);
    List<GiftCertificate> findAll(int page, int size, Map<String, String> params);
    void deleteById(long id);
    void deleteAll();
    GiftCertificate update(GiftCertificate certificate);
    List<GiftCertificate> findGiftCertificatesByTags(List<Tag> tags, int page, int size);
}
