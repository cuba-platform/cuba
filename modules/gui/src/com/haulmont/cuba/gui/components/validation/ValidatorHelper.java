/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.components.validation;

import com.haulmont.cuba.gui.components.validation.numbers.*;
import com.haulmont.cuba.gui.components.validation.time.AbstractTimeValidator.*;
import com.haulmont.cuba.gui.components.validation.time.TimeValidator;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;

public final class ValidatorHelper {

    @Nullable
    public static NumberConstraint getNumberConstraint(Number value) {
        if (value == null) {
            return null;
        }

        Class clazz = value.getClass();
        if (clazz.equals(Integer.class) || clazz.equals(BigInteger.class)) {
            return new BigIntegerConstraint(BigInteger.valueOf(value.longValue()));
        } else if (clazz.equals(Long.class)) {
            return new LongConstraint(value.longValue());
        } else if (clazz.equals(BigDecimal.class)) {
            return new BigDecimalConstraint((BigDecimal) value);
        } else if (clazz.equals(Double.class)) {
            return new DoubleConstraint(value.doubleValue());
        } else if (clazz.equals(Float.class)) {
            return new FloatConstraint(value.floatValue());
        }
        return null;
    }

    @Nullable
    public static <T> TimeValidator getTimeConstraint(T value) {
        if (value == null) {
            return null;
        }

        Class clazz = value.getClass();
        if (clazz.equals(Date.class)) {
            return new DateConstraint((Date) value);
        } else if (clazz.equals(LocalDate.class)) {
            return new LocalDateConstraint((LocalDate) value);
        } else if (clazz.equals(java.sql.Date.class)) {
            return new LocalDateConstraint(((java.sql.Date) value).toLocalDate());
        } else if (clazz.equals(LocalDateTime.class)) {
            return new LocalDateTimeConstraint((LocalDateTime) value);
        } else if (clazz.equals(LocalTime.class)) {
            return new LocalTimeConstraint((LocalTime) value);
        } else if (clazz.equals(OffsetTime.class)) {
            return new OffsetTimeConstraint((OffsetTime) value);
        } else if (clazz.equals(OffsetDateTime.class)) {
            return new OffsetDateTimeConstraint((OffsetDateTime) value);
        }
        return null;
    }
}
