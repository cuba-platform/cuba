/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class SessionBrowser extends AbstractLookup {

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private UserSessionService uss;

    @Inject
    private Table sessionsTable;

    @Inject
    private UserSessionsDatasource sessionsDs;

    @Inject
    private Label lastUpdateTsLable;

    public void init(Map<String, Object> params) {
        super.init(params);

        sessionsTable.addAction(new AbstractAction("refresh") {
            @Override
            public void actionPerform(Component component) {
                sessionsTable.getDatasource().refresh();
            }
        });
        sessionsTable.addAction(new AbstractAction("kill") {
            @Override
            public void actionPerform(Component component) {
                Set<UserSessionEntity> set = sessionsTable.getSelected();
                for (UserSessionEntity session : set) {
                    if (!session.getId().equals(userSessionSource.getUserSession().getId())) {
                        uss.killSession(session.getId());
                    } else
                        showNotification(getMessage("killUnavailable"), NotificationType.WARNING);
                }
                sessionsTable.getDatasource().refresh();
            }
        });
        sessionsDs.addListener(new CollectionDsListenerAdapter<UserSessionEntity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                lastUpdateTsLable.setValue(sessionsDs.getUpdateTs());
            }
        });
    }
}
