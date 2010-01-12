/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.01.2010 14:40:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;

import java.util.Map;

public class CubaOpenJpaVendorAdapter extends OpenJpaVendorAdapter {

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> map = super.getJpaPropertyMap();
        for (String name : AppContext.getPropertyNames()) {
            if (name.startsWith("openjpa.")) {
                map.put(name, AppContext.getProperty(name));
            }
        }
        return map;
    }
}
