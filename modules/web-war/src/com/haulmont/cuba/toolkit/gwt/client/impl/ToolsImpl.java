/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 19.12.2008 14:16:24
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.impl;

public class ToolsImpl {

    public native int parseSize(String s) /*-{
         try {
            var result = /^(\d+)(%|px|em|ex|in|cm|mm|pt|pc)$/.exec(s)
            return parseInt(result[0]);
         } catch (e) {
            return -1;
         }
    }-*/;
}
