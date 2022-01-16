package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private TagDao tagDao;

    @Mock
    private TagConverter tagConverter;

    @InjectMocks
    private TagServiceImpl tagService;

    private final List<TagDto> tagDtos = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();

    @BeforeEach
    void setUp() {
        TagDto tagDto1 = new TagDto(1, "pet");
        TagDto tagDto2 = new TagDto(2, "cat");
        TagDto tagDto3 = new TagDto(3, "dog");
        tagDtos.add(tagDto1);
        tagDtos.add(tagDto2);
        tagDtos.add(tagDto3);

        Tag tag1 = new Tag(1, "pet");
        Tag tag2 = new Tag(2, "cat");
        Tag tag3 = new Tag(3, "dog");
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
    }

    @Test
    public void add_TagWithValidInfo_Success() {
        TagDto tagDto = new TagDto();
        tagDto.setName("hamster");
        Tag tag = new Tag();
        tag.setName("hamster");
        when(tagConverter.convertDtoToEntity(tagDto)).thenReturn(tag);
        when(tagDao.save(tag)).then(invocation -> {
            tag.setId(1);
            return tag;
        });
        when(tagConverter.convertEntityToDto(tag)).then(invocation -> {
            tagDto.setId(1);
            return tagDto;
        });
        TagDto resultDto = tagService.add(tagDto);
        assertEquals(resultDto, tagDto);
    }

    @Test
    public void getById_ValidId_Success() {
        when(tagDao.findById(1L)).thenReturn(Optional.of(tags.get(0)));
        when(tagConverter.convertEntityToDto(tags.get(0))).thenReturn(tagDtos.get(0));
        TagDto resultDto = tagService.getById(1L);
        assertEquals(resultDto, tagDtos.get(0));
    }

    @Test
    public void getAll_Success() {
        int page = 0;
        int size = 3;
        when(tagDao.findAll(page, size)).thenReturn(tags);
        when(tagConverter.convertEntityToDto(tags.get(0))).thenReturn(tagDtos.get(0));
        when(tagConverter.convertEntityToDto(tags.get(1))).thenReturn(tagDtos.get(1));
        when(tagConverter.convertEntityToDto(tags.get(2))).thenReturn(tagDtos.get(2));
        List<TagDto> resultDtos = tagService.getAll(page, size);
        assertEquals(resultDtos, tagDtos);
    }

    @Test
    public void deleteById_CorrectId_Success() {
        int page = 0;
        int size = 3;
        when(tagDao.findById(1L)).thenReturn(Optional.of(tags.get(0)));
        doAnswer(invocation -> {
            tags.remove(0);
            return null;
        }).when(tagDao).deleteById(1L);
        tagDtos.remove(0);
        tagService.deleteById(1L);
        when(tagDao.findAll(page, size)).thenReturn(tags);
        when(tagConverter.convertEntityToDto(tags.get(0))).thenReturn(tagDtos.get(0));
        when(tagConverter.convertEntityToDto(tags.get(1))).thenReturn(tagDtos.get(1));
        List<TagDto> resultDtos = tagService.getAll(page, size);
        assertEquals(resultDtos, tagDtos);
    }

    @Test
    public void deleteAll_Success() {
        int page = 0;
        int size = 3;
        doAnswer(invocation -> {
            tags.clear();
            return null;
        }).when(tagDao).deleteAll();
        tagService.deleteAll();
        tagDtos.clear();
        when(tagDao.findAll(page, size)).thenReturn(tags);
        assertThrows(ResourceNotFoundException.class, () -> tagService.getAll(page, size));
    }
}
