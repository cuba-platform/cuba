/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 19.05.2009 7:43:16
 *
 * $Id$
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
