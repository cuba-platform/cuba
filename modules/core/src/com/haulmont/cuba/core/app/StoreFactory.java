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
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * INTERNAL.
 * Factory of {@link DataStore} implementations.
 */
@Component(StoreFactory.NAME)
public class StoreFactory {

    private Logger log = LoggerFactory.getLogger(StoreFactory.class);

    public static final String NAME = "cuba_StoreFactory";

    private Map<String, DataStore> stores = new HashMap<>();

    /**
     * Get a {@link DataStore} implementation by name.
     * The implementation bean should be registered in a {@code cuba.storeImpl_<storeName>} app property. If no such
     * property specified, the {@link RdbmsStore} is returned.
     */
    public DataStore get(String name) {
        DataStore store = stores.get(name);
        if (store != null) {
            return store;
        }

        String implName = AppContext.getProperty("cuba.storeImpl_" + name);
        if (implName == null) {
            log.debug("No implementation is specified for {} store, using RdbmsStore", name);
            implName = RdbmsStore.NAME;
        }
        store = AppBeans.getPrototype(implName, name);
        stores.put(name, store);
        return store;
    }
}
