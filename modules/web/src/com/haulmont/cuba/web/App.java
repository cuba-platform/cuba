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

import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.Terminal;
import com.itmill.toolkit.terminal.gwt.server.WebBrowser;
import com.itmill.toolkit.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class App extends Application implements ConnectionListener, ApplicationContext.TransactionListener
{
    private Log log = LogFactory.getLog(App.class);

    private Connection connection;
    private WindowManager windowManager;

    private AppLog appLog;

    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();

    private boolean principalIsWrong;

    static {
        System.setProperty(AppConfig.PERMISSION_CONFIG_XML_PROP, "cuba/permission-config.xml");
        System.setProperty(AppConfig.MENU_CONFIG_XML_PROP, "cuba/client/web/menu-config.xml");
        System.setProperty(AppConfig.WINDOW_CONFIG_XML_PROP, "cuba/client/web/screen-config.xml");
        System.setProperty(AppConfig.WINDOW_CONFIG_IMPL_PROP, "com.haulmont.cuba.web.WindowConfig");
        System.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
        System.setProperty(AppConfig.MESSAGES_PACK_PROP, "com.haulmont.cuba.web");
    }

    public App() {
        appLog = new AppLog();
        connection = new Connection();
        connection.addListener(this);
        windowManager = createWindowManager();
    }

    protected WindowManager createWindowManager() {
        return new WindowManager(this);
    }

    public void init() {
        log.debug("Initializing application");

        AppConfig.getInstance().addGroovyImport(PersistenceHelper.class);

        ApplicationContext appContext = getContext();
        appContext.addTransactionListener(this);

        LoginWindow window = createLoginWindow();
        setMainWindow(window);

        deployViews();
    }

    public static App getInstance() {
        return currentApp.get();
    }

    protected LoginWindow createLoginWindow() {
        return new LoginWindow(this, connection);
    }

    protected void deployViews() {
        //MetadataProvider.getViewRepository().deployViews("/com/haulmont/cuba/web/app/ui/security/user/user.views.xml");
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

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public AppLog getAppLog() {
        return appLog;
    }

    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            Window window = createAppWindow();
            setMainWindow(window);
        }
        else {
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
                && ActiveDirectoryHelper.useActiveDirectory()
                && !(request.getRequestURI().endsWith("/login") || request.getRequestURI().endsWith("/UIDL/")))
        {
            String userName = request.getUserPrincipal().getName();
            log.debug("Trying to login ActiveDirectory as " + userName);
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
                ServerSecurityUtils.setSecurityAssociation(userSession.getLogin(), userSession.getId());
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
