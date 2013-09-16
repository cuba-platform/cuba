/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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