/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web;

import com.vaadin.ui.Panel;
import org.apache.commons.lang.StringUtils;

/**
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