/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.user.copysettings;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CopySettings extends AbstractWindow {

    @Inject
    protected Datasource<User> usersDs;

    @Inject
    protected Button copyBtn;

    @Inject
    protected Button cancelBtn;

    @WindowParam(required = true)
    protected Set<User> users;

    @Inject
    protected ClientConfig clientConfig;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidthAuto();

        copyBtn.setAction(new AbstractAction("deployBtn") {
            @Override
            public void actionPerform(Component component) {
                if (usersDs.getItem() == null) {
                    showNotification(
                            getMessage("selectUser"), NotificationType.HUMANIZED);
                } else {
                    showOptionDialog(
                            getMessage("confirmCopy.title"),
                            getMessage("confirmCopy.msg"),
                            MessageType.CONFIRMATION,
                            new Action[]{
                                    new DialogAction(Type.YES) {
                                        @Override
                                        public void actionPerform(Component component) {
                                            copySettings();
                                        }
                                    },
                                    new DialogAction(Type.NO, Status.PRIMARY)
                            }
                    );
                }
            }
        });

        cancelBtn.setAction(new AbstractAction("cancelBtn") {
            @Override
            public void actionPerform(Component component) {
                close("cancel");
            }
        });
    }

    protected void copySettings() {
        UserSettingService settingsService = AppBeans.get(UserSettingService.NAME);
        User fromUser = usersDs.getItem();
        for (User user : users) {
            if (!user.equals(fromUser)) {
                settingsService.copySettings(fromUser, user);
            }
        }
        close("ok");
    }
}