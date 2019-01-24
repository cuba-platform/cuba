/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.chile.core.datatypes;

import java.text.ParseException;

/**
 * Exception that can be thrown during value conversion in {@link Datatype}.
 */
public class ValueConversionException extends ParseException {

    public ValueConversionException(String message) {
        super(message, 0);
    }
}
