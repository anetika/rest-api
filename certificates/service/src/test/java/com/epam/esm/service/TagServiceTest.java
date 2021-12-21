package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ActiveProfiles("service")
@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @InjectMocks
    private TagServiceImpl service;

    @Mock
    private TagDao tagRepository;

    private final List<TagDto> dtos = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();

    @BeforeEach
    public void setUpDtos() {
        TagConverter converter = TagConverter.getInstance();

        TagDto dto1 = new TagDto();
        dto1.setId(1);
        dto1.setName("winter");

        TagDto dto2 = new TagDto();
        dto2.setId(2);
        dto2.setName("spring");

        TagDto dto3 = new TagDto();
        dto3.setId(3);
        dto3.setName("summer");

        TagDto dto4 = new TagDto();
        dto4.setId(4);
        dto4.setName("autumn");

        dtos.add(dto1);
        dtos.add(dto2);
        dtos.add(dto3);
        dtos.add(dto4);

        Tag tag1 = converter.convertDtoToEntity(dto1);
        Tag tag2 = converter.convertDtoToEntity(dto2);
        Tag tag3 = converter.convertDtoToEntity(dto3);
        Tag tag4 = converter.convertDtoToEntity(dto4);

        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
        tags.add(tag4);
    }

    @Test
    public void shouldAddTagWhenExists() throws RepositoryException, ServiceException, ResourceNotFoundException {
        when(tagRepository.add(any())).thenReturn(tags.get(0));
        when(tagRepository.getByName(any())).thenThrow(ResourceNotFoundException.class);
        TagDto tagDto = service.add(dtos.get(0));
        assertEquals(tagDto, dtos.get(0));
    }

    @Test
    public void shouldGetTagByIdWhenExists() throws ResourceNotFoundException, RepositoryException, ServiceException, ResourceNotFoundServiceException {
        when(tagRepository.getById(1)).thenReturn(tags.get(0));
        TagDto tagDto = service.getById(1);
        assertEquals(tagDto, dtos.get(0));
    }

    @Test
    public void shouldGetAllTagsWhenExists() throws RepositoryException, ServiceException {
        when(tagRepository.getAll()).thenReturn(tags);
        List<TagDto> tagDtoList = service.getAll();
        assertEquals(tagDtoList, dtos);
    }

    @Test
    public void shouldDeleteTagByIdWhenExists() throws RepositoryException, ServiceException, ResourceNotFoundServiceException {
        doAnswer(invocation -> {
            dtos.remove(0);
            tags.remove(0);
            return null;
        }).when(tagRepository).delete(1);
        when(tagRepository.getAll()).thenReturn(tags);
        service.delete(1);
        List<TagDto> tagDtoList = service.getAll();
        assertEquals(tagDtoList, dtos);
    }

    @Test
    public void shouldDeleteAllTagsWhenExists() throws RepositoryException, ServiceException {
        doAnswer(invocation -> {
            dtos.clear();
            tags.clear();
            return null;
        }).when(tagRepository).deleteAll();
        when(tagRepository.getAll()).thenReturn(tags);
        service.deleteAll();
        List<TagDto> tagDtoList = service.getAll();
        assertEquals(tagDtoList, dtos);
    }
}
