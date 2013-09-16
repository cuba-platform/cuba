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
        el.setAttribute('onselectstart', null);
    }-*/;

    @Override
    protected native void setTextSelectionDisable(Element el) /*-{
        el.setAttribute('onselectstart', this.@com.haulmont.cuba.web.toolkit.ui.client.sys.ToolsImpl::falseFunction);
    }-*/;
}