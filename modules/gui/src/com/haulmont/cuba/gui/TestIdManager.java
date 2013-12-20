/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class TestIdManager {

    protected Map<String, Integer> ids = new HashMap<>();

    public String getTestId(String baseId) {
        String id = normalize(baseId);

        Integer number = ids.get(id);
        if (number == null) {
            number = 0;
        } else {
            number++;
        }
        ids.put(id, number);

        // prevent conflicts
        while (ids.containsKey(id + number)) {
            number++;
        }

        if (number > 0) {
            id = id + number;
        }

        return id;
    }

    public String reserveId(String id) {
        if (!ids.containsKey(id)) {
            ids.put(id, 0);
        }

        return id;
    }

    public String normalize(String id) {
        if (id != null) {
            return  id.replaceAll("[^\\p{L}\\p{Nd}]", "_");
        }
        return id;
    }

    public void reset() {
        ids.clear();
    }
}