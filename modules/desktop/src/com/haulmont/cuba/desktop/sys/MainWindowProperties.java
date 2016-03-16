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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 */
public class MainWindowProperties {

    private JFrame frame;

    private Logger log = LoggerFactory.getLogger(getClass());

    public MainWindowProperties(JFrame frame) {
        this.frame = frame;
    }

    public void save() {
        Properties properties = new Properties();
        saveProperties(properties);
        try {
            File file = new File(AppBeans.get(Configuration.class).getConfig(GlobalConfig.class).getDataDir(), "main-window.properties");
            FileOutputStream stream = FileUtils.openOutputStream(file);
            try {
                properties.store(stream, "Main window properties");
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } catch (IOException e) {
            log.error("Error saving main window location", e);
        }
    }

    protected void saveProperties(Properties properties) {
        if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
            properties.setProperty("maximized", "true");
        } else {
            properties.setProperty("x", String.valueOf((int)frame.getBounds().getX()));
            properties.setProperty("y", String.valueOf((int)frame.getBounds().getY()));
            properties.setProperty("width", String.valueOf((int)frame.getBounds().getWidth()));
            properties.setProperty("height", String.valueOf((int)frame.getBounds().getHeight()));
        }
    }

    public void load() {
        Properties properties = new Properties();
        try {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            File file = new File(configuration.getConfig(GlobalConfig.class).getDataDir(), "main-window.properties");
            if (file.exists()) {
                FileInputStream stream = FileUtils.openInputStream(file);
                try {
                    properties.load(stream);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            }
        } catch (IOException e) {
            log.error("Error loading main window location", e);
        }
        loadProperties(properties);
    }

    protected void loadProperties(Properties properties) {
        Boolean maximized = Boolean.valueOf(properties.getProperty("maximized", "false"));
        if (maximized) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        int x = Integer.parseInt(properties.getProperty("x", "0"));
        int y = Integer.parseInt(properties.getProperty("y", "0"));
        int width = Integer.parseInt(properties.getProperty("width", "1000"));
        int height = Integer.parseInt(properties.getProperty("height", "700"));

        frame.setBounds(x, y, width, height);
        if (x == 0 && y == 0)
            frame.setLocationRelativeTo(null);
    }
}