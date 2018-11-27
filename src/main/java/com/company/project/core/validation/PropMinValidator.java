package com.company.project.core.validation;

import com.company.project.core.utils.BeanUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author shanchao
 * @date 2018-05-22
 */
public class PropMinValidator implements ConstraintValidator<PropMin, Object> {

    private String propName;
    private long min;
    private boolean nullable;

    @Override
    public void initialize(PropMin constraintAnnotation) {
        this.propName = constraintAnnotation.value();
        this.min = constraintAnnotation.min();
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
        if (propVal instanceof Number) {
            return ((Number) propVal).longValue() >= min;
        } else if (propVal instanceof String) {
            Double d = Double.parseDouble(propVal.toString());
            return d.longValue() >= min;
        }
        return false;
    }

}
