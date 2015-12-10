/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

public class TimeTodayQueryMacroHandlerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void testExpandMacro() throws Exception {
        TimeTodayQueryMacroHandler handler = new TimeTodayQueryMacroHandler();
        String res = handler.expandMacro("select u from sec$User where @today(u.createTs) and u.deleteTs is null");
        System.out.println(res);
        System.out.println(handler.getParams());
    }
}