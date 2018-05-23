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
@Constraint(validatedBy = PropPatternValidator.class)
public @interface PropPattern {
    String message() default "{value}格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    String regex();

    boolean nullable() default true;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @interface List {
        PropPattern[] value();
    }

}
