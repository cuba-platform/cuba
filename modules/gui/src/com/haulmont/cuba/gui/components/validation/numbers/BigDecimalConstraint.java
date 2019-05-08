/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation.numbers;

import java.math.BigDecimal;

public class BigDecimalConstraint implements NumberConstraint {

    protected BigDecimal value;

    public BigDecimalConstraint(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean isMax(long max) {
        return compareValueWith(max) <= 0;
    }

    @Override
    public boolean isMin(long min) {
        return compareValueWith(min) >= 0;
    }

    @Override
    public boolean isDigits(int integer, int fraction) {
        BigDecimal bigDecimal = value.stripTrailingZeros();

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
        return value.signum() <= 0;
    }

    @Override
    public boolean isNegative() {
        return value.signum() < 0;
    }

    @Override
    public boolean isPositiveOrZero() {
        return value.signum() >= 0;
    }

    @Override
    public boolean isPositive() {
        return value.signum() > 0;
    }

    protected int compareValueWith(long val) {
        return this.value.compareTo(BigDecimal.valueOf(val));
    }

    private int compareValueWith(BigDecimal val) {
        return this.value.compareTo(val);
    }
}
