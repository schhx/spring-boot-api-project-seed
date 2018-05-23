package com.company.project.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author shanchao
 * @date 2018-05-22
 */
public class PropNotEmptyValidator implements ConstraintValidator<PropNotEmpty, Map> {

    private String propName;

    @Override
    public void initialize(PropNotEmpty constraintAnnotation) {
        this.propName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Map value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Object propVal = value.get(propName);
        if (propVal == null) {
            return false;
        }
        if (propVal instanceof Number) {
            return true;
        }
        if (propVal instanceof CharSequence) {
            return ((CharSequence) propVal).length() > 0;
        } else if (propVal instanceof Collection) {
            return ((Collection) propVal).size() > 0;
        } else if (propVal instanceof Map) {
            return ((Map) propVal).size() > 0;
        } else if (propVal.getClass().isArray()) {
            return Array.getLength(propVal) > 0;
        }
        return false;
    }

}
