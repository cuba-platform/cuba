/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.theme;

import java.util.Collections;
import java.util.Map;

/**
 * Theme defaults for application UI, components and screens.
 *
 * @author artamonov
 * @version $Id$
 */
public class ThemeConstants {

    public static final String PREFIX = "theme://";

    protected Map<String, String> properties;

    public ThemeConstants(Map<String, String> properties) {
        this.properties = Collections.unmodifiableMap(properties);
    }

    public String get(String key) {
        return properties.get(key);
    }

    public int getInt(String key) {
        String value = properties.get(key);
        if (value != null && value.endsWith("px")) {
            value = value.substring(0, value.length() - 2);
        }

        if (value == null) {
            throw new IllegalArgumentException("Null value for theme key " + key);
        }

        return Integer.parseInt(value);
    }
}