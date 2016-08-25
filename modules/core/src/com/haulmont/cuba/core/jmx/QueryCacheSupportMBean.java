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

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Manages query cache")
public interface QueryCacheSupportMBean {

    long getMaxSize();

    long getSize();

    @ManagedOperation(description = "Discards all queries results in the cache")
    String invalidateAll();

    @ManagedOperation(description = "Discards cached query results for entity")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "metaClass", description = "Entity name, e.g. sec$User")
    })
    String invalidate(String typeName);

    @ManagedOperation(description = "Discards cached query results for query")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "id", description = "query identifier")
    })
    String invalidateById(String id);

    @ManagedOperation(description = "Print statistics for all cached queries (query string and count of objects for query)")
    String printQueryResultsStat();

    @ManagedOperation(description = "Print cached results for query")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "id", description = "query identifier")
    })
    String printQueryResultsByQueryId(String id);
}
