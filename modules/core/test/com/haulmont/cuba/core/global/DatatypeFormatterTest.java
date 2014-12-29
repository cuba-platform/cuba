/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.core.CubaTestCase;

import java.util.Date;


@SuppressWarnings("unchecked")
public class DatatypeFormatterTest extends CubaTestCase {

    private DatatypeFormatter formatter;
    private UserSessionSource uss;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        formatter = AppBeans.get(DatatypeFormatter.class);
        uss = AppBeans.get(UserSessionSource.class);
    }

    public void testFormatDate() throws Exception {
        Date date = new Date();
        String str = formatter.formatDate(date);
        assertEquals(Datatypes.get(DateDatatype.NAME).format(date, uss.getLocale()), str);
    }

    public void testParseDate() throws Exception {
        Date date = formatter.parseDate("29/12/2014");
        assertEquals(Datatypes.get(DateDatatype.NAME).parse("29/12/2014", uss.getLocale()), date);
    }
}