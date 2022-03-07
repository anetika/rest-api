package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface that contains functionality for TagDao
 */
@Repository
public interface TagDao extends JpaRepository<Tag, Long> {
    /**
     * Finds a tag by name.
     *
     * @param name the name of the tag
     * @return the tag
     */
    Optional<Tag> findTagByName(String name);
}
