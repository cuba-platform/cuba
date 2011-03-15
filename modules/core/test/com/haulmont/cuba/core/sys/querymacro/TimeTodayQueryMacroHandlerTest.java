/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.04.2010 17:50:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.cuba.core.sys.querymacro.TimeTodayQueryMacroHandler;
import junit.framework.TestCase;

public class TimeTodayQueryMacroHandlerTest extends TestCase {

    public void testExpandMacro() throws Exception {
        TimeTodayQueryMacroHandler handler = new TimeTodayQueryMacroHandler();
        String res = handler.expandMacro("select u from sec$User where @today(u.createTs) and u.deleteTs is null");
        System.out.println(res);
        System.out.println(handler.getParams());
    }
}