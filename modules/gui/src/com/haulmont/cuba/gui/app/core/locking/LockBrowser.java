/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author krivopustov
 * @version $Id$
 */
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