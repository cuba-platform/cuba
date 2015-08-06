/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.StringListTypeFactory;

import java.util.List;

@Source(type = SourceType.DATABASE)
public interface FtsConfig extends Config {

    @Property("cuba.fts.enabled")
    @DefaultBoolean(false)
    boolean getEnabled();
    void setEnabled(boolean enable);

    @Property("cuba.fts.indexingBatchSize")
    @DefaultInt(300)
    int getIndexingBatchSize();

    @Property("cuba.fts.indexDir")
    String getIndexDir();

    @Property("cuba.fts.maxSearchResults")
    @DefaultInt(100)
    int getMaxSearchResults();

    @Property("cuba.fts.searchResultsBatchSize")
    @DefaultInt(5)
    int getSearchResultsBatchSize();

    @Property("cuba.fts.storeContentInIndex")
    @DefaultBoolean(true)
    boolean getStoreContentInIndex();

    @Property("cuba.fts.indexingHosts")
    @Factory(factory = StringListTypeFactory.class)
    List<String> getIndexingHosts();

    @Property("cuba.fts.reindexBatchSize")
    @DefaultInt(5000)
    int getReindexBatchSize();
}
