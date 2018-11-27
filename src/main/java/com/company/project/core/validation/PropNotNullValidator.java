package com.company.project.core.validation;

import com.company.project.core.utils.BeanUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author shanchao
 * @date 2018-05-22
 */
public class PropNotNullValidator implements ConstraintValidator<PropNotNull, Object> {

    private String propName;

    @Override
    public void initialize(PropNotNull constraintAnnotation) {
        this.propName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value != null && BeanUtil.getProperty(value, propName) != null;
    }

}
