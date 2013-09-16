/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

/**
 * Wraps a JavaScript Date object.
 */
public class JsDate extends JavaScriptObject {
    /**
     * Creates a new date with the specified internal representation, which is the
     * number of milliseconds since midnight on January 1st, 1970. This is the
     * same representation returned by {@link #getTime()}.
     */
    public static native JsDate create(double milliseconds) /*-{
        return new Date(milliseconds);
    }-*/;

    /**
     * Converts a JsDate to a Java Date.  If the JsDate is null, the function
     * returns null.
     *
     * @param js A JsDate.
     * @return A Java Date object, or null, if js is null.
     */
    public static Date toJava(JsDate js) {
        return js == null ? null : new Date(js.getTimeMs());
    }

    /**
     * Converts a Java Date to a JsDate.  If the Date is null, the function
     * returns null.
     *
     * @param java A Java Date.
     * @return An analogous JsDate, or null if the original Java Date was null.
     */
    public static JsDate toJs(Date java) {
        return java == null ? null : create(java.getTime());
    }

    protected JsDate() {
    }

    /**
     * A wrapper for the JavaScript Date's getTime() method.
     *
     * @return The time in milliseconds corresponding to this JsDate.
     */
    public final long getTimeMs() {
        return (long) getTime();
    }

    private native double getTime() /*-{
        return this.getTime();
    }-*/;
}