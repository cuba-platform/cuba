/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.04.2010 17:50:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import junit.framework.TestCase;

public class TimeBetweenQueryMacroHandlerTest extends TestCase {

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
    }
}
