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

    public static int getHorizontalPaddingsAndBorder(Element element) 
    {
        int paddings;
        String originalWidth = DOM.getElementAttribute(element, "width");

        int offsetWidth = element.getOffsetWidth();
        DOM.setElementAttribute(element, "width", offsetWidth + "px");
        paddings = element.getOffsetWidth() - offsetWidth;
        if (paddings < 0) {
            paddings = 0;
        }

        DOM.setElementAttribute(element, "width", originalWidth);

        return paddings;
    }

    public static int getVerticalPaddingsAndBorder(Element element)
    {
        int paddings;
        String originalHeight = DOM.getElementAttribute(element, "height");

        int offsetHeight = element.getOffsetHeight();
        DOM.setElementAttribute(element, "height", offsetHeight + "px");
        paddings = element.getOffsetHeight() - offsetHeight;
        if (paddings < 0) {
            paddings = 0;
        }

        DOM.setElementAttribute(element, "height", originalHeight);

        return paddings;
    }
}
