package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ValidationException;

import java.util.List;
import java.util.Map;

/**
 * The interface that contains functionality for GiftCertificateService.
 */
public interface GiftCertificateService extends CRDService<GiftCertificateDto> {
    /**
     * Gets all certificate dtos.
     *
     * @param params the parameters of request
     * @return the all gift certificate dtos
     * @throws ServiceException the service exception
     */
    List<GiftCertificateDto> getAll(Map<String, String> params) throws ServiceException;

    /**
     * Updates gift certificate dto.
     *
     * @param id             the id
     * @param certificateDto the certificate dto
     * @return the gift certificate dto
     * @throws ServiceException                 the service exception
     * @throws ResourceNotFoundServiceException the resource not found service exception
     */
    GiftCertificateDto update(long id, GiftCertificateDto certificateDto) throws ServiceException, ResourceNotFoundServiceException, ValidationException;
}
