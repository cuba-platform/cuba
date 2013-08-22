/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.LockDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class LockBrowser extends AbstractWindow {

    @Inject
    protected LockService service;

    @Named("locks")
    protected Table table;

    @Inject
    protected Table setupTable;

    public void init(Map<String, Object> params) {
        setupTable.addAction(new AbstractAction("create") {
            @Override
            public void actionPerform(Component component) {
                Window window = openEditor("sys$LockDescriptor.edit", new LockDescriptor(), WindowManager.OpenType.DIALOG);
                window.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        getDsContext().get("lockDescriptorDs").refresh();
                    }
                });
            }

            @Override
            public String getCaption() {
                return AppBeans.get(Messages.class).getMainMessage("actions.Create");
            }
        });

        setupTable.addAction(new AbstractAction("edit") {
            @Override
            public void actionPerform(Component component) {
                Entity entity = getDsContext().get("lockDescriptorDs").getItem();
                if (entity != null) {
                    Window window = openEditor("sys$LockDescriptor.edit", entity, WindowManager.OpenType.DIALOG);
                    window.addListener(new CloseListener() {
                        @Override
                        public void windowClosed(String actionId) {
                            getDsContext().get("lockDescriptorDs").refresh();
                        }
                    });
                }
            }

            @Override
            public String getCaption() {
                return AppBeans.get(Messages.class).getMainMessage("actions.Edit");
            }
        });
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
