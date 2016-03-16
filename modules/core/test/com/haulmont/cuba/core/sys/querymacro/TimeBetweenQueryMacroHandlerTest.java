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

import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TimeBetweenQueryMacroHandlerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testExpandMacro() throws Exception {
        TimeBetweenQueryMacroHandler handler = new TimeBetweenQueryMacroHandler();
        String res = handler.expandMacro("select u from sec$User where @between(u.createTs, now, now+1, day) and u.deleteTs is null");
        System.out.println(res);
        System.out.println(handler.getParams());

        handler = new TimeBetweenQueryMacroHandler();
        res = handler.expandMacro("select u from sec$User " +
                "where @between(u.createTs, now-10, now+1, minute) or @between(u.createTs, now-20, now-10, minute)" +
                " and u.deleteTs is null");
        System.out.println(res);
        System.out.println(handler.getParams());

        handler = new TimeBetweenQueryMacroHandler();
        res = handler.expandMacro("select u from sec$User where @between(u.createTs, now-5+2, now, day) and u.deleteTs is null");
        System.out.println(res);
        System.out.println(handler.getParams());
    }

    @Test
    public void testReplaceQueryParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", 5);
        TimeBetweenQueryMacroHandler handler = new TimeBetweenQueryMacroHandler();
        String res = handler.replaceQueryParams("select u from sec$User where @between(u.createTs, now, now+:param1, day) and u.deleteTs is null", params);
        System.out.println(res);
    }
}
