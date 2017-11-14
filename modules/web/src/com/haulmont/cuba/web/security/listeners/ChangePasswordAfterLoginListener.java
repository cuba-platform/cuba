/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.security.listeners;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.security.events.AppLoggedInEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.haulmont.cuba.web.security.ExternalUserCredentials.isLoggedInWithExternalAuth;

@Component("cuba_ChangePasswordAfterLoginListener")
public class ChangePasswordAfterLoginListener implements ApplicationListener<AppLoggedInEvent> {
    @Inject
    protected WindowConfig windowConfig;

    @Override
    public void onApplicationEvent(AppLoggedInEvent event) {
        App app = event.getApp();
        Connection connection = app.getConnection();

        if (connection.isAuthenticated()
                && !isLoggedInWithExternalAuth(connection.getSessionNN())) {
            User user = connection.getSessionNN().getUser();
            // Change password on logon
            if (Boolean.TRUE.equals(user.getChangePasswordAtNextLogon())) {
                WebWindowManager wm = app.getWindowManager();
                for (Window window : wm.getOpenWindows()) {
                    window.setEnabled(false);
                }

                WindowInfo changePasswordDialog = windowConfig.getWindowInfo("sec$User.changePassword");

                Window changePasswordWindow = wm.openWindow(changePasswordDialog,
                        WindowManager.OpenType.DIALOG.closeable(false),
                        ParamsMap.of("cancelEnabled", Boolean.FALSE));

                changePasswordWindow.addCloseListener(actionId -> {
                    for (Window window : wm.getOpenWindows()) {
                        window.setEnabled(true);
                    }
                });
            }
        }
    }
}