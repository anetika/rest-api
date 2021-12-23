package com.epam.esm.service;

import com.epam.esm.dto.Dto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.ValidationException;

import java.util.List;

/**
 * The interface that contains functionality for certificates and tags.
 *
 * @param <T> the type parameter
 */
public interface CRDService<T extends Dto> {
    /**
     * Adds an item.
     *
     * @param item certificate or tag
     * @return an item
     * @throws ServiceException the service exception
     */
    T add(T item) throws ServiceException, ValidationException;

    /**
     * Gets an item by id.
     *
     * @param id the id of an item
     * @return the item
     * @throws ServiceException                 the service exception
     * @throws ResourceNotFoundServiceException the resource not found service exception
     */
    T getById(long id) throws ServiceException, ResourceNotFoundServiceException;

    /**
     * Deletes an item by id.
     *
     * @param id the id of an item
     * @throws ServiceException                 the service exception
     * @throws ResourceNotFoundServiceException the resource not found service exception
     */
    void delete(long id) throws ServiceException, ResourceNotFoundServiceException;

    /**
     * Deletes all items.
     *
     * @throws ServiceException the service exception
     */
    void deleteAll() throws ServiceException;
}
