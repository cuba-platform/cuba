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
package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class DateEqualsMacroHandlerTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testExpandMacro() throws Exception {
        Date date = new Date();
        DateEqualsMacroHandler handler = AppBeans.get(DateEqualsMacroHandler.class);
        String res = handler.expandMacro("select distinct t from tm$Task t where t.id <> :param_exclItem  " +
                "and @dateEquals(t.createTs, :component_genericFilter_nLxPpRlkOq29857)     order by t.num desc");
        handler.setQueryParams(Collections.<String, Object>singletonMap("component_genericFilter_nLxPpRlkOq29857", date));
        Map<String,Object> params = handler.getParams();

        System.out.println(res);
        System.out.println(params);

        Assertions.assertEquals("select distinct t from tm$Task t where t.id <> :param_exclItem  and (t.createTs >= :component_genericFilter_nLxPpRlkOq29857 and t.createTs < :t_createTs_1)     order by t.num desc", res);
        Assertions.assertEquals(DateUtils.truncate(date, Calendar.DAY_OF_MONTH), params.get("component_genericFilter_nLxPpRlkOq29857"));
        Assertions.assertEquals(DateUtils.addDays(DateUtils.truncate(date, Calendar.DAY_OF_MONTH), 1), params.get("t_createTs_1"));
    }
}
