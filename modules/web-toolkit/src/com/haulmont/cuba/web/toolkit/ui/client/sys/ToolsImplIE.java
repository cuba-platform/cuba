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
public class ToolsImplIE extends ToolsImpl {

    @Override
    protected native void setTextSelectionEnable(Element el) /*-{
        el.setAttribute('onselectstart', null);
    }-*/;

    @Override
    protected native void setTextSelectionDisable(Element el) /*-{
        el.setAttribute('onselectstart', this.@com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl::falseFunction);
    }-*/;
}