package com.company.project.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * @author shanchao
 * @date 2018-05-22
 */
public class PropNotNullValidator implements ConstraintValidator<PropNotNull, Map> {

    private String propName;

    @Override
    public void initialize(PropNotNull constraintAnnotation) {
        this.propName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Map value, ConstraintValidatorContext context) {
        return value != null && value.get(propName) != null;
    }

}
