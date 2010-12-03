/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 22.10.2010 17:15:47
 *
 * $Id: DefaultApp.java 3262 2010-11-26 06:41:45Z krokhin $
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class DefaultApp extends App implements ConnectionListener {

    private static Log log = LogFactory.getLog(DefaultApp.class);

    private boolean principalIsWrong;

    private static final long serialVersionUID = 70273562618123015L;

    @Override
    protected Connection createConnection() {
        Connection connection = new DefaultConnection();
        connection.addListener(this);
        return connection;
    }

    /**
     * Should be overridden in descendant to create an application-specific login window
     */
    protected LoginWindow createLoginWindow() {
        LoginWindow window = new LoginWindow(this, connection);
        return window;
    }

    public void init() {
        log.debug("Initializing application");

        AppConfig.getInstance().addGroovyImport(PersistenceHelper.class);

        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(this);

        LoginWindow window = createLoginWindow();
        setMainWindow(window);

        checkDeployedViews();

        String themeName = AppContext.getProperty(AppConfig.THEME_NAME_PROP);
        if (themeName == null) themeName = THEME_NAME;
        setTheme(themeName);
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

                connection.addListener(appWindow);

                return appWindow;
            } else {
                final Window loginWindow = createLoginWindow();
                loginWindow.setName(name);
                addWindow(loginWindow);

                return loginWindow;
            }
        }

        return window;
    }

    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            log.debug("Creating AppWindow");

            stopTimers();

            for (Object win : new ArrayList(getWindows())) {
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
        }
        else {
            log.debug("Closing all windows");
            getWindowManager().closeAll();

            stopTimers();

            for (Object win : new ArrayList(getWindows())) {
                removeWindow((Window) win);
            }

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


    protected boolean loginOnStart(HttpServletRequest request) {
        if (request.getUserPrincipal() != null
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
            }
        }

        return false;
    }

}
