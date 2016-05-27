/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class SessionBrowser extends AbstractLookup {

    public interface Companion {
        void enableTextSelection(Table table);
    }

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected UserSessionService uss;

    @Inject
    protected Table<UserSessionEntity> sessionsTable;

    @Inject
    protected UserSessionsDatasource sessionsDs;

    @Inject
    protected Label lastUpdateTsLab;

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

        Companion companion = getCompanion();
        if (companion != null) {
            companion.enableTextSelection(sessionsTable);
        }

        sessionsDs.addCollectionChangeListener(e -> {
            String time = Datatypes.getNN(Date.class).format(sessionsDs.getUpdateTs(), userSessionSource.getLocale());
            lastUpdateTsLab.setValue(time);
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
        SessionMessageWindow window = (SessionMessageWindow) openWindow("sessionMessageWindow", OpenType.DIALOG, params);
        window.addCloseListener(actionId -> {
            String result = window.getResult();
            if (!StringUtils.isBlank(result)) {
                showNotification(result, NotificationType.TRAY);
                sessionsTable.requestFocus();
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
                        new DialogAction(Type.OK) {
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
                        new DialogAction(Type.CANCEL, Status.PRIMARY)
                }
        );
    }
}