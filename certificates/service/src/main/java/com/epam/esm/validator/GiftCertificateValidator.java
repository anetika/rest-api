package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ValidationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidatorRegex.*;

public class GiftCertificateValidator {

    private final TagValidator tagValidator;

    private static final GiftCertificateValidator instance = new GiftCertificateValidator();

    private GiftCertificateValidator(){
        tagValidator = TagValidator.getInstance();
    }

    public static GiftCertificateValidator getInstance(){
        return instance;
    }

    public void validateUpdatedGiftCertificate(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getName() != null) {
            validateName(giftCertificateDto.getName());
        }
        if (giftCertificateDto.getDescription() != null) {
            validateDescription(giftCertificateDto.getDescription());
        }
        if (giftCertificateDto.getPrice() != null) {
            validatePrice(giftCertificateDto.getPrice());
        }
    }

    public void validateGiftCertificate(GiftCertificateDto giftCertificateDto) {
        validateName(giftCertificateDto.getName());
        validateDescription(giftCertificateDto.getDescription());
        validatePrice(giftCertificateDto.getPrice());
        validateDuration(giftCertificateDto.getDuration());
        validateTags(giftCertificateDto.getTags());
    }

    private void validateName(String name) {
        Pattern pattern = Pattern.compile(CERTIFICATE_NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new ValidationException("validation_error.name");
        }
    }

    private void validateDescription(String description) {
        Pattern pattern = Pattern.compile(CERTIFICATE_DESCRIPTION);
        Matcher matcher = pattern.matcher(description);
        if (!matcher.matches()) {
            throw new ValidationException("validation_error.description");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal("0")) <= 0) {
            throw new ValidationException("validation_error.price");
        }
    }

    private void validateDuration(int duration) {
        if (duration <= 0) {
            throw new ValidationException("validation_error.duration");
        }
    }

    private void validateTags(List<TagDto> tags){
        tags.forEach(tagValidator::validateTag);
    }
}
