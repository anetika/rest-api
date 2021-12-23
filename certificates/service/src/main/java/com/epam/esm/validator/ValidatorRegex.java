package com.epam.esm.validator;

public class ValidatorRegex {
    public static final String CERTIFICATE_NAME_REGEX = "^[A-Za-z0-9_-]{3,16}$";
    public static final String CERTIFICATE_DESCRIPTION = "^[\\w\\s,.!?:;\"'-_]+$";
    public static final String CERTIFICATE_NUMBER = "^(0|[-\\+]?[1-9][0-9]*)$";
    public static final String TAG_NAME_REGEX = "^[A-Za-z0-9_-]{3,16}$";

    private ValidatorRegex() {}
}
