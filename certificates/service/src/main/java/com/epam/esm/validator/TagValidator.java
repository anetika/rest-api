package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidatorRegex.TAG_NAME_REGEX;

@Component
public class TagValidator {

    private static final TagValidator instance = new TagValidator();

    private TagValidator(){}

    public static TagValidator getInstance(){
        return instance;
    }

    public void validateTag(TagDto tag){
        validateName(tag.getName());
    }

    private void validateName(String name) {
        Pattern pattern = Pattern.compile(TAG_NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new ValidationException("validation_error.name");
        }
    }
}
