package com.company.project.core.validation;

import com.company.project.core.utils.BeanUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shanchao
 * @date 2018-05-22
 */
public class PropPatternValidator implements ConstraintValidator<PropPattern, Object> {

    private String propName;
    private String regex;
    private Pattern pattern;
    private boolean nullable;

    @Override
    public void initialize(PropPattern constraintAnnotation) {
        this.propName = constraintAnnotation.value();
        this.regex = constraintAnnotation.regex();
        this.pattern = Pattern.compile(this.regex);
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable;
        }
        Object propVal = BeanUtil.getProperty(value, propName);
        if (propVal == null) {
            return nullable;
        }
        if (propVal instanceof CharSequence){
            Matcher m = this.pattern.matcher((CharSequence) propVal);
            return m.matches();
        }
        if (propVal instanceof Integer){
            Matcher m = this.pattern.matcher(String.valueOf(propVal));
            return m.matches();
        }
        return false;
    }
}
