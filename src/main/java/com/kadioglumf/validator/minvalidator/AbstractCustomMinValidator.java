package com.kadioglumf.validator.minvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractCustomMinValidator<T> implements ConstraintValidator<CustomMin, T> {

    protected long minValue;

    public AbstractCustomMinValidator() {
    }

    @Override
    public void initialize(CustomMin maxValue) {
        this.minValue = maxValue.value();
    }

    @Override
    public boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext) {
        if (t == null) {
            return true;
        } else {
            return this.compare(t) >= 0;
        }
    }

    protected abstract int compare(T value);
}

