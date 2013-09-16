/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MainWindowProperties {

    private JFrame frame;

    private Log log = LogFactory.getLog(getClass());

    public MainWindowProperties(JFrame frame) {
        this.frame = frame;
    }

    public void save() {
        Properties properties = new Properties();
        saveProperties(properties);
        try {
            File file = new File(ConfigProvider.getConfig(GlobalConfig.class).getDataDir(), "main-window.properties");
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
            File file = new File(ConfigProvider.getConfig(GlobalConfig.class).getDataDir(), "main-window.properties");
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
        int x = Integer.valueOf(properties.getProperty("x", "0"));
        int y = Integer.valueOf(properties.getProperty("y", "0"));
        int width = Integer.valueOf(properties.getProperty("width", "1000"));
        int height = Integer.valueOf(properties.getProperty("height", "700"));

        frame.setBounds(x, y, width, height);
        if (x == 0 && y == 0)
            frame.setLocationRelativeTo(null);
    }
}
