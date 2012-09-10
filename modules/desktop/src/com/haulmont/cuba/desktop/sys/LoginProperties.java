/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
 *
 * @author devyatkin
 * @version $Id$
 */
public class LoginProperties {

    private static Log log = LogFactory.getLog(LoginProperties.class);

    private static String LOGIN = "login";
            
    public static void saveLogin(String login) {
        Properties properties = new Properties();
        properties.setProperty(LOGIN, login);
        try {

            File file = loadFile();
            FileOutputStream stream = FileUtils.openOutputStream(file);
            try {
                properties.store(stream, "Login properties");
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } catch (IOException e) {
            log.error("Error saving login properties", e);
        }
    }

    public static String loadLastLogin() {
        Properties properties = new Properties();
        try {
            File file = loadFile();
            if (file.exists()) {
                FileInputStream stream = FileUtils.openInputStream(file);
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
    
    private static File loadFile(){
        String dataDir = AppBeans.get(Configuration.NAME, Configuration.class).getConfig(GlobalConfig.class).getDataDir();
        return new File(dataDir, "login.properties");
    }
}
