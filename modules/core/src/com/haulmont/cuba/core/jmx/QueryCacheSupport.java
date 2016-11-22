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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.core.sys.entitycache.QueryCache;
import com.haulmont.cuba.core.sys.entitycache.QueryCacheManager;
import com.haulmont.cuba.core.sys.entitycache.QueryKey;
import com.haulmont.cuba.core.sys.entitycache.QueryResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

@Component("cuba_QueryCacheSupportMBean")
public class QueryCacheSupport implements QueryCacheSupportMBean {
    @Inject
    protected QueryCache queryCache;
    @Inject
    protected QueryCacheManager queryCacheMgr;

    @Override
    public long getMaxSize() {
        return queryCache.getMaxSize();
    }

    @Override
    public long getSize() {
        return queryCache.size();
    }

    @Override
    public String evictAll() {
        queryCacheMgr.invalidateAll(true);
        return "Done";
    }

    @Override
    public String evict(String typeName) {
        if (Strings.isNullOrEmpty(typeName)) {
            return "Please specify entity name";
        }
        queryCacheMgr.invalidate(typeName, true);
        return "Done";
    }

    @Override
    public String evictById(String queryId) {
        if (Strings.isNullOrEmpty(queryId)) {
            return "Please specify query id";
        }
        UUID uuid;
        try {
            uuid = UuidProvider.fromString(queryId);
        } catch (Exception e) {
            return "Incorrect identifier";
        }
        queryCacheMgr.invalidate(uuid, true);
        return "Done";
    }

    @Override
    public String printCacheContent() {
        Map<QueryKey, QueryResult> map = queryCache.asMap();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<QueryKey, QueryResult> entry : map.entrySet()) {
            QueryResult queryResult = entry.getValue();
            builder.append(entry.getKey().printDescription())
                    .append(", instance count: ")
                    .append(queryResult.getResult() != null ? queryResult.getResult().size() : 0)
                    .append("\n");
        }
        return builder.toString();
    }

    @Override
    public String printQueryResultsByQueryId(String queryId) {
        if (Strings.isNullOrEmpty(queryId)) {
            return "Please specify query id";
        }
        UUID uuid;
        try {
            uuid = UuidProvider.fromString(queryId);
            QueryKey queryKey = queryCache.findQueryKeyById(uuid);
            if (queryKey != null) {
                QueryResult queryResult = queryCache.get(queryKey);
                if (queryResult != null) {
                    return queryResult.getResult() == null ? "[]" : queryResult.getResult().toString();
                }
            }
        } catch (Exception e) {
            return "Incorrect identifier";
        }
        return null;
    }
}
