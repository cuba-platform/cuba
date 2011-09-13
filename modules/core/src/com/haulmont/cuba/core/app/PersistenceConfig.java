/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * Configuration parameters interface used by some mechanisms related to data storage.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Source(type = SourceType.DATABASE)
public interface PersistenceConfig extends Config {

    /**
     * @return Default maximum number of entity instances of certain type in database, after which lookup screen
     * will be used instead of dropdown list for lookup.
     */
    @DefaultInt(100)
    int getDefaultLookupScreenThreshold();
    void setDefaultLookupScreenThreshold(int value);

    /**
     * @return Default maximum number of entity instances of certain type in database, after which a lazy
     * collection datasource will be used. This parameter is taken into account only if entity statistics has been
     * gathered and if the datasource has fetchMode="LAZY".
     */
    @DefaultInt(100)
    int getDefaultLazyCollectionThreshold();
    void setDefaultLazyCollectionThreshold(int value);

    /**
     * @return Default number of entity instances to fetch from the database. This parameter affects some visual
     * components working with datasources to show entity lists.
     */
    @DefaultInt(100)
    int getDefaultFetchUI();
    void setDefaultFetchUI(int value);

    /**
     * @return Default maximum number of entity instances to fetch from the database. Each SELECT statement is
     * automatically amended with an appropriate LIMIT clause.
     */
    @DefaultInt(10000)
    int getDefaultMaxFetchUI();
    void setDefaultMaxFetchUI(int value);
}
