/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Dialog that can be enabled/disabled like the main frame.
 * <p>Used by {@link DesktopWindowManager} to show windows in DIALOG mode.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class DialogWindow extends JDialog {

    protected DisabledGlassPane glassPane;
    protected Integer fixedHeight;
    protected Integer fixedWidth;

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

    public Integer getFixedHeight() {
        return fixedHeight;
    }

    @Override
    public Dimension getPreferredSize() {
        if (fixedHeight != null && fixedWidth != null) {
            // dialog does not grow
            return new Dimension(fixedWidth, fixedHeight);
        }

        if (fixedWidth != null) {
            // dialog does not grow by horizontal axis
            return new Dimension(fixedWidth, super.getPreferredSize().height);
        }

        return super.getPreferredSize();
    }

    public void setFixedWidth(Integer fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void setFixedHeight(Integer fixedHeight) {
        this.fixedHeight = fixedHeight;

        if (fixedHeight != null) {
            // Hack to prevent dialog resizing
            addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    Rectangle b = getBounds();
                    if (b.height != DialogWindow.this.fixedHeight) {
                        b.height = DialogWindow.this.fixedHeight;
                        setBounds(b);
                    }
                    super.componentResized(e);
                }
            });
        }
    }
}