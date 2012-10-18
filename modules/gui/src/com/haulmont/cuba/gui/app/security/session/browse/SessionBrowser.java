/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

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
    protected Messages messages;

    @Inject
    private Label lastUpdateTsLab;

    @Named("sessionsTable.message")
    private Action messageAction;

    public void init(Map<String, Object> params) {
        super.init(params);
        // TODO remove after implementing #1558
        if (!ClientType.WEB.equals(AppConfig.getClientType())) {
            messageAction.setVisible(false);
        }
        sessionsDs.addListener(new CollectionDsListenerAdapter<UserSessionEntity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation) {
                String time = Datatypes.get(Date.class).format(sessionsDs.getUpdateTs(), userSessionSource.getLocale());
                lastUpdateTsLab.setValue(time);
            }
        });
    }

    public void refresh() {
        sessionsDs.refresh();
    }

    public void message() {
        final Set<UserSessionEntity> selected = sessionsTable.getSelected();
        final Set<UserSessionEntity> all = new HashSet<>();
        for (UUID id : sessionsDs.getItemIds()) {
            all.add(sessionsDs.getItem(id));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("selectedSessions", selected);
        params.put("allSessions", all);
        final SessionMessageWindow window = openWindow("sessionMessageWindow", WindowManager.OpenType.DIALOG, params);
        window.addListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                String result = window.getResult();
                if (!StringUtils.isBlank(result)) {
                    showNotification(result, NotificationType.TRAY);
                }
            }
        });
    }

    public void kill() {
        final Set<UserSessionEntity> selected = sessionsTable.getSelected();
        if (selected.isEmpty())
            return;

        showOptionDialog(
                messages.getMainMessage("dialogs.Confirmation"),
                messages.getMessage(getClass(), "killConfirm"),
                MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.OK) {
                            @Override
                            public void actionPerform(Component component) {
                                for (UserSessionEntity session : selected) {
                                    if (!session.getId().equals(userSessionSource.getUserSession().getId())) {
                                        uss.killSession(session.getId());
                                    } else
                                        showNotification(getMessage("killUnavailable"), NotificationType.WARNING);
                                }
                                sessionsTable.getDatasource().refresh();
                            }
                        },
                        new DialogAction(DialogAction.Type.CANCEL)
                }
        );
    }
}
