/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation.numbers;

import java.math.BigDecimal;

public class DoubleConstraint implements NumberConstraint {

    protected Double value;

    public DoubleConstraint(Double value) {
        this.value = value;
    }

    @Override
    public boolean isMax(long max) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMin(long min) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDigits(int integer, int fraction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDecimalMax(BigDecimal max, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDecimalMin(BigDecimal min, boolean inclusive) {
        throw new UnsupportedOperationException();
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
}
