/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.12.2009 12:26:58
 *
 * $Id$
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
