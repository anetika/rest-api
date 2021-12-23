package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;

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

    public boolean validateGiftCertificate(GiftCertificateDto giftCertificateDto) {
        return validateName(giftCertificateDto.getName())
                && validateDescription(giftCertificateDto.getDescription())
                && validatePrice(giftCertificateDto.getPrice())
                && validateDuration(giftCertificateDto.getDuration())
                && validateTags(giftCertificateDto.getTags());
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile(CERTIFICATE_NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean validateDescription(String description) {
        Pattern pattern = Pattern.compile(CERTIFICATE_DESCRIPTION);
        Matcher matcher = pattern.matcher(description);
        return matcher.matches();
    }

    private boolean validatePrice(BigDecimal price) {
        Pattern pattern = Pattern.compile(CERTIFICATE_NUMBER);
        Matcher matcher = pattern.matcher(price.toString());
        return matcher.matches();
    }

    private boolean validateDuration(int duration) {
        Pattern pattern = Pattern.compile(CERTIFICATE_NUMBER);
        Matcher matcher = pattern.matcher(Integer.toString(duration));
        return matcher.matches();
    }

    private boolean validateTags(List<TagDto> tags){
        return tags.stream().allMatch(tagValidator::validateTag);
    }
}
