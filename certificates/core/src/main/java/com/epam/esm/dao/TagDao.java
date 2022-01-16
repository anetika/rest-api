package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao {
    Optional<Tag> findTagByName(String name);
    Optional<Tag> findById(long id);
    List<Tag> findAll(int page, int size);
    Tag save(Tag tag);
    void deleteById(long id);
    void deleteAll();
}
