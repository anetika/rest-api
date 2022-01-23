package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface that contains functionality for GiftCertificateDao
 */
public interface GiftCertificateDao {
    /**
     * Saves a certificate.
     *
     * @param certificate the certificate
     * @return the gift certificate
     */
    GiftCertificate save(GiftCertificate certificate);

    /**
     * Finds a certificate by id.
     *
     * @param id the id
     * @return the optional
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * Finds all certificates.
     *
     * @param page   the page
     * @param size   the size of a page
     * @param params the params of a request
     * @return the list of certificates
     */
    List<GiftCertificate> findAll(int page, int size, Map<String, String> params);

    /**
     * Deletes a certificate by id.
     *
     * @param id the id of a certificate
     */
    void deleteById(long id);

    /**
     * Deletes all certificates.
     */
    void deleteAll();

    /**
     * Updates a certificate.
     *
     * @param certificate the certificate
     * @return the certificate
     */
    GiftCertificate update(GiftCertificate certificate);

    /**
     * Finds gift certificates by tags.
     *
     * @param tags the tags
     * @param page the page
     * @param size the size
     * @return the list of certificates
     */
    List<GiftCertificate> findGiftCertificatesByTags(List<Tag> tags, int page, int size);
}
