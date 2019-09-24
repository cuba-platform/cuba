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

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TimeTodayQueryMacroHandlerTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testExpandMacro() throws Exception {
        TimeTodayQueryMacroHandler handler = AppBeans.get(TimeTodayQueryMacroHandler.class);
        String res = handler.expandMacro("select u from sec$User where @today(u.createTs) and u.deleteTs is null");
        handler.setExpandedParamTypes(ImmutableMap.of("u_createTs_1_1", Date.class, "u_createTs_1_2", Date.class));
        System.out.println(res);
        System.out.println(handler.getParams());
    }
}