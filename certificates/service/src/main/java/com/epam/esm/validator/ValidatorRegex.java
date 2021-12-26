package com.epam.esm.validator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorRegex {
    public static final String CERTIFICATE_NAME_REGEX = "^[A-Za-z0-9_]{3,30}$";
    public static final String CERTIFICATE_DESCRIPTION = "^[\\w\\s,.!?:;\"'-_]+$";
    public static final String TAG_NAME_REGEX = "^[A-Za-z0-9_-]{3,16}$";
}
