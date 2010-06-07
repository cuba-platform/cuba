/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.06.2010 12:29:24
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.locking;

import com.haulmont.cuba.core.app.LockService;
import com.haulmont.cuba.core.global.LockInfo;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;

import java.util.Map;

public class LockBrowser extends AbstractWindow {

    public LockBrowser(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        final Table table = getComponent("locks");

        TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createRefreshAction();

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
