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

import com.haulmont.cuba.core.app.ResourceRepositoryService;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.gui.config.ActionsConfig;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.resource.Messages;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.Terminal;
import com.itmill.toolkit.terminal.gwt.server.WebBrowser;
import com.itmill.toolkit.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application implements ConnectionListener, ApplicationContext.TransactionListener
{
    private Log log = LogFactory.getLog(App.class);

    private Connection connection;

    private MenuConfig menuConfig;
    private ActionsConfig actionConfig;

    private ScreenManager screenManager;

    private AppLog appLog;

    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();

    private boolean principalIsWrong;

    static {
        SecurityAssociation.setServer();
    }

    public App() {
        appLog = new AppLog();
        connection = new Connection();
        connection.addListener(this);
        screenManager = new ScreenManager(this);
    }

    public void init() {
        log.debug("Initializing application");

        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(this);

        LoginWindow window = createLoginWindow();
        setMainWindow(window);
    }

    public static App getInstance() {
        return currentApp.get();
    }

    protected LoginWindow createLoginWindow() {
        return new LoginWindow(this, connection);
    }

    protected AppWindow createAppWindow() {
        return new AppWindow(connection);
    }

    public AppWindow getAppWindow() {
        return (AppWindow) getMainWindow();
    }

    public Connection getConnection() {
        return connection;
    }

    public MenuConfig getMenuConfig() {
        if (menuConfig == null) {
            if (!connection.isConnected())
                throw new RuntimeException("Not connected");
            menuConfig = new MenuConfig(ClientType.WEB, connection.getSession());
            menuConfig.loadConfig(getClass().getName(), getActionsConfig(), getResourceBundle(), getMenuConfigXml());
        }

        return menuConfig;
    }

    public ActionsConfig getActionsConfig() {
        if (actionConfig == null) {
            actionConfig = new ActionsConfig();
            actionConfig.loadConfig(getClass().getName(), getResourceBundle(), getActionsConfigXml());
        }

        return actionConfig;
    }

    protected String getActionsConfigXml() {
        ResourceRepositoryService rrs = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);
        return rrs.getResAsString("cuba/client/web/action-config.xml");
    }

    protected String getMenuConfigXml() {
        ResourceRepositoryService rrs = ServiceLocator.lookup(ResourceRepositoryService.JNDI_NAME);
        return rrs.getResAsString("cuba/client/web/menu-config.xml");
    }

    protected ResourceBundle getResourceBundle() {
        return Messages.getResourceBundle();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public AppLog getAppLog() {
        return appLog;
    }

    public void connectionStateChanged(Connection connection) {
        if (connection.isConnected()) {
            Window window = createAppWindow();
            setMainWindow(window);
        }
        else {
            menuConfig = null;
            Window window = createLoginWindow();
            setMainWindow(window);
        }
    }

    public void terminalError(Terminal.ErrorEvent event) {
        super.terminalError(event);
        getAppLog().log(event);
    }

    public void transactionStart(Application application, Object transactionData) {
        HttpServletRequest request = (HttpServletRequest) transactionData;
        if (log.isTraceEnabled()) {
            log.trace("requestStart: [@" + Integer.toHexString(System.identityHashCode(request)) + "] " +
                    request.getRequestURI() +
                    (request.getUserPrincipal() != null ? " [" + request.getUserPrincipal() + "]" : "") +
                    " from " + request.getRemoteAddr());
        }
        if (application == App.this) {
            currentApp.set((App) application);
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

        if (!connection.isConnected()
                && request.getUserPrincipal() != null
                && !principalIsWrong
                && ActiveDirectoryHelper.useActiveDirectory())
        {
            String userName = request.getUserPrincipal().getName();
            try {
                connection.loginActiveDirectory(userName);
                principalIsWrong = false;
            } catch (LoginException e) {
                principalIsWrong = true;
            }
        }

        if (connection.isConnected()) {
            UserSession userSession = connection.getSession();
            if (userSession != null) {
                SecurityAssociation.setPrincipal(new SimplePrincipal(userSession.getLogin()));
                SecurityAssociation.setCredential(userSession.getId().toString().toCharArray());
            }
        }
    }

    public void transactionEnd(Application application, Object transactionData) {
        if (application == App.this) {
            currentApp.set(null);
            currentApp.remove();
        }
        if (log.isTraceEnabled()) {
            log.trace("requestEnd: [@" + Integer.toHexString(System.identityHashCode(transactionData)) + "]");
        }
    }
}
