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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.LoadContext;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

/**
 */
public class QueryHolder implements Serializable {

    private static final long serialVersionUID = -6055610488135337366L;

    public final LoadContext.Query query;

    public QueryHolder(LoadContext.Query query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryHolder that = (QueryHolder) o;

        if (query == null || that.query == null) return false;
        if (!ObjectUtils.equals(query.getQueryString(), that.query.getQueryString())) return false;
        if (!ObjectUtils.equals(query.getParameters(), that.query.getParameters())) return false;

        return true;
    }
}