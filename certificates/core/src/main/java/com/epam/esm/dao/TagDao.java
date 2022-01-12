package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TagDao extends PagingAndSortingRepository<Tag, Long> {
    @Transactional
    Optional<Tag> findTagByName(String name);
}
