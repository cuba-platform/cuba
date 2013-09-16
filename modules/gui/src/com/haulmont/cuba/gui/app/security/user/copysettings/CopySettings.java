/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.user.copysettings;

import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
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

    protected Set<User> users;

    @Override
    public void init(Map<String, Object> params) {
        users = (Set<User>) params.get("users");

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
                                    new DialogAction(DialogAction.Type.YES) {
                                        public void actionPerform(Component component) {
                                            copySettings();
                                        }
                                    },
                                    new DialogAction(DialogAction.Type.NO)
                            }
                    );
                }
            }
        });

        Button cancelBtn = getComponent("cancelBtn");
        cancelBtn.setAction(new AbstractAction("cancelBtn") {
            @Override
            public void actionPerform(Component component) {
                close("cancel");
            }
        });
    }

    private void copySettings() {
        UserSettingService settingsService = ServiceLocator.lookup(UserSettingService.NAME);
        User fromUser = usersDs.getItem();
        for (User user : users) {
            if (!user.equals(fromUser))
                settingsService.copySettings(fromUser, user);
        }
        close("ok");
    }
}
