/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        String key = placeholder;
        String defValue = null;
        String[] parts = placeholder.split("\\?:");
        if (parts.length == 2) {
            key = parts[0];
            defValue = parts[1];
        }

        String value = null;
        if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE)
            value = super.resolvePlaceholder(key, props, systemPropertiesMode);

        if (value == null)
            value = AppContext.getProperty(key);

        if (value == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK)
            value = super.resolvePlaceholder(key, props, systemPropertiesMode);

        if (value == null && defValue != null) {
            value = defValue;
        }
        return value;
    }
}
