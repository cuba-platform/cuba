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
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LockBrowser extends AbstractWindow {

    @Inject
    protected LockService service;

    @Inject
    protected CollectionDatasource<LockInfo, UUID> locksDs;

    @Named("locks")
    protected Table<LockInfo> table;

    @Named("setupTable.create")
    protected CreateAction createAction;

    @Named("setupTable.edit")
    protected EditAction editAction;

    @Override
    public void init(Map<String, Object> params) {
        createAction.setOpenType(WindowManager.OpenType.DIALOG);
        editAction.setOpenType(WindowManager.OpenType.DIALOG);
        refresh();
    }

    public void unlock() {
        LockInfo lockInfo = table.getSingleSelected();
        if (lockInfo != null) {
            service.unlock(lockInfo.getEntityName(), lockInfo.getEntityId());
            refresh();
        }
    }

    public void refresh() {
        locksDs.clear();

        List<LockInfo> locks = service.getCurrentLocks();
        for (LockInfo lockInfo : locks) {
            locksDs.includeItem(lockInfo);
        }
    }

    public void reloadConfig() {
        service.reloadConfiguration();
    }
}