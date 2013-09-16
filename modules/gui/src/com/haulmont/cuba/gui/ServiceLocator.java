/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.AppBeans;

/**
 * DEPRECATED. Use {@link AppBeans} or dependency injection.
 */
@Deprecated
public abstract class ServiceLocator
{
    private static transient DataService dataService;

    /**
     * Locate service reference by name
     */
    public static <T> T lookup(String name) {
        return (T) AppBeans.get(name);
    }

    /**
     * Reference to DataService
     */
    public static DataService getDataService() {
        if (dataService == null) {
            dataService = (DataService) AppBeans.get(DataService.NAME);
        }
        return dataService;
    }
}
