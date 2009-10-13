/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.10.2009 13:25:10
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.config.type.TypeFactory;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

public class AvailableLocalesFactory extends TypeFactory {

    public Object build(String string) {
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
