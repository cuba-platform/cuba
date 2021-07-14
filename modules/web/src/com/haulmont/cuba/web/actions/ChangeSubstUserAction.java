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

package com.haulmont.cuba.web.actions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.sys.WebScreens;

public class ChangeSubstUserAction extends AbstractAction {
    protected User user;

    public ChangeSubstUserAction(User user) {
        super("changeSubstUserAction");

        this.user = user;

        setIconFromSet(CubaIcon.OK);
    }

    @Override
    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        AppUI ui = AppUI.getCurrent();

        if (!isUserActive(user)) {
            doRevert();
            ui.getNotifications().create(Notifications.NotificationType.ERROR)
                    .withCaption("User substitution is not allowed")
                    .withDescription(String.format("User '%s' is disabled", user.getName()))
                    .show();
        } else {
            WebScreens screens = (WebScreens) ui.getScreens();
            screens.checkModificationsAndCloseAll()
                    .then(() -> {
                        App app = ui.getApp();

                        try {
                            app.getConnection().substituteUser(user);
                            doAfterChangeUser();
                        } catch (javax.persistence.NoResultException e) {
                            Messages messages = AppBeans.get(Messages.NAME);
                            app.getWindowManager().showNotification(
                                    messages.formatMainMessage("substitutionNotPerformed", user.getName()),
                                    Frame.NotificationType.WARNING
                            );
                            doRevert();
                        }
                    })
                    .otherwise(this::doRevert);
        }
    }

    private static boolean isUserActive(User user) {
        return AppBeans.<AuthenticationService>get(AuthenticationService.NAME).isUserActive(user);
    }

    public void doAfterChangeUser() {
    }

    public void doRevert() {
    }
}