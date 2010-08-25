/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.06.2010 15:12:35
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

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

    @Property("cuba.fts.optimizationPeriod")
    @DefaultInt(20)
    int getOptimizationPeriod();

    @Property("cuba.fts.maxSearchResults")
    @DefaultInt(100)
    int getMaxSearchResults();

    @Property("cuba.fts.searchResultsBatchSize")
    @DefaultInt(5)
    int getSearchResultsBatchSize();

    @Property("cuba.fts.storeContentInIndex")
    @DefaultBoolean(true)
    boolean getStoreContentInIndex();
}
