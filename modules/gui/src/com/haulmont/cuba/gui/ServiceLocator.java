/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 14:57:17
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.sys.AppContext;

/**
 * Locator of middleware services for use on presentation layer
 */
public abstract class ServiceLocator
{
    private static transient DataService dataService;

    /**
     * Locate service reference by name
     */
    public static <T> T lookup(String name) {
        return (T) AppContext.getBean(name);
    }

    /**
     * Reference to DataService
     */
    public static DataService getDataService() {
        if (dataService == null) {
            dataService = (DataService) AppContext.getBean(DataService.NAME);
        }
        return dataService;
    }
}
