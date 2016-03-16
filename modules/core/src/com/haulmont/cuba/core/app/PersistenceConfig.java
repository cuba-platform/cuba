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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * Configuration parameters interface used by some mechanisms related to data storage.
 *
 */
@Source(type = SourceType.DATABASE)
public interface PersistenceConfig extends Config {

    /**
     * @return Default maximum number of entity instances of certain type in database, after which lookup screen
     * will be used instead of dropdown list for lookup.
     */
    @Property("cuba.defaultLookupScreenThreshold")
    @DefaultInt(100)
    int getDefaultLookupScreenThreshold();
    void setDefaultLookupScreenThreshold(int value);

    /**
     * @return Default maximum number of entity instances of certain type in database, after which a lazy
     * collection datasource will be used. This parameter is taken into account only if entity statistics has been
     * gathered and if the datasource has fetchMode="LAZY".
     */
    @Property("cuba.defaultLazyCollectionThreshold")
    @DefaultInt(100)
    int getDefaultLazyCollectionThreshold();
    void setDefaultLazyCollectionThreshold(int value);

    /**
     * @return Default number of entity instances to fetch from the database. This parameter affects some visual
     * components working with datasources to show entity lists.
     */
    @Property("cuba.defaultFetchUI")
    @DefaultInt(50)
    int getDefaultFetchUI();
    void setDefaultFetchUI(int value);

    /**
     * @return Default maximum number of entity instances to fetch from the database. Each SELECT statement is
     * automatically amended with an appropriate LIMIT clause.
     */
    @Property("cuba.defaultMaxFetchUI")
    @DefaultInt(10000)
    int getDefaultMaxFetchUI();
    void setDefaultMaxFetchUI(int value);
}
