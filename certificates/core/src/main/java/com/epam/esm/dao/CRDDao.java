package com.epam.esm.dao;


import com.epam.esm.entity.Entity;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;

/**
 * The interface that contains functionality for Gift Certificate Repository and Tag Repository
 *
 * @param <T> the type parameter
 */
public interface CRDDao<T extends Entity> {
    /**
     * Adds an item to repository
     *
     * @param item certificate or tag
     * @return the item
     * @throws RepositoryException the repository exception
     */
    T add(T item) throws RepositoryException;

    /**
     * Gets an item by id.
     *
     * @param id the id of an item
     * @return the item by id
     * @throws RepositoryException       the repository exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    T getById(long id) throws RepositoryException, ResourceNotFoundException;

    /**
     * Deletes the item by id.
     *
     * @param id the id of an item
     * @throws RepositoryException the repository exception
     */
    void delete(long id) throws RepositoryException;

    /**
     * Deletes all items.
     *
     * @throws RepositoryException the repository exception
     */
    void deleteAll() throws RepositoryException;
}
