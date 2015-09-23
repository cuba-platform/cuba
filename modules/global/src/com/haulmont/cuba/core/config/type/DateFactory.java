/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DateFactory extends TypeFactory {
    @Override
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