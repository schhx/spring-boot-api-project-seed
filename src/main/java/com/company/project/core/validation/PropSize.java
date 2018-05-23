package com.company.project.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author shanchao
 * @date 2018-05-22
 */
@Target({METHOD, PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PropSizeValidator.class)
public @interface PropSize {
    String message() default "{value}取值范围只能是{min} - {max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    boolean nullable() default true;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @interface List {
        PropSize[] value();
    }

}
