/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

public class CubaPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    public CubaPropertyPlaceholderConfigurer() {
        Properties properties = new Properties();
        for (String name : AppContext.getPropertyNames()) {
            properties.setProperty(name, AppContext.getProperty(name));
        }
        setProperties(properties);
    }
}
