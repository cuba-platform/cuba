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

    @Property("fts.enabled")
    @DefaultBoolean(false)
    boolean getEnabled();
    void setEnabled(boolean enable);

    @Property("fts.indexingBatchSize")
    @DefaultInt(300)
    int getIndexingBatchSize();

    @Property("fts.indexDir")
    String getIndexDir();

    @Property("fts.maxSearchResults")
    @DefaultInt(100)
    int getMaxSearchResults();

    @Property("fts.searchResultsBatchSize")
    @DefaultInt(5)
    int getSearchResultsBatchSize();

    @Property("fts.storeContentInIndex")
    @DefaultBoolean(true)
    boolean getStoreContentInIndex();

    @Property("fts.indexingHosts")
    @Factory(factory = StringListTypeFactory.class)
    List<String> getIndexingHosts();

    @Property("fts.reindexBatchSize")
    @DefaultInt(5000)
    int getReindexBatchSize();
}
