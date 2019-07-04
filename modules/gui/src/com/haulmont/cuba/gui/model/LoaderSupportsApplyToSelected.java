/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.model;

import com.haulmont.cuba.core.global.LoadContext;

import java.util.List;

/**
 * Interface implemented by data loaders that can pass the stack of previous queries to {@link LoadContext}.
 */
public interface LoaderSupportsApplyToSelected {

    /**
     * Returns last query executed by loader
     */
    LoadContext.Query getLastQuery();

    /**
     * Allows to load data on a previous loading result.
     *
     * @return editable list of previous queries
     */
    List<LoadContext.Query> getPrevQueries();

    /**
     * Set previous queries which allows to load data on a previous loading result.
     */
    void setPrevQueries(List<LoadContext.Query> prevQueries);

    /**
     * @return key of the current stack of sequential queries, which is unique for the current user session
     */
    Integer getQueryKey();

    /**
     * @param queryKey key of the current stack of sequential queries, which is unique for the current user session
     */
    void setQueryKey(Integer queryKey);
}
