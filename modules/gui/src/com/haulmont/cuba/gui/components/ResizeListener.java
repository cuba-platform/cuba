/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author subbotin
 * @version $Id$
 */
public interface ResizeListener<T> {

    public void onResize(T source, String oldWidth, String oldHeight, String width, String height);
}
