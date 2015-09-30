/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;

import org.springframework.stereotype.Component;
import javax.swing.*;

/**
 * Naive implementation of WindowManagerProvider. <br/>
 * Always returns WindowManager of main application frame.
 *
 * @author artamonov
 * @version $Id$
 */
@Component(WindowManagerProvider.NAME)
public class DesktopWindowManagerProvider implements WindowManagerProvider {

    @Override
    public WindowManager get() {
        if (!SwingUtilities.isEventDispatchThread())
            throw new IllegalStateException("Could not access to WindowManager outside Event Dispatch Thread");

        TopLevelFrame topFrame = App.getInstance().getMainFrame();

        return topFrame.getWindowManager();
    }
}