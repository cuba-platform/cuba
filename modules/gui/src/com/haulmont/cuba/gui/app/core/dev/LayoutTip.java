/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.dev;

/**
 * @author artamonov
 * @version $Id$
 */
public class LayoutTip {

    public final LayoutTipType errorType;
    public final String componentPath;
    public final String message;

    public LayoutTip(LayoutTipType errorType, String componentPath, String message) {
        this.errorType = errorType;
        this.componentPath = componentPath;
        this.message = message;
    }

    public static LayoutTip warn(String componentPath, String message) {
        return new LayoutTip(LayoutTipType.WARN, componentPath, message);
    }

    public static LayoutTip warn(String componentPath, String message, Object... params) {
        return new LayoutTip(LayoutTipType.WARN, componentPath, String.format(message, params));
    }

    public static LayoutTip error(String componentPath, String message) {
        return new LayoutTip(LayoutTipType.ERROR, componentPath, message);
    }

    public static LayoutTip error(String componentPath, String message, Object... params) {
        return new LayoutTip(LayoutTipType.ERROR, componentPath, String.format(message, params));
    }
}