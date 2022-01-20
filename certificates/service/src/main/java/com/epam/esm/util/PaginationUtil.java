package com.epam.esm.util;

import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Component;

/**
 * The util for validating page values.
 */
@Component
public class PaginationUtil {

    /**
     * Validates pagination info.
     *
     * @param page the page
     * @param size the size
     */
    public void validatePaginationInfo(int page, int size) {
        if (page < 1 || size < 1) {
            throw new PaginationException("Incorrect pagination input");
        }
    }
}
