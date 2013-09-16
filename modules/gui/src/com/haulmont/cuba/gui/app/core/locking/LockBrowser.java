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

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class LockBrowser extends AbstractWindow {

    @Inject
    protected LockService service;

    @Named("locks")
    protected Table table;

    @Named("setupTable.create")
    protected CreateAction createAction;

    @Named("setupTable.edit")
    protected EditAction editAction;

    @Inject
    protected Table setupTable;

    public void init(Map<String, Object> params) {
        createAction.setOpenType(WindowManager.OpenType.DIALOG);
        editAction.setOpenType(WindowManager.OpenType.DIALOG);
        table.refresh();
    }

    public void unlock() {
        LockInfo lockInfo = table.getSingleSelected();
        if (lockInfo != null) {
            service.unlock(lockInfo.getEntityName(), lockInfo.getEntityId());
            table.refresh();
        }
    }

    public void refresh() {
        table.refresh();
    }

    public void reloadConfig() {
        service.reloadConfiguration();
    }
}
