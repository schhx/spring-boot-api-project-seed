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
@Constraint(validatedBy = PropMaxValidator.class)
public @interface PropMax {
    String message() default "{value}不能大于{max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    long max();

    boolean nullable() default true;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @interface List {
        PropMax[] value();
    }

}
