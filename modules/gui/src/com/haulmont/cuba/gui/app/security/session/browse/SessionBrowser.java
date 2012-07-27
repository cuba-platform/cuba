/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

public class SessionBrowser extends AbstractLookup {

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private UserSessionService uss;

    @Resource(name = "sessions_table")
    private Table table;

    public void init(Map<String, Object> params) {
        super.init(params);

        table.addAction(new AbstractAction("refresh") {
            public void actionPerform(Component component) {
                table.getDatasource().refresh();
            }
        });
        table.addAction(new AbstractAction("kill") {
            public void actionPerform(Component component) {
                Set<UserSessionEntity> set = table.getSelected();
                for (UserSessionEntity session : set) {
                    if (!session.getId().equals(userSessionSource.getUserSession().getId())) {
                        uss.killSession(session.getId());
                    }
                }
                table.getDatasource().refresh();
            }
        });
//        table.setAllowMultiStringCells(true);
    }
}
