package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

public interface GiftCertificateUtilDao {
    List<GiftCertificate> findGiftCertificatesByTags(List<Tag> tags);
}
