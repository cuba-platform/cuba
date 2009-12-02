/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 18.05.2009 17:18:45
 *
 * $Id$
 */
package com.haulmont.cuba.core.config.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateStringify extends TypeStringify{

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public String stringify(Object value) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(value);
    }
}
