package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * The interface that contains functionality for GiftCertificateRepository
 */
public interface GiftCertificateRepository extends CRDRepository<GiftCertificate> {
    /**
     * Updates gift certificate.
     *
     * @param id                 the id of updatable certificate
     * @param updatedCertificate the updated certificate
     * @return the gift certificate
     * @throws RepositoryException       the repository exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    GiftCertificate update(long id, GiftCertificate updatedCertificate) throws RepositoryException, ResourceNotFoundException;

    /**
     * Adds connection between certificate and tag.
     *
     * @param certificateId the certificate id
     * @param tagId         the tag id
     * @throws RepositoryException the repository exception
     */
    void addGiftCertificateTagConnection(long certificateId, long tagId) throws RepositoryException;

    /**
     * Gets all certificates that exist
     *
     * @param params the parameters of request
     * @return the list of certificates
     * @throws RepositoryException the repository exception
     */
    List<GiftCertificate> getAll(Map<String, String> params) throws RepositoryException;
}
