/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.logging;

/**
 * @author artamonov
 * @version $Id$
 */
public class ClientLoggerFactory {

    public static ClientLogger getLogger(String name) {
        if (isLoggerEnabled(name))
            return new ActiveClientLogger(name);
        else
            return new ClientLogger();
    }

    public static native boolean isLoggerEnabled(String name) /*-{
        return $wnd.CubaClientDebug != null && $wnd.CubaClientDebug[name] == true;
    }-*/;
}