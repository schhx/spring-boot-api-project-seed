package com.company.project.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * @author shanchao
 * @date 2018-05-22
 */
public class PropMaxValidator implements ConstraintValidator<PropMax, Map> {

    private String propName;
    private long max;
    private boolean nullable;

    @Override
    public void initialize(PropMax constraintAnnotation) {
        this.propName = constraintAnnotation.value();
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
        if (propVal instanceof Number) {
            return ((Number) propVal).longValue() <= max;
        } else if (propVal instanceof String) {
            Double d = Double.parseDouble(propVal.toString());
            return d.longValue() <= max;
        }
        return false;
    }

}
