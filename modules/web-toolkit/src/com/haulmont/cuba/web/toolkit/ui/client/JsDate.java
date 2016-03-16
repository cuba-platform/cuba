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

package com.haulmont.cuba.web.toolkit.ui.client;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

/**
 * Wraps a JavaScript Date object.
 *
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