/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.config.type;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

public class DateFactory extends TypeFactory {
    public Object build(String string) {
        if (string == null)
            return null;

        DateFormat df = new SimpleDateFormat(DateStringify.DATE_FORMAT);
        try {
            return df.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
