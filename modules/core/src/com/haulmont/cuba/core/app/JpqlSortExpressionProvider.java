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

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaPropertyPath;

/**
 * Spring bean that generates sort expression for datatype and LOB properties.
 * It's possible to override bean and use custom sort logic, e.g. use functions.
 */
public interface JpqlSortExpressionProvider {

    String NAME = "cuba_JpqlSortExpressionProvider";

    /**
     * Returns JPQL order expression for specified property,
     * e.g. <code>{E}.property</code>, where <code>{E}</code> is a selected entity alias.
     * It's possible to:
     * <ul>
     *     <li>Apply JPQL functions for property, e.g <code>upper({E}.property)</code></li>
     *     <li>Use <code>asc/desc</code> or <code>nulls last/nulls first</code>,
     *     e.g. <code></>{E}.property asc nulls first</code></li>
     * </ul>
     */
    String getDatatypeSortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc);

    /**
     * Returns JPQL order expression for specified lob property.
     */
    String getLobSortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc);
}
