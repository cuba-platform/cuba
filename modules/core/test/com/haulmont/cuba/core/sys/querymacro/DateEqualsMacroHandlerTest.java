/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.querymacro;

import junit.framework.TestCase;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class DateEqualsMacroHandlerTest extends TestCase {

    public void testExpandMacro() throws Exception {
        Date date = new Date();
        DateEqualsMacroHandler handler = new DateEqualsMacroHandler();
        String res = handler.expandMacro("select distinct t from tm$Task t where t.id <> :param_exclItem  " +
                "and @dateEquals(t.createTs, :component_genericFilter_nLxPpRlkOq29857)     order by t.num desc");
        handler.setQueryParams(Collections.<String, Object>singletonMap("component_genericFilter_nLxPpRlkOq29857", date));
        Map<String,Object> params = handler.getParams();

        System.out.println(res);
        System.out.println(params);

        assertEquals("select distinct t from tm$Task t where t.id <> :param_exclItem  and (t.createTs >= :component_genericFilter_nLxPpRlkOq29857 and t.createTs < :t_createTs_1)     order by t.num desc", res);
        assertEquals(DateUtils.truncate(date, Calendar.DAY_OF_MONTH), params.get("component_genericFilter_nLxPpRlkOq29857"));
        assertEquals(DateUtils.addDays(DateUtils.truncate(date, Calendar.DAY_OF_MONTH), 1), params.get("t_createTs_1"));
    }
}
