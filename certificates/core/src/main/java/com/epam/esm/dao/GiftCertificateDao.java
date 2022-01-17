package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    void updateCertificateDuration(long id, int duration, LocalDateTime date);
    void updateCertificatePrice(long id, BigDecimal price, LocalDateTime date);
    GiftCertificate save(GiftCertificate certificate);
    Optional<GiftCertificate> findById(long id);
    List<GiftCertificate> findAll(int page, int size);
    void deleteById(long id);
    void deleteAll();
    GiftCertificate update(GiftCertificate certificate);
    List<GiftCertificate> findGiftCertificatesByTags(List<Tag> tags, int page, int size);
}
