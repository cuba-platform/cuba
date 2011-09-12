/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RefreshAction;

import java.util.Map;

public class LockBrowser extends AbstractWindow {

    public LockBrowser(IFrame frame) {
        super(frame);
    }

    public void init(Map<String, Object> params) {
        final Table table = getComponent("locks");

        table.addAction(new RefreshAction(table));

        table.addAction(
                new AbstractAction("unlock") {
                    public void actionPerform(Component component) {
                        LockInfo lockInfo = table.getSingleSelected();
                        if (lockInfo != null) {
                            LockService service = ServiceLocator.lookup(LockService.NAME);
                            service.unlock(lockInfo.getEntityName(), lockInfo.getEntityId());
                            table.refresh();
                        }
                    }
                }
        );

        table.refresh();
    }
}
