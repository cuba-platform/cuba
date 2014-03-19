/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DateStringify extends TypeStringify {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public String stringify(Object value) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(value);
    }
}