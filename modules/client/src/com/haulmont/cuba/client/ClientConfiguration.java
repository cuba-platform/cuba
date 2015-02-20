/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.Configuration;

/**
 * Extension of the global {@link Configuration} interface specific for the client tier.
 *
 * <p>Adds method {@link #getConfigCached(Class)} to obtain config proxies that are more effective in server invocations
 * at the cost of possible stale DB-stored parameter values.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ClientConfiguration extends Configuration {

    /**
     * A config implementation instance obtained through this method caches DB-stored parameter values to minimize
     * server invocations. There is a side effect of this behaviour: if you get a config instance, all subsequent
     * invocations of some method will return the same value, even if the real value of the parameter has been changed.
     * You can see the new value only when you get another instance of the config interface.
     *
     * @param configInterface   class of configuration interface
     * @return                  an instance to work with parameters
     */
    <T extends Config> T getConfigCached(Class<T> configInterface);
}
