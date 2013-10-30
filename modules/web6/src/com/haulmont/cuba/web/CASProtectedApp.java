/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.LoginException;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;

/**
 * @author krokhin
 * @version $Id$
 */
public class CASProtectedApp extends App implements ConnectionListener {

    private static final long serialVersionUID = -6926944868742949956L;

    private static Log log = LogFactory.getLog(CASProtectedApp.class);

    @Override
    protected Connection createConnection() {
        Connection connection = new CASProtectedConnection();
        connection.addListener(this);
        return connection;
    }

    @Override
    public void init() {
        log.debug("Initializing application");
        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(this);
    }

    @Override
    protected boolean loginOnStart(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            if (principal != null && principal.getName() != null && !connection.isConnected()) {
                connection.login(principal.getName(), null, request.getLocale());

                return true;
            }
        } catch (LoginException e) {
            //do nothing
        }
        return false;
    }

    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            log.debug("Creating AppWindow");

            getTimers().stopAll();

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
        } else {
            //todo think what I should to do in this case?
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

                return appWindow;
            } else {
                //todo think what I should to do in this case?
            }
        }
        return window;
    }
}