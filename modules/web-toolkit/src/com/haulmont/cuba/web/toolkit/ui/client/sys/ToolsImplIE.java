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
        el.setAttribute('onselectstart', null);
        el.style.msUserSelect = "";

        if (el.style.setProperty) {
            el.style.setProperty("user-select", "");
        } else {
            el.style.setAttribute("user-select", "");
        }
    }-*/;

    @Override
    protected native void setTextSelectionDisable(Element el) /*-{
        if (typeof $doc.ieTextSelectionFalseFunction != "function") {
            $doc.ieTextSelectionFalseFunction = function() {
                return false;
            };
        }

        if (typeof el.style == "undefined") {
            el.style = {};
        }

        el.setAttribute('onselectstart', $doc.ieTextSelectionFalseFunction);
        el.style.msUserSelect = "none";

        if (el.style.setProperty) {
            el.style.setProperty("user-select", "none");
        } else {
            el.style.setAttribute("user-select", "none");
        }
    }-*/;
}