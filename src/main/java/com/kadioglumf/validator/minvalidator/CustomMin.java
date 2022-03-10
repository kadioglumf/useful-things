package com.kadioglumf.validator.minvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BigDecimalCustomMinValidator.class}) // diğer subclasslar buraya eklenir.
@Repeatable(CustomMin.List.class)
public @interface CustomMin {

    String message() default "minimum değerden az olamaz";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long value();

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        CustomMin[] value();
    }
}

