/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaOpenJpaVendorAdapter extends OpenJpaVendorAdapter {

    private final OpenJpaDialect jpaDialect = new CubaOpenJpaDialect();

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

    @Override
    public JpaDialect getJpaDialect() {
        return jpaDialect;
    }
}