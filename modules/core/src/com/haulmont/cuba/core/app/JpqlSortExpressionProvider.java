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
 * Interface to be implemented by a Spring bean that generates JPQL sort expression for datatype and LOB attributes.
 * <p>
 * Override the bean to provide custom sort logic, e.g. to use JPQL functions.
 */
public interface JpqlSortExpressionProvider {

    String NAME = "cuba_JpqlSortExpressionProvider";

    /**
     * Returns JPQL "order by" expression for the specified attribute,
     * e.g. <code>{E}.property</code>, where <code>{E}</code> is a placeholder for entity alias.
     * It's possible to:
     * <ul>
     *     <li>Apply JPQL functions for property, e.g <br><code>upper({E}.property)</code></li>
     *     <li>Use <code>asc/desc</code> or <code>nulls last/nulls first</code>,
     *     e.g. <br><code>{E}.property asc nulls first</code></li>
     * </ul>
     */
    String getDatatypeSortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc);

    /**
     * Returns JPQL "order by" expression for the specified LOB attribute.
     *
     * @see #getDatatypeSortExpression(MetaPropertyPath, boolean)
     */
    String getLobSortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc);
}