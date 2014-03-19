/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.UserSessionSource;
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
    protected UserSessionSource userSessionSource;

    @Inject
    protected UserSessionService uss;

    @Inject
    protected Table sessionsTable;

    @Inject
    protected UserSessionsDatasource sessionsDs;

    @Inject
    protected Label lastUpdateTsLab;

    @Inject
    protected Label sessionsInfo;

    @Inject
    protected TextField userLogin;

    @Inject
    protected TextField userName;

    @Inject
    protected TextField userAddress;

    @Inject
    protected TextField userInfo;

    @Named("sessionsTable.refresh")
    protected Action refreshAction;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        sessionsDs.addListener(new CollectionDsListenerAdapter<UserSessionEntity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<UserSessionEntity> items) {
                String time = Datatypes.getNN(Date.class).format(sessionsDs.getUpdateTs(), userSessionSource.getLocale());
                lastUpdateTsLab.setValue(time);

                Map<String, Object> info = uss.getLicenseInfo();
                Integer licensed = (Integer) info.get("licensedSessions");
                Integer active = (Integer) info.get("activeSessions");
                if (licensed == 0) {
                    sessionsInfo.setVisible(false);
                } else {
                    sessionsInfo.setValue(messages.formatMessage(getMessagesPack(), "sessionsInfo",
                            info.get("activeSessions"), info.get("licensedSessions")));
                    if (active > licensed) {
                        sessionsInfo.setStyleName("h2-red");
                    } else {
                        sessionsInfo.setStyleName(null);
                    }
                }
            }
        });

        addAction(refreshAction);
    }

    public void refresh() {
        Map<String, Object> fieldValues = new HashMap<>();
        String userLoginStr = userLogin.getValue();
        if (!StringUtils.isEmpty(userLoginStr))
            fieldValues.put("userLogin", userLoginStr.toLowerCase());
        String userNameStr = userName.getValue();
        if (!StringUtils.isEmpty(userNameStr))
            fieldValues.put("userName", userNameStr.toLowerCase());
        String userAddressStr = userAddress.getValue();
        if (!StringUtils.isEmpty(userAddressStr))
            fieldValues.put("userAddress", userAddressStr.toLowerCase());
        String userInfoStr = userInfo.getValue();
        if (!StringUtils.isEmpty(userInfoStr))
            fieldValues.put("userInfo", userInfoStr.toLowerCase());
        sessionsDs.refresh(fieldValues);
    }

    public void clearTextFields() {
        userLogin.setValue("");
        userName.setValue("");
        userAddress.setValue("");
        userInfo.setValue("");
        refresh();
    }

    public void message() {
        final Set<UserSessionEntity> selected = sessionsTable.getSelected();
        final Set<UserSessionEntity> all = new HashSet<>(sessionsDs.getItems());

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
                    sessionsTable.requestFocus();
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
                                sessionsTable.requestFocus();
                            }
                        },
                        new DialogAction(DialogAction.Type.CANCEL)
                }
        );
    }
}