/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigStorageCommon;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.security.app.Authenticated;
import com.haulmont.cuba.core.app.ConfigStorageAPI;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.Authentication;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_ConfigStorageMBean")
public class ConfigStorage implements ConfigStorageMBean {

    @Inject
    protected ConfigStorageAPI configStorage;

    @Inject
    protected ConfigStorageCommon configStorageCommon;

    @Override
    public String printDbProperties() {
        return printDbProperties(null);
    }

    @Authenticated
    @Override
    public String printDbProperties(String prefix) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : configStorage.getDbProperties().entrySet()) {
                if (prefix == null || entry.getKey().startsWith(prefix)) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public String getDbProperty(String name) {
        if (StringUtils.isBlank(name))
            return "Enter a property name";

        try {
            String value = configStorage.getDbProperty(name);
            return name + "=" + value;
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public String setDbProperty(String name, String value) {
        if (StringUtils.isBlank(name))
            return "Enter a property name";
        if (StringUtils.isBlank(value))
            return "Enter a property value";

        try {
            configStorage.setDbProperty(name, value);
            return "Property " + name + " set to " + value;
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Authenticated
    @Override
    public String removeDbProperty(String name) {
        try {
            configStorage.setDbProperty(name, null);
            return "Property " + name + " removed";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public void clearCache() {
        configStorage.clearCache();
    }

    @Override
    public String printAppProperties() {
        return printAppProperties(null);
    }

    @Override
    public String printAppProperties(String prefix) {
        return configStorageCommon.printAppProperties(prefix);
    }

    @Override
    public String getAppProperty(String name) {
        return configStorageCommon.getAppProperty(name);
    }

    @Override
    public String setAppProperty(String name, String value) {
        return configStorageCommon.setAppProperty(name, value);
    }

    @Override
    @Authenticated
    public String getConfigValue(String classFQN, String methodName) {
        return configStorageCommon.getConfigValue(classFQN, methodName);
    }
}
