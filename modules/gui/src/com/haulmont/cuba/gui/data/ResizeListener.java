/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import java.io.Serializable;

/**
 * @author subbotin
 * @version $Id$
 */
public interface ResizeListener<T> extends Serializable {
    public void onResize(T source, String oldWidth, String oldHeight, String width, String height);
}
