package com.epam.esm.util;

import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    public void validatePaginationInfo(int page, int size) {
        if (page < 1 || size < 1) {
            throw new PaginationException("Incorrect pagination input");
        }
    }
}
