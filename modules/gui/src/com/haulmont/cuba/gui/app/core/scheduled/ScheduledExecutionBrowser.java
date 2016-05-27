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

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.Map;

public class ScheduledExecutionBrowser extends AbstractWindow {

    @Inject
    protected Table executionsTable;

    @Inject
    protected CollectionDatasource executionsDs;

    @Inject
    protected PersistenceManagerService persistenceManager;

    @Override
    public void init(Map<String, Object> params) {
        executionsTable.addAction(new RefreshAction(executionsTable));

        int maxResults = persistenceManager.getFetchUI(executionsDs.getMetaClass().getName());
        executionsDs.setMaxResults(maxResults);
    }
}