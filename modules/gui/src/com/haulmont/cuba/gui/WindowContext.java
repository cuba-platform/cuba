/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

/**
 * Provides access to window parameters and component values.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface WindowContext extends FrameContext {

    /**
     * How the window is opened.
     */
    WindowManager.OpenType getOpenType();
}
