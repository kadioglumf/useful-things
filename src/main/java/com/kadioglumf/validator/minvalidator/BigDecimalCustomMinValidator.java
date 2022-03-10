package com.kadioglumf.validator.minvalidator;

import java.math.BigDecimal;

public class BigDecimalCustomMinValidator extends AbstractCustomMinValidator<BigDecimal> {

    public BigDecimalCustomMinValidator() {
    }

    @Override
    protected int compare(BigDecimal value) {
        return value.compareTo(BigDecimal.valueOf(this.minValue));
    }
}
