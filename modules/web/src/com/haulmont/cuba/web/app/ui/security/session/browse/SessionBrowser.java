/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: DEGTYARJOV EUGENIY
 * Created: 14.09.2009 12:29:34
 *
 * $Id$
 */

package com.haulmont.cuba.web.app.ui.security.session.browse;

import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

import java.util.Map;
import java.util.Set;

public class SessionBrowser extends AbstractLookup {
    public SessionBrowser(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        super.init(params);

        final Table table = getComponent("sessions_table");
        table.addAction(new AbstractAction("refresh") {
            public void actionPerform(Component component) {
                table.getDatasource().refresh();
            }
        });
        table.addAction(new AbstractAction("kill") {
            public void actionPerform(Component component) {
                Set<UserSessionEntity> set = table.getSelected();
                for (UserSessionEntity session : set){
                    if (!session.getId().equals(UserSessionClient.getUserSession().getId())) {
                        UserSessionService uss = ServiceLocator.lookup(UserSessionService.NAME);
                        uss.killSession(session.getId());
                    }
                }
                table.getDatasource().refresh();
            }
        });
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        vTable.setAllowMultiStringCells(true);
    }
}
