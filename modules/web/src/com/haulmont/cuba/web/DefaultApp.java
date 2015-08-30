/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.auth.ExternallyAuthenticatedConnection;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * Default {@link App} implementation that shows {@link LoginWindow} on start.
 * Supports SSO through external authentication.
 *
 * @author gorodnov
 * @version $Id$
 */
public class DefaultApp extends App implements ConnectionListener, UserSubstitutionListener {

    private static Logger log = LoggerFactory.getLogger(DefaultApp.class);

    // Login on start only on first request from user
    protected boolean tryLoginOnStart = true;

    public DefaultApp() {
    }

    @Override
    protected Connection createConnection() {
        Connection connection = new DefaultConnection();
        connection.addConnectionListener(this);
        return connection;
    }

    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            // substitution listeners are cleared by connection on logout
            connection.addSubstitutionListener(this);

            ClientCacheManager clientCacheManager = AppBeans.get(ClientCacheManager.NAME);
            clientCacheManager.initialize();

            if (webConfig.getUseSessionFixationProtection()) {
                VaadinService.reinitializeSession(VaadinService.getCurrentRequest());

                WrappedSession session = VaadinSession.getCurrent().getSession();
                session.setMaxInactiveInterval(webConfig.getHttpSessionExpirationTimeoutSec());
            }

            log.debug("Creating AppWindow");

            initExceptionHandlers(true);
            for (final AppUI ui : getAppUIs()) {
                ui.accessSynchronously(new Runnable() {
                    @Override
                    public void run() {
                        AppWindow appWindow = createAppWindow(ui);
                        ui.showView(appWindow);
                    }
                });
            }

            if (linkHandler != null) {
                linkHandler.handle();
                linkHandler = null;
            }

            afterLoggedIn();
        } else {
            cleanupBackgroundTasks();
            closeAllWindows();
            clearSettingsCache();

            for (final AppUI ui : getAppUIs()) {
                ui.accessSynchronously(new Runnable() {
                    @Override
                    public void run() {
                        if (ui.isTestMode()) {
                            ui.getTestIdManager().reset();
                        }

                        UIView window = createLoginWindow(ui);
                        ui.showView(window);
                    }
                });
            }
            initExceptionHandlers(false);
        }
    }

    @Override
    protected void initView(AppUI ui) {
        if (connection.isAlive() || loginOnStart()) {
            ui.showView(createAppWindow(ui));
        } else {
            ui.showView(createLoginWindow(ui));
        }
    }

    /**
     * Perform actions after successful login
     */
    protected void afterLoggedIn() {
        if (!webAuthConfig.getExternalAuthentication()) {
            UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
            final User user = sessionSource.getUserSession().getUser();
            // Change password on logon
            if (Boolean.TRUE.equals(user.getChangePasswordAtNextLogon())) {
                final WebWindowManager wm = getWindowManager();
                for (com.haulmont.cuba.gui.components.Window window : wm.getOpenWindows())
                    window.setEnabled(false);

                WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
                WindowInfo changePasswordDialog = windowConfig.getWindowInfo("sec$User.changePassword");
                wm.getDialogParams().setCloseable(false);
                Map<String, Object> params = Collections.singletonMap("cancelEnabled", (Object) Boolean.FALSE);
                com.haulmont.cuba.gui.components.Window changePasswordWindow = wm.openWindow(changePasswordDialog,
                        WindowManager.OpenType.DIALOG, params);

                changePasswordWindow.addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        for (com.haulmont.cuba.gui.components.Window window : wm.getOpenWindows())
                            window.setEnabled(true);
                    }
                });
            }
        }
    }

    @Override
    protected boolean loginOnStart() {
        if (tryLoginOnStart &&
                principal != null
                && webAuthConfig.getExternalAuthentication()) {

            String userName = principal.getName();
            log.debug("Trying to login after external authentication as " + userName);
            try {
                ((ExternallyAuthenticatedConnection) connection).loginAfterExternalAuthentication(userName, locale);

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

        for (final AppUI ui : getAppUIs()) {
            ui.accessSynchronously(new Runnable() {
                @Override
                public void run() {
                    if (ui.isTestMode()) {
                        ui.getTestIdManager().reset();
                    }

                    ui.showView(createAppWindow(ui));
                }
            });
        }
    }
}