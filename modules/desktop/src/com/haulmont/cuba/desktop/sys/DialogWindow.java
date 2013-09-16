/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog that can be enabled/disabled like the main frame.
 * <p>Used by {@link DesktopWindowManager} to show windows in DIALOG mode.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class DialogWindow extends JDialog {

    private DisabledGlassPane glassPane;

    public DialogWindow(Frame frame, String title) {
        super(frame, title, false);
        glassPane = new DisabledGlassPane();
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setGlassPane(glassPane);
    }

    public void disableWindow(@Nullable String message) {
        glassPane.activate(message);
    }

    public void enableWindow() {
        glassPane.deactivate();
        requestFocus();
        toFront();
    }
}
