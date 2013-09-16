/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.sys.AppContext;

/**
 * DEPRECATED - use {@link Configuration} via DI or <code>AppBeans.get(Configuration.class)</code>
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
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
