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

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;


@SuppressWarnings("unchecked")
public class DatatypeFormatterTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private DatatypeFormatter formatter;
    private UserSessionSource uss;

    @Before
    public void setUp() throws Exception {
        formatter = AppBeans.get(DatatypeFormatter.class);
        uss = AppBeans.get(UserSessionSource.class);
    }

    @Test
    public void testFormatDate() throws Exception {
        Date date = new Date();
        String str = formatter.formatDate(date);
        assertEquals(Datatypes.get(DateDatatype.NAME).format(date, uss.getLocale()), str);
    }

    @Test
    public void testParseDate() throws Exception {
        Date date = formatter.parseDate("29/12/2014");
        assertEquals(Datatypes.get(DateDatatype.NAME).parse("29/12/2014", uss.getLocale()), date);
    }
}