/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.DisabledGlassPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * Container for detached inner tab or frame
 *
 * @author devyatkin
 * @version $Id$
 */
public class DetachedFrame extends JFrame {

    private Container parentContainer;

    private DisabledGlassPane glassPane;

    public DetachedFrame(String caption, Container parentContainer) {
        super(caption);
        this.parentContainer = parentContainer;
        initUI();
    }

    public Container getParentContainer() {
        return parentContainer;
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        glassPane = new DisabledGlassPane();
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setGlassPane(glassPane);

        final java.awt.Component topLevelGlassPane = DesktopComponentsHelper.getTopLevelFrame(parentContainer).getGlassPane();
        topLevelGlassPane.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
                    if (topLevelGlassPane.isVisible()) {
                        glassPane.activate(null);
                    } else {
                        glassPane.deactivate();
                    }
                }
            }
        });
    }

}
