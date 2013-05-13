/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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