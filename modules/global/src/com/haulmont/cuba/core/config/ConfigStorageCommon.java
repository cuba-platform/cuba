/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean("cuba_ConfigStorageCommon")
public class ConfigStorageCommon {

    @Inject
    protected Configuration configuration;

    public String printAppProperties(String prefix) {
        List<String> list = new ArrayList<String>();
        for (String name : AppContext.getPropertyNames()) {
            if (prefix == null || name.startsWith(prefix)) {
                list.add(name + "=" + AppContext.getProperty(name));
            }
        }
        Collections.sort(list);
        return new StrBuilder().appendWithSeparators(list, "\n").toString();
    }

    public String getAppProperty(String name) {
        if (StringUtils.isBlank(name))
            return "Enter a property name";

        return name + "=" + AppContext.getProperty(name);
    }

    public String setAppProperty(String name, String value) {
        if (StringUtils.isBlank(name))
            return "Enter a property name";
        if (StringUtils.isBlank(value))
            return "Enter a property value";

        AppContext.setProperty(name, value);
        return "Property " + name + " set to " + value;
    }

    public String getConfigValue(String classFQN, String methodName) {
        Class<?> aClass;
        try {
            aClass = Class.forName(classFQN);
        } catch (ClassNotFoundException e) {
            return "Class " + classFQN + " not found.\nPlease ensure that you entered a fully qualified class name and " +
                    "that you class is in a proper application module (core, web or portal).";
        }

        if (Config.class.isAssignableFrom(aClass)) {
            Config config = configuration.getConfig((Class<? extends Config>)aClass);
            Method method;
            try {
                method = aClass.getMethod(methodName);
                Object result = method.invoke(config);
                return result.toString();
            } catch (NoSuchMethodException e) {
                return "Method " + methodName + "() not found in class " + classFQN;
            } catch (InvocationTargetException e) {
                return ExceptionUtils.getStackTrace(e);
            } catch (IllegalAccessException e) {
                return ExceptionUtils.getStackTrace(e);
            }
        } else {
            return "Class " + classFQN + " is not an implementation of Config interface";
        }
    }

}
