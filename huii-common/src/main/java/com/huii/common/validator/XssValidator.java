package com.huii.common.validator;

import com.huii.common.annotation.Xss;
import com.huii.common.constants.RegConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssValidator implements ConstraintValidator<Xss, String> {

    private static final String HTML_PATTERN = RegConstants.XSS_MATCHER;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
