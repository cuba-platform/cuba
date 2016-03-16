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

import java.util.List;
import java.util.UUID;

/**
 * Interface provides functionality for working with previously selected query results
 */
public interface QueryResultsService {
    String NAME = "cuba_QueryResultsService";

    /**
     * Stores {@code idList} collection as {@link com.haulmont.cuba.core.entity.QueryResult} records
     */
    void insert(int queryKey, List<UUID> idList);

    /**
     * Deletes {@code QueryResult} records with given {@code queryKey}
     */
    void delete(int queryKey);
}
