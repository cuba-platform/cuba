/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.type.TypeFactory;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

public class AvailableLocalesFactory extends TypeFactory {

    public Object build(String string) {
        if (string == null)
            return null;

        String[] items = string.split(";");
        Map<String, Locale> map = new LinkedHashMap<String, Locale>(items.length);
        for (String item : items) {
            String[] parts = item.split("\\|");
            String[] locParts = parts[1].split("_");
            Locale loc;
            if (locParts.length == 1)
                loc = new Locale(parts[1]);
            else
                loc = new Locale(locParts[0], locParts[1]);

            map.put(parts[0], loc);
        }
        return map;
    }
}
