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
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.core.global.filter.ParameterInfo;

import java.util.Set;

public interface DsContextImplementation extends DsContext {

    void setParent(DsContext parentDsContext);

    void register(Datasource datasource);
    void unregister(Datasource datasource);
    void registerListener(ParameterInfo item, Datasource datasource);

    interface LazyTask {
        void execute(DsContext context);
    }

    void addLazyTask(LazyTask lazyTask);
    void executeLazyTasks();

    void resumeSuspended();

    void fireBeforeCommit(CommitContext context);
    void fireAfterCommit(CommitContext context, Set<Entity> committedEntities);
}