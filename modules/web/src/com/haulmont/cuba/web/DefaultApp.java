/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class DefaultApp extends App implements ConnectionListener {

    private static final long serialVersionUID = 70273562618123015L;

    private static Log log = LogFactory.getLog(DefaultApp.class);

    // Login on start only on first request from user
    protected boolean tryLoginOnStart = true;

    public DefaultApp() {
    }

    @Override
    protected Connection createConnection() {
        Connection connection = new DefaultConnection();
        connection.addListener(this);
        return connection;
    }

    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            log.debug("Creating AppWindow");

            UIView appWindow = createAppWindow();
            showView(appWindow);

            initExceptionHandlers(true);

            if (linkHandler != null) {
                linkHandler.handle();
                linkHandler = null;
            }

            afterLoggedIn();
        } else {
            log.debug("Closing all windows");
            getWindowManager().closeAll();

            UIView window = createLoginWindow();
            showView(window);

            initExceptionHandlers(false);
        }
    }

    @Override
    protected void initView() {
        if (!connection.isConnected()) {
            if (!loginOnStart())
                showView(createLoginWindow());
        } else
            showView(createAppWindow());
    }

    /**
     * Should be overridden in descendant to create an application-specific login window
     *
     * @return Login form
     */
    protected UIView createLoginWindow() {
        return new LoginWindow(this, connection);
    }

    protected UIView createAppWindow() {
        AppWindow window = new AppWindow(connection);

        CubaTimer timer = createSessionPingTimer();
        if (timer != null) {
            window.addTimer(timer);

            timer.start();
        }

        return window;
    }

    protected CubaTimer createSessionPingTimer() {
        int sessionExpirationTimeout = webConfig.getHttpSessionExpirationTimeoutSec();
        int sessionPingPeriod = sessionExpirationTimeout / 3;
        if (sessionPingPeriod > 0) {
            CubaTimer timer = new CubaTimer();
            timer.setRepeating(true);
            timer.setDelay(sessionPingPeriod * 1000);
            timer.addTimerListener(new CubaTimer.TimerListener() {
                @Override
                public void onTimer(CubaTimer timer) {
                    log.debug("Ping session");
                    UserSessionService service = AppBeans.get(UserSessionService.NAME);
                    String message = service.getMessages();
                    if (message != null) {
                        App.getInstance().getWindowManager().showNotification(message, IFrame.NotificationType.ERROR);
                    }
                }

                @Override
                public void onStopTimer(CubaTimer timer) {
                }
            });
            return timer;
        }
        return null;
    }

    /**
     * Perform actions after success login
     */
    protected void afterLoggedIn() {
        if (!webAuthConfig.getUseActiveDirectory()) {
            final User user = AppBeans.get(UserSessionSource.class).getUserSession().getUser();
            // Change password on logon
            if (Boolean.TRUE.equals(user.getChangePasswordAtNextLogon())) {
                final WebWindowManager wm = getWindowManager();
                for (com.haulmont.cuba.gui.components.Window window : wm.getOpenWindows())
                    window.setEnabled(false);

                WindowInfo changePasswordDialog = AppBeans.get(WindowConfig.class).getWindowInfo("sec$User.changePassw");
                wm.getDialogParams().setCloseable(false);
                Map<String, Object> params = Collections.singletonMap("cancelEnabled", (Object) Boolean.FALSE);
                com.haulmont.cuba.gui.components.Window changePasswordWindow = wm.openEditor(changePasswordDialog, user,
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
                && ActiveDirectoryHelper.useActiveDirectory()) {

            String userName = principal.getName();
            log.debug("Trying to login ActiveDirectory as " + userName);
            try {
                ((ActiveDirectoryConnection) connection).loginActiveDirectory(userName, locale);

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
}