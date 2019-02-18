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

package com.haulmont.cuba.testsupport;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.DefaultJpqlSortExpressionProvider;

import java.util.HashSet;
import java.util.Set;

public class TestJpqlSortExpressionProvider extends DefaultJpqlSortExpressionProvider {
    protected Set<MetaPropertyPath> toUpperPaths = new HashSet<>();

    @Override
    public String getDatatypeSortExpression(MetaPropertyPath metaPropertyPath, boolean sortDirectionAsc) {
        if (toUpperPaths.contains(metaPropertyPath)) {
            return String.format("upper({E}.%s) %s nulls first", metaPropertyPath.toString(), sortDirectionAsc ? "asc" : "desc");
        } else {
            return super.getDatatypeSortExpression(metaPropertyPath, sortDirectionAsc);
        }
    }

    public void addToUpperPath(MetaPropertyPath metaPropertyPath) {
        toUpperPaths.add(metaPropertyPath);
    }

    public void resetToUpperPaths() {
        toUpperPaths.clear();
    }
}
