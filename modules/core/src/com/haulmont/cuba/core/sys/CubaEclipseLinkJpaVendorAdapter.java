/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaEclipseLinkJpaVendorAdapter extends EclipseLinkJpaVendorAdapter {

    private final EclipseLinkJpaDialect jpaDialect = new CubaEclipseLinkJpaDialect();

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> map = super.getJpaPropertyMap();
        for (String name : AppContext.getPropertyNames()) {
            if (name.startsWith("eclipselink.")) {
                map.put(name, AppContext.getProperty(name));
            }
        }
        return map;
    }

    @Override
    public EclipseLinkJpaDialect getJpaDialect() {
        return jpaDialect;
    }
}