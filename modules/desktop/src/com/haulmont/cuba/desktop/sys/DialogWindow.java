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

package com.haulmont.cuba.desktop.sys;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog that can be enabled/disabled like the main frame.
 * <p>Used by {@link DesktopWindowManager} to show windows in DIALOG mode.</p>
 *
 */
public class DialogWindow extends JDialog {

    protected DisabledGlassPane glassPane;
    protected Integer fixedHeight;
    protected Integer fixedWidth;

    protected boolean softModal;

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

    public Integer getFixedWidth() {
        return fixedWidth;
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

        if (fixedHeight != null) {
            // dialog does not grow by vertical axis
            return new Dimension(super.getPreferredSize().width, fixedHeight);
        }

        return super.getPreferredSize();
    }

    public void setFixedWidth(Integer fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void setFixedHeight(Integer fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public boolean isSoftModal() {
        return softModal;
    }

    public void setSoftModal(boolean softModal) {
        this.softModal = softModal;
    }
}