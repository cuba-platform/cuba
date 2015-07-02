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
public class IntegerDatatypeTest extends AbstractDatatypeTest {

    private Datatype<Integer> intDt;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        intDt = Datatypes.getNN(Integer.class);
    }

    @Test
    public void parseValid() throws ParseException {
        assertEquals((Integer) 10, intDt.parse("10"));
        assertEquals(null, intDt.parse(""));
        assertEquals((Integer) 10000, intDt.parse("10000"));
    }

    @Test
    public void format() throws ParseException {
        assertEquals("10", intDt.format(10));
        assertEquals("", intDt.format(null));
    }

    @Test
    public void parseLocaleRu() throws ParseException {
        assertEquals((Integer) 10, intDt.parse("10", ruLocale));
        assertEquals(null, intDt.parse("", ruLocale));
        assertEquals((Integer) 10000, intDt.parse("10000", ruLocale));
        assertEquals((Integer) 10000, intDt.parse("10 000", ruLocale));
    }

    @Test
    public void parseLocaleEn() throws ParseException {
        assertEquals((Integer) 10, intDt.parse("10", enGbLocale));
        assertEquals(null, intDt.parse("", enGbLocale));
        assertEquals((Integer) 10000, intDt.parse("10000", enGbLocale));
        assertEquals((Integer) 10000, intDt.parse("10,000", enGbLocale));
    }

    @Test
    public void parseLocaleUnknown() throws ParseException {
        assertEquals((Integer) 10, intDt.parse("10", Locale.FRENCH));
        assertEquals(null, intDt.parse("", Locale.FRENCH));
        assertEquals((Integer) 10000, intDt.parse("10000", Locale.FRENCH));
    }

    @Test
    public void formatRu() throws ParseException {
        assertEquals("10", intDt.format(10, ruLocale));
        assertEquals("10 000", intDt.format(10000, ruLocale));
        assertEquals("", intDt.format(null, ruLocale));
    }

    @Test
    public void formatEn() throws ParseException {
        assertEquals("10", intDt.format(10, enGbLocale));
        assertEquals("10,000", intDt.format(10000, enGbLocale));
        assertEquals("", intDt.format(null, enGbLocale));
    }

    @Test
    public void formatUnknown() throws ParseException {
        assertEquals(intDt.format(10), intDt.format(10, Locale.FRENCH));
        assertEquals(intDt.format(10000), intDt.format(10000, Locale.FRENCH));
        assertEquals(intDt.format(null), intDt.format(null, Locale.FRENCH));
    }

    @Test(expected = ParseException.class)
    public void parseDouble() throws ParseException {
        intDt.parse("12.1");
    }

    @Test(expected = ParseException.class)
    public void parseRoundedDouble() throws ParseException {
        intDt.parse("12.0");
    }

    @Test(expected = ParseException.class)
    public void parseLowerThanMIN() throws ParseException {
        intDt.parse("-1000000000000");
    }

    @Test(expected = ParseException.class)
    public void parseGreaterThanMAX() throws ParseException {
        intDt.parse("1000000000000");
    }

    @Test(expected = ParseException.class)
    public void parseLowerThanMINLong() throws ParseException {
        intDt.parse("-1000000000000000000000");
    }

    @Test(expected = ParseException.class)
    public void parseGreaterThanMAXLong() throws ParseException {
        intDt.parse("1000000000000000000000");
    }
}