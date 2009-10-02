/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 26.03.2009 12:22:46
 * $Id$
 */

package com.haulmont.cuba.gui.components;

public interface WrappedFrame {

    IFrame wrapBy(Class<? extends IFrame> aClass);
}