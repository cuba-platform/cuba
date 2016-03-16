/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
