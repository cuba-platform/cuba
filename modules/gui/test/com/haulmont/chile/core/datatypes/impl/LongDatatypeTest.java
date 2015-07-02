/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;

/**
 * @author artamonov
 * @version $Id$
 */
public class LongDatatypeTest extends AbstractDatatypeTest {

    private Datatype<Long> longDt;
    private Long long10000 = (long) 10000;
    private Long long10 = (long) 10;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        longDt = Datatypes.getNN(Long.class);
    }

    @Test
    public void parseValid() throws ParseException {
        assertEquals(long10, longDt.parse("10"));
        assertEquals(null, longDt.parse(""));
        assertEquals(long10000, longDt.parse("10000"));
    }

    @Test
    public void format() throws ParseException {
        assertEquals("10", longDt.format((long) 10));
        assertEquals("", longDt.format(null));
    }

    @Test
    public void parseLocaleRu() throws ParseException {
        assertEquals(long10, longDt.parse("10", ruLocale));
        assertEquals(null, longDt.parse("", ruLocale));
        assertEquals(long10000, longDt.parse("10000", ruLocale));
        assertEquals(long10000, longDt.parse("10 000", ruLocale));
    }

    @Test
    public void parseLocaleEn() throws ParseException {
        assertEquals(long10, longDt.parse("10", enGbLocale));
        assertEquals(null, longDt.parse("", enGbLocale));
        assertEquals(long10000, longDt.parse("10000", enGbLocale));
        assertEquals(long10000, longDt.parse("10,000", enGbLocale));
    }

    @Test
    public void parseLocaleUnknown() throws ParseException {
        assertEquals(long10, longDt.parse("10", Locale.FRENCH));
        assertEquals(null, longDt.parse("", Locale.FRENCH));
        assertEquals(long10000, longDt.parse("10000", Locale.FRENCH));
    }

    @Test
    public void formatRu() throws ParseException {
        assertEquals("10", longDt.format(long10, ruLocale));
        assertEquals("10 000", longDt.format(long10000, ruLocale));
        assertEquals("", longDt.format(null, ruLocale));
    }

    @Test
    public void formatEn() throws ParseException {
        assertEquals("10", longDt.format(long10, enGbLocale));
        assertEquals("10,000", longDt.format(long10000, enGbLocale));
        assertEquals("", longDt.format(null, enGbLocale));
    }

    @Test
    public void formatUnknown() throws ParseException {
        assertEquals(longDt.format(long10), longDt.format(long10, Locale.FRENCH));
        assertEquals(longDt.format(long10000), longDt.format(long10000, Locale.FRENCH));
        assertEquals(longDt.format(null), longDt.format(null, Locale.FRENCH));
    }

    @Test(expected = ParseException.class)
    public void parseDouble() throws ParseException {
        longDt.parse("12.1");
    }

    @Test(expected = ParseException.class)
    public void parseRoundedDouble() throws ParseException {
        longDt.parse("12.0");
    }

    @Test(expected = ParseException.class)
    public void parseLowerThanMIN() throws ParseException {
        longDt.parse("-1000000000000000000000");
    }

    @Test(expected = ParseException.class)
    public void parseGreaterThanMAX() throws ParseException {
        longDt.parse("1000000000000000000000");
    }
}