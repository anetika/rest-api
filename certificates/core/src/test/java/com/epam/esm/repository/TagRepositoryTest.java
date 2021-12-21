package com.epam.esm.repository;

import com.epam.esm.configuration.TestCoreConfiguration;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = TestCoreConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("dev")
@ExtendWith(SpringExtension.class)
public class TagRepositoryTest {

    @Autowired
    private TagRepository repository;

    @Test
    public void shouldAddTagWhenExists() throws RepositoryException {
        Tag tag = new Tag();
        tag.setName("pet");
        Tag actual = repository.add(tag);
        tag.setId(5);
        assertEquals(actual, tag);
    }

    @Test
    public void shouldGetByIdWhenExists() throws RepositoryException, ResourceNotFoundException {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("food");
        Tag actual = repository.getById(1);
        assertEquals(tag, actual);
    }

    @Test
    public void shouldGetAllTagsWhenExist() throws RepositoryException {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "food"));
        tags.add(new Tag(2, "clothes"));
        tags.add(new Tag(3, "animals"));
        tags.add(new Tag(4, "entertainment"));
        List<Tag> actualTagList = repository.getAll();
        assertEquals(tags, actualTagList);
    }

    @Test
    public void shouldDeleteTagByIdWhenExists() throws RepositoryException {
        repository.delete(4);
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1, "food"));
        tags.add(new Tag(2, "clothes"));
        tags.add(new Tag(3, "animals"));
        List<Tag> actualTagList = repository.getAll();
        assertEquals(tags, actualTagList);
    }

    @Test
    public void shouldDeleteAllTagsWhenExist() throws RepositoryException {
        repository.deleteAll();
        List<Tag> tags = new ArrayList<>();
        List<Tag> actualTagList = repository.getAll();
        assertEquals(tags, actualTagList);
    }
}
