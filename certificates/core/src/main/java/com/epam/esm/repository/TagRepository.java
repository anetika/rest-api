package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;

/**
 * The interface that contains functionality for TagRepository
 */
public interface TagRepository extends CRDRepository<Tag>{
    /**
     * Gets tag by name.
     *
     * @param name the name of tag
     * @return the tag
     * @throws RepositoryException       the repository exception
     * @throws ResourceNotFoundException the resource not found exception
     */
    Tag getByName(String name) throws RepositoryException, ResourceNotFoundException;

    /**
     * Gets all tags by certificate id.
     *
     * @param id the id of certificate
     * @return all tags by certificate id
     * @throws RepositoryException the repository exception
     */
    List<Tag> getAllTagsByCertificateId(long id) throws RepositoryException;

    /**
     * Gets all tags.
     *
     * @return all tags
     * @throws RepositoryException the repository exception
     */
    List<Tag> getAll() throws RepositoryException;
}
