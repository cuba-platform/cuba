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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

/**
 * INTERNAL.
 * System level wrapper around DBMS-related application properties.
 *
 * <p>For data conversion on the middleware use {@link DbTypeConverter} obtained from
 * {@link com.haulmont.cuba.core.Persistence} bean.
 * <p>If your client code needs to know what DBMS is currently in use, call {@code getDbmsType()} and
 * {@code getDbmsVersion()} methods of {@link com.haulmont.cuba.core.app.PersistenceManagerService}.
 *
 */
public class DbmsType {

    public static String getType() {
        return getType(Stores.MAIN);
    }

    public static String getType(String storeName) {
        String propName = "cuba.dbmsType";
        if (!Stores.isMain(storeName))
            propName = propName + "_" + storeName;

        String id = AppContext.getProperty(propName);
        if (StringUtils.isBlank(id))
            throw new IllegalStateException("Property " + propName + " is not set");
        return id;
    }

    public static String getVersion() {
        return getVersion(Stores.MAIN);
    }

    public static String getVersion(String storeName) {
        String propName = "cuba.dbmsVersion";
        if (!Stores.isMain(storeName))
            propName = propName + "_" + storeName;

        return StringUtils.trimToEmpty(AppContext.getProperty(propName));
    }
}