/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author devyatkin
 * @version $Id$
 */
public class LoginProperties {

    private Log log = LogFactory.getLog(getClass());

    private File propertiesFile;

    private static String LOGIN = "login";

    public LoginProperties() {
        propertiesFile = loadFile();
    }

    public void saveLogin(String login) {
        Properties properties = new Properties();
        properties.setProperty(LOGIN, login);
        try {
            FileOutputStream stream = FileUtils.openOutputStream(propertiesFile);
            try {
                properties.store(stream, "Login properties");
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } catch (IOException e) {
            log.error("Error saving login properties", e);
        }
    }

    public String loadLastLogin() {
        Properties properties = new Properties();
        try {
            if (propertiesFile.exists()) {
                FileInputStream stream = FileUtils.openInputStream(propertiesFile);
                try {
                    properties.load(stream);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            }
        } catch (IOException e) {
            log.error("Error loading login properties", e);
        }
        return properties.getProperty(LOGIN);
    }

    private File loadFile() {
        String dataDir = AppBeans.get(Configuration.NAME, Configuration.class).getConfig(GlobalConfig.class).getDataDir();
        return new File(dataDir, "login.properties");
    }
}
