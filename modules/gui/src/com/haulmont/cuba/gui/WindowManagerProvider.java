/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui;

/**
 * Provides current client specific WindowManager
 *
 * @author artamonov
 * @version $Id$
 */
public interface WindowManagerProvider {

    String NAME = "cuba_WindowManagerProvider";

    WindowManager get();
}