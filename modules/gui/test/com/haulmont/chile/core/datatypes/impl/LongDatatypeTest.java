/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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