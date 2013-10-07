/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.vaadin.ui.Panel;
import org.apache.commons.lang.StringUtils;

/**
 * Base class for application's UI content.
 *
 * @see LoginWindow
 * @see AppWindow
 *
 * @author artamonov
 * @version $Id$
 */
public abstract class UIView extends Panel {

    protected String baseStyle = null;

    public String getBaseStyle() {
        return null;
    }

    public void setBaseStyle(String baseStyle) {
        this.baseStyle = baseStyle;
    }

    public String getStyle(String style) {
        if (StringUtils.isEmpty(baseStyle))
            return style;
        else
            return baseStyle + "-" + style;
    }

    public abstract String getTitle();
}