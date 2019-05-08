/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation.numbers;

import java.math.BigDecimal;

public class LongConstraint implements NumberConstraint {

    protected Long value;
    protected BigDecimal bigDecimalValue;

    public LongConstraint(Long value) {
        this.value = value;
        this.bigDecimalValue = new BigDecimal(value);
    }

    @Override
    public boolean isMax(long max) {
        return value <= max;
    }

    @Override
    public boolean isMin(long min) {
        return value >= min;
    }

    @Override
    public boolean isDigits(int integer, int fraction) {
        BigDecimal bigDecimal = new BigDecimal(value).stripTrailingZeros();

        int integerLength = bigDecimal.precision() - bigDecimal.scale();
        int fractionLength = bigDecimal.scale() < 0 ? 0 : bigDecimal.scale();

        return integer >= integerLength && fraction >= fractionLength;
    }

    @Override
    public boolean isDecimalMax(BigDecimal max, boolean inclusive) {
        if (inclusive) {
            return compareValueWith(max) <= 0;
        } else {
            return compareValueWith(max) < 0;
        }
    }

    @Override
    public boolean isDecimalMin(BigDecimal min, boolean inclusive) {
        if (inclusive) {
            return compareValueWith(min) >= 0;
        } else {
            return compareValueWith(min) > 0;
        }
    }

    @Override
    public boolean isNegativeOrZero() {
        return value <= 0;
    }

    @Override
    public boolean isNegative() {
        return value < 0;
    }

    @Override
    public boolean isPositiveOrZero() {
        return value >= 0;
    }

    @Override
    public boolean isPositive() {
        return value > 0;
    }

    protected int compareValueWith(BigDecimal val) {
        return bigDecimalValue.compareTo(val);
    }
}
