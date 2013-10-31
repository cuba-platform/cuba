/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.auth.ActiveDirectoryConnection;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class DefaultApp extends App implements ConnectionListener {

    private static final long serialVersionUID = 70273562618123015L;

    protected boolean principalIsWrong;

    private static Log log = LogFactory.getLog(DefaultApp.class);

    // Login on start only on first request from user
    protected boolean tryLoginOnStart = true;

    @Override
    protected Connection createConnection() {
        Connection connection = new DefaultConnection();
        connection.addListener(this);
        return connection;
    }

    /**
     * Should be overridden in descendant to create an application-specific login window
     *
     * @return Login form
     */
    protected LoginWindow createLoginWindow() {
        LoginWindow window = new LoginWindow(this, connection);

        Timer timer = createSessionPingTimer(false);
        if (timer != null)
            timers.add(timer, window);

        return window;
    }

    /**
     * Get or create new LoginWindow
     *
     * @return LoginWindow
     */
    private LoginWindow getLoginWindow() {
        for (Window win : getWindows()) {
            if (win instanceof LoginWindow)
                return (LoginWindow) win;
        }

        return createLoginWindow();
    }

    @Override
    public void init() {
        log.debug("Initializing application");

        //todo AppConfig.addGroovyImport(PersistenceHelper.class);

        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(this);

        LoginWindow window = createLoginWindow();
        setMainWindow(window);

        if (getTheme() == null) {
            String themeName = AppContext.getProperty("cuba.web.theme");
            if (themeName == null) themeName = THEME_NAME;
            setTheme(themeName);
        }
    }

    @Override
    public Window getWindow(String name) {
        Window window = super.getWindow(name);

        // it does not exist yet, create it.
        if (window == null) {
            if (connection.isConnected()) {
                final AppWindow appWindow = createAppWindow();
                appWindow.setName(name);
                addWindow(appWindow);
                appWindow.focus();

                return appWindow;
            } else {
                final Window loginWindow = getLoginWindow();
                removeWindow(loginWindow);

                loginWindow.setName(name);

                addWindow(loginWindow);

                return loginWindow;
            }
        }

        return window;
    }

    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            log.debug("Creating AppWindow");

            getTimers().stopAll();

            for (Object win : new ArrayList<Object>(getWindows())) {
                removeWindow((Window) win);
            }

            String name = currentWindowName.get();
            if (name == null)
                name = createWindowName(true);

            Window window = getWindow(name);

            setMainWindow(window);
            currentWindowName.set(window.getName());

            initExceptionHandlers(true);

            if (linkHandler != null) {
                linkHandler.handle();
                linkHandler = null;
            }

            afterLoggedIn();
        } else {
            log.debug("Closing all windows");
            cleanupBackgroundTasks();
            getTimers().stopAll();

            closeAllWindows();

            String name = currentWindowName.get();
            if (name == null)
                name = createWindowName(false);

            Window window = createLoginWindow();
            window.setName(name);
            setMainWindow(window);

            currentWindowName.set(window.getName());

            initExceptionHandlers(false);
        }
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
    protected boolean loginOnStart(HttpServletRequest request) {
        if (tryLoginOnStart &&
                request.getUserPrincipal() != null
                && !principalIsWrong
                && ActiveDirectoryHelper.useActiveDirectory()) {

            String userName = request.getUserPrincipal().getName();
            log.debug("Trying to login ActiveDirectory as " + userName);
            try {
                ((ActiveDirectoryConnection) connection).loginActiveDirectory(userName, request.getLocale());
                principalIsWrong = false;

                return true;
            } catch (LoginException e) {
                principalIsWrong = true;
            } finally {
                // Close attempt login on start
                tryLoginOnStart = false;
            }
        }

        return false;
    }
}