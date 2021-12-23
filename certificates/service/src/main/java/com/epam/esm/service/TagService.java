package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceException;

import java.util.List;

/**
 * The interface that contains functionality for TagService.
 */
public interface TagService extends CRDService<TagDto> {
    /**
     * Gets all tag dtos.
     *
     * @return all tag dtos
     * @throws ServiceException the service exception
     */
    List<TagDto> getAll() throws ServiceException;

}
