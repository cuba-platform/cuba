/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sys;

import com.google.gwt.dom.client.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class ToolsImplWebkit extends ToolsImpl {

    @Override
    protected native void setTextSelectionEnable(Element el) /*-{
        if (typeof el.style == "undefined")
            el.style = {};
        el.style.webkitUserSelect = "";
    }-*/;

    @Override
    protected native void setTextSelectionDisable(Element el) /*-{
        if (typeof el.style == "undefined")
            el.style = {};
        el.style.webkitUserSelect = "none";
    }-*/;
}
