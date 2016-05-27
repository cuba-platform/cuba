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

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class TimeZoneTest {

    @Test
    public void testTimeZoneConversion() throws Exception {
        Date serverDate, userDate;

        TimeZones timeZones = new TimeZones();

        serverDate = date("2015-01-15 16:30:10");
        userDate = timeZones.convert(serverDate, TimeZones.UTC, TimeZone.getTimeZone("GMT+04"));
        assertEquals(date("2015-01-15 20:30:10"), userDate);

        serverDate = date("2015-01-15 22:30:10");
        userDate = timeZones.convert(serverDate, TimeZones.UTC, TimeZone.getTimeZone("GMT+04"));
        assertEquals(date("2015-01-16 02:30:10"), userDate);

        serverDate = date("2015-01-15 06:30:10");
        userDate = timeZones.convert(serverDate, TimeZones.UTC, TimeZone.getTimeZone("GMT-08"));
        assertEquals(date("2015-01-14 22:30:10"), userDate);
    }

    protected Date date(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.parse(str);
    }
}