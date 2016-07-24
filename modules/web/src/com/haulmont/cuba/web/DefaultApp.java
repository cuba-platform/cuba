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
package com.haulmont.cuba.web;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.loginwindow.AppLoginWindow;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.server.WrappedSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 * Default {@link App} implementation that shows {@link AppLoginWindow} on start.
 * Supports SSO through external authentication.
 */
@Component(App.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefaultApp extends App implements ConnectionListener, UserSubstitutionListener {

    private static final Logger log = LoggerFactory.getLogger(DefaultApp.class);

    // Login on start only on first request from user
    protected boolean tryLoginOnStart = true;

    @Inject
    protected UserSessionSource userSessionSource;

    public DefaultApp() {
    }

    @Override
    protected Connection createConnection() {
        Connection connection = super.createConnection();
        connection.addConnectionListener(this);
        return connection;
    }

    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        log.debug("connectionStateChanged connected: {}, authenticated: {}",
                connection.isConnected(), connection.isAuthenticated());

        cleanupBackgroundTasks();
        closeAllWindows();
        clearSettingsCache();

        if (connection.isConnected()) {
            UserSession userSession = connection.getSession();
            if (userSession == null) {
                throw new IllegalStateException("Unable to obtain session from connected Connection");
            }
            setLocale(userSession.getLocale());

            // substitution listeners are cleared by connection on logout
            connection.addSubstitutionListener(this);

            if (webConfig.getUseSessionFixationProtection()) {
                VaadinService.reinitializeSession(VaadinService.getCurrentRequest());

                WrappedSession session = VaadinSession.getCurrent().getSession();
                int timeout = webConfig.getHttpSessionExpirationTimeoutSec();
                session.setMaxInactiveInterval(timeout);

                HttpSession httpSession = session instanceof WrappedHttpSession ?
                        ((WrappedHttpSession) session).getHttpSession() : null;
                log.debug("Session reinitialized: HttpSession={}, timeout={}sec, UserSession={}",
                        httpSession, timeout, connection.getSession());
            }

            initExceptionHandlers(true);

            AppUI currentUi = AppUI.getCurrent();
            createTopLevelWindow(currentUi);

            for (AppUI ui : getAppUIs()) {
                if (currentUi != ui) {
                    ui.accessSynchronously(() ->
                            createTopLevelWindow(ui)
                    );
                }
            }

            if (linkHandler != null) {
                linkHandler.handle();
                linkHandler = null;
            }

            afterLoggedIn();
        } else {
            initExceptionHandlers(false);

            setLocale(resolveLocale(null));

            getConnection().loginAnonymous(getLocale());
        }
    }

    @Override
    protected String routeTopLevelWindowId() {
        if (connection.isConnected() && connection.isAuthenticated()) {
            return "mainWindow";
        } else {
            return "loginWindow";
        }
    }

    /**
     * Perform actions after successful login
     */
    protected void afterLoggedIn() {
        if (connection.isAuthenticated() && !webAuthConfig.getExternalAuthentication()) {
            User user = userSessionSource.getUserSession().getUser();
            // Change password on logon
            if (Boolean.TRUE.equals(user.getChangePasswordAtNextLogon())) {
                WebWindowManager wm = getWindowManager();
                for (Window window : wm.getOpenWindows()) {
                    window.setEnabled(false);
                }

                WindowInfo changePasswordDialog = windowConfig.getWindowInfo("sec$User.changePassword");

                Window changePasswordWindow = wm.openWindow(changePasswordDialog,
                        OpenType.DIALOG.closeable(false),
                        ParamsMap.of("cancelEnabled", Boolean.FALSE));

                changePasswordWindow.addCloseListener(actionId -> {
                    for (Window window : wm.getOpenWindows()) {
                        window.setEnabled(true);
                    }
                });
            }
        }
    }

    @Override
    protected boolean loginOnStart() {
        if (tryLoginOnStart
                && principal != null
                && webAuthConfig.getExternalAuthentication()) {

            String userName = principal.getName();
            log.debug("Trying to login after external authentication as {}", userName);
            try {
                ((ExternallyAuthenticatedConnection) connection).loginAfterExternalAuthentication(userName, getLocale());

                return true;
            } catch (LoginException e) {
                log.trace("Unable to login on start", e);
            } finally {
                // Close attempt login on start
                tryLoginOnStart = false;
            }
        }

        return false;
    }

    @Override
    public void userSubstituted(Connection connection) {
        cleanupBackgroundTasks();
        clearSettingsCache();
        closeAllWindows();

        AppUI currentUi = AppUI.getCurrent();
        createTopLevelWindow(currentUi);

        for (AppUI ui : getAppUIs()) {
            if (currentUi != ui) {
                ui.accessSynchronously(() ->
                        createTopLevelWindow(ui)
                );
            }
        }
    }
}