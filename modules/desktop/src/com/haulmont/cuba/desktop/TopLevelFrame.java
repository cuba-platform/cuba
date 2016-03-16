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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.DisabledGlassPane;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;

import static com.haulmont.cuba.gui.components.Frame.NotificationType;

/**
 * Represents Top level application frame
 *
 */
public class TopLevelFrame extends JFrame {

    protected DisabledGlassPane glassPane;

    protected DesktopWindowManager windowManager;

    public TopLevelFrame(String applicationTitle) {
        super(applicationTitle);
        initUI();
    }

    protected void initUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        glassPane = new DisabledGlassPane();
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setGlassPane(glassPane);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        DesktopConfig config = configuration.getConfig(DesktopConfig.class);

        DesktopResources resources = App.getInstance().getResources();
        if (StringUtils.isNotEmpty(config.getWindowIcon())) {
            setIconImage(resources.getImage(config.getWindowIcon()));
        }
    }

    public void deactivate(@Nullable String message) {
        glassPane.activate(message);
    }

    public void activate() {
        glassPane.deactivate();
    }

    public DesktopWindowManager getWindowManager() {
        if (windowManager == null)
            initWindowManager();

        return windowManager;
    }

    protected void initWindowManager() {
        windowManager = new DesktopWindowManager(this);
    }

    public void showNotification(String caption, String description, NotificationType type) {
        getWindowManager().showNotification(caption, description, type);
    }

    public void showNotification(String caption, NotificationType type) {
        getWindowManager().showNotification(caption, type);
    }
}