/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.sys;

import com.google.gwt.dom.client.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class ToolsImplIE extends ToolsImpl {

    @Override
    protected native void setTextSelectionEnable(Element el) /*-{
        if (typeof el.style == "undefined")
            el.style = {};

        el.onselectstart = null;
        el.style.msUserSelect = "";
    }-*/;

    @Override
    protected native void setTextSelectionDisable(Element el) /*-{
        if (typeof $wnd.ieTextSelectionFalseFunction != "function") {
            $wnd.ieTextSelectionFalseFunction = function() {
                return false;
            };
        }

        if (typeof el.style == "undefined") {
            el.style = {};
        }

        el.onselectstart = $wnd.ieTextSelectionFalseFunction;
        el.style.msUserSelect = "none";
    }-*/;
}