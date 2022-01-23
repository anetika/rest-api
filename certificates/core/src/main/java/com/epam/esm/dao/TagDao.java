package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface that contains functionality for TagDao
 */
@Repository
public interface TagDao {
    /**
     * Finds a tag by name.
     *
     * @param name the name of the tag
     * @return the tag
     */
    Optional<Tag> findTagByName(String name);

    /**
     * Finds a tag by id.
     *
     * @param id the id of the tag
     * @return the tag
     */
    Optional<Tag> findById(long id);

    /**
     * Finds all tags.
     *
     * @param page the page
     * @param size the size
     * @return the list of all tags
     */
    List<Tag> findAll(int page, int size);

    /**
     * Saves a tag.
     *
     * @param tag the tag
     * @return the tag
     */
    Tag save(Tag tag);

    /**
     * Deletes by id.
     *
     * @param id the id of the tag
     */
    void deleteById(long id);

    /**
     * Deletes all tags.
     */
    void deleteAll();
}
