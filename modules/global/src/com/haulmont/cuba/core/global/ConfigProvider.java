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
 * Utility class to provide configuration interfaces in static context.
 * <p>Injected {@link Configuration} interface should be used instead of this class wherever possible.</p>
 */
public abstract class ConfigProvider
{
    private static Configuration getConfiguration() {
        return AppBeans.get(Configuration.NAME, Configuration.class);
    }

    /**
     * Get reference to a configuration interface implementation.
     * @param configInterface   class of configuration interface
     * @return  the interface implementation which can be used to get/set parameters
     */
    public static <T extends Config> T getConfig(Class<T> configInterface) {
        return getConfiguration().getConfig(configInterface);
    }
}
