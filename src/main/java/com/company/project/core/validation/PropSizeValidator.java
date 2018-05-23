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
public class PropSizeValidator implements ConstraintValidator<PropSize, Map> {

    private String propName;
    private int min;
    private int max;
    private boolean nullable;

    @Override
    public void initialize(PropSize constraintAnnotation) {
        this.propName = constraintAnnotation.value();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(Map value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable;
        }
        Object propVal = value.get(propName);
        if (propVal == null) {
            return nullable;
        }
        int size = 0;
        if (propVal instanceof CharSequence) {
            size = ((CharSequence) propVal).length();
        } else if (propVal instanceof Collection) {
            size = ((Collection) propVal).size();
        } else if (propVal instanceof Map) {
            size = ((Map) propVal).size();
        } else if (propVal.getClass().isArray()) {
            size = Array.getLength(propVal);
        } else {
            return false;
        }
        return size >= min && size <= max;
    }

}
