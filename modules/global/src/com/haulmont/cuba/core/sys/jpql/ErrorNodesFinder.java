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

package com.haulmont.cuba.core.sys.jpql;

import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class ErrorNodesFinder implements TreeVisitorAction {
    protected List<CommonErrorNode> errorNodes = new ArrayList<>();

    @Override
    public Object pre(Object t) {
        if (t instanceof CommonErrorNode) {
            errorNodes.add((CommonErrorNode) t);
            return t;
        }
        return t;
    }

    @Override
    public Object post(Object t) {
        return t;
    }

    public List<CommonErrorNode> getErrorNodes() {
        return Collections.unmodifiableList(errorNodes);
    }
}
