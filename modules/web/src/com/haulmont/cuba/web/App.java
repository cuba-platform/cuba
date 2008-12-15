/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2008 14:37:46
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.config.MenuConfig;
import com.haulmont.cuba.web.ScreenManager;
import com.haulmont.cuba.web.config.ActionConfig;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.terminal.Terminal;
import com.itmill.toolkit.terminal.gwt.server.WebBrowser;
import com.itmill.toolkit.service.ApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class App extends Application implements ConnectionListener
{
    private Log log = LogFactory.getLog(App.class);

    private Connection connection;

    private MenuConfig menuConfig;
    private ActionConfig actionConfig;

    private ScreenManager screenManager;

    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();

    static {
        SecurityAssociation.setServer();
    }

    public App() {
        connection = new Connection();
        connection.addListener(this);
        screenManager = new ScreenManager(this);
    }

    public void init() {
        log.debug("Initializing application");

        AppWindow appWindow = createAppWindow();
        connection.addListener(appWindow);

        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(new RequestListener());
    }

    public static App getInstance() {
        return currentApp.get();
    }

    protected AppWindow createAppWindow() {
        return new AppWindow(this);
    }

    public AppWindow getAppWindow() {
        return (AppWindow) getMainWindow();
    }

    public Connection getConnection() {
        return connection;
    }

    public MenuConfig getMenuConfig() {
        if (menuConfig == null)
            menuConfig = new MenuConfig(getActionConfig());
        return menuConfig;
    }

    public ActionConfig getActionConfig() {
        if (actionConfig == null)
            actionConfig = new ActionConfig();
        return actionConfig;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public void connectionStateChanged(Connection connection) {
        if (!connection.isConnected()) {
            menuConfig = null;
        }
    }

    private class RequestListener implements ApplicationContext.TransactionListener
    {
        public void transactionStart(Application application, Object transactionData) {
            if (log.isTraceEnabled()) {
                HttpServletRequest request = (HttpServletRequest) transactionData;
                log.trace("requestStart: " + request + " from " + request.getRemoteAddr());
            }
            if (application == App.this) {
                currentApp.set((App) application);
            }
            UserSession userSession = connection.getSession();
            if (userSession != null) {
                SecurityAssociation.setPrincipal(new SimplePrincipal(userSession.getLogin()));
                SecurityAssociation.setCredential(userSession.getId().toString().toCharArray());
            }
            Terminal terminal = application.getMainWindow().getTerminal();
            if (terminal != null) {
                if (terminal instanceof WebBrowser) {
                    Locale locale = ((WebBrowser) terminal).getLocale();
                    application.setLocale(locale);
                }
                else {
                    log.error("Unsupported terminal type: " + terminal);
                }
            }
        }

        public void transactionEnd(Application application, Object transactionData) {
            if (application == App.this) {
                currentApp.set(null);
                currentApp.remove();
            }
            if (log.isTraceEnabled()) {
                log.trace("requestEnd: " + transactionData);
            }
        }
    }
}
