/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 19.12.2008 14:12:50
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client;

import com.haulmont.cuba.toolkit.gwt.client.impl.ToolsImpl;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;

public class Tools {
    private static ToolsImpl impl;

    static {
        impl = new ToolsImpl();
    }

    public static int parseSize(String s) {
        return impl.parseSize(s);
    }

    public static String format(String s) {
        return impl.format(s);
    }

    public static void removeChildren(Element e) {
        int childCount = DOM.getChildCount(e);
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                DOM.removeChild(e, DOM.getChild(e, 0));
            }
        }
    }
}
