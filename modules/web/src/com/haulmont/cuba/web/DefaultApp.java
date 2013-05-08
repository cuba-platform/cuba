/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.sys.MessagesClientImpl;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
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
        MessagesClientImpl messagesClient = AppBeans.get(Messages.NAME);

        if (connection.isConnected()) {
            log.debug("Creating AppWindow");

            messagesClient.setRemoteSearch(true);

            UIView appWindow = createAppWindow();
            showView(appWindow);

            initExceptionHandlers(true);

//            if (linkHandler != null) {
//                linkHandler.handle();
//                linkHandler = null;
//            }

            afterLoggedIn();
        } else {
            log.debug("Closing all windows");
//            getWindowManager().closeAll();

            messagesClient.setRemoteSearch(false);

            UIView window = createLoginWindow();
            showView(window);

            initExceptionHandlers(false);
        }
    }

    @Override
    protected void initView() {
        if (!connection.isConnected())
            showView(createLoginWindow());
        else
            showView(createAppWindow());
    }

    /**
     * Should be overridden in descendant to create an application-specific login window
     *
     * @return Login form
     */
    protected UIView createLoginWindow() {
        LoginWindow window = new LoginWindow(this, connection);

//  vaadin7 use heartbeat
//        Timer timer = createSessionPingTimer(false);
//        if (timer != null)
//            timers.add(timer, window);

        return window;
    }

    protected UIView createAppWindow() {
        AppWindow window = new AppWindow(connection);

//  vaadin7 use heartbeat
//        Timer timer = createSessionPingTimer(true);
//        if (timer != null)
//            timers.add(timer, appWindow);

        return window;
    }

    /**
     * Perform actions after success login
     */
    protected void afterLoggedIn() {
        if (!webConfig.getUseActiveDirectory()) {
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
    protected boolean loginOnStart(HttpServletRequest request) {
        if (tryLoginOnStart &&
                request.getUserPrincipal() != null
//                && !principalIsWrong
                && ActiveDirectoryHelper.useActiveDirectory()) {

            String userName = request.getUserPrincipal().getName();
            log.debug("Trying to login ActiveDirectory as " + userName);
            try {
                ((ActiveDirectoryConnection) connection).loginActiveDirectory(userName, request.getLocale());
//                principalIsWrong = false;

                return true;
            } catch (LoginException e) {
//                principalIsWrong = true;
            } finally {
                // Close attempt login on start
                tryLoginOnStart = false;
            }
        }

        return false;
    }
}