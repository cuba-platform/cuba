/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class LockBrowser extends AbstractWindow {

    @Inject
    protected LockService service;

    @Named("locks")
    protected Table table;

    public void init(Map<String, Object> params) {
        table.addAction(new RefreshAction(table));
        table.refresh();
    }

    public void unlock(){
        LockInfo lockInfo = table.getSingleSelected();
        if (lockInfo != null) {
            service.unlock(lockInfo.getEntityName(), lockInfo.getEntityId());
            table.refresh();
        }
    }

    public void reloadConfig(){
        service.reloadConfiguration();
    }
}
