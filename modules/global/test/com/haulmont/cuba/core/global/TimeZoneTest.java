/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * @author krivopustov
 * @version $Id$
 */
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
