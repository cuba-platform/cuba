/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
