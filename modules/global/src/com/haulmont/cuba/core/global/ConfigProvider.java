/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 17:10:33
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.sys.AppContext;

/**
 * Entry point to configuration parameters functionality.<br>
 * Use static methods. 
 */
public abstract class ConfigProvider
{
    private static ConfigProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_ConfigProvider", ConfigProvider.class);
    }

    /**
     * Get reference to a configuration interface implementation.
     * @param configInterface   class of configuration interface
     * @return  the interface implementation which can be used to get/set parameters
     */
    public static <T extends Config> T getConfig(Class<T> configInterface) {
        return getInstance().doGetConfig(configInterface);
    }

    public abstract <T extends Config> T doGetConfig(Class<T> configInterface);
}
