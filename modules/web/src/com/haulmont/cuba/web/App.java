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
import com.haulmont.cuba.web.exception.ExceptionHandlers;
import com.haulmont.cuba.web.exception.UniqueConstraintViolationHandler;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.itmill.toolkit.Application;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.Terminal;
import com.itmill.toolkit.terminal.gwt.server.WebBrowser;
import com.itmill.toolkit.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class App extends Application implements ConnectionListener, ApplicationContext.TransactionListener
{
    private Log log = LogFactory.getLog(App.class);

    private Connection connection;
    private WindowManager windowManager;

    private AppLog appLog;

    private ExceptionHandlers exceptionHandlers;

    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();

    private boolean principalIsWrong;

    private LinkHandler linkHandler;

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
        exceptionHandlers = new ExceptionHandlers(this);
    }

    public static Application.SystemMessages getSystemMessages() {
        Application.CustomizedSystemMessages msgs = new Application.CustomizedSystemMessages();
        msgs.setInternalErrorURL("/cuba?restartApplication");
        return msgs;
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

    protected void initExceptionHandlers(boolean isConnected) {
        if (isConnected) {
            exceptionHandlers.addHandler(new UniqueConstraintViolationHandler());
        } else {
            exceptionHandlers.getHandlers().clear();
        }
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
            initExceptionHandlers(true);
            if (linkHandler != null) {
                linkHandler.handle();
                linkHandler = null;
            }
        }
        else {
            Window window = createLoginWindow();
            setMainWindow(window);
            initExceptionHandlers(false);
        }
    }

    public void terminalError(Terminal.ErrorEvent event) {
//        super.terminalError(event);
        exceptionHandlers.handle(event);
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
        application.setLocale(request.getLocale());

        String requestURI = request.getRequestURI();

        if (!connection.isConnected()
                && request.getUserPrincipal() != null
                && !principalIsWrong
                && ActiveDirectoryHelper.useActiveDirectory()
                && !(requestURI.endsWith("/login") || requestURI.endsWith("/UIDL/")))
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
                ServerSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());
            }
        }

        if (requestURI.endsWith("/open") && !requestURI.contains("/UIDL/")) {
            Map<String, String> params = new HashMap<String, String>();
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = (String) parameterNames.nextElement();
                params.put(name, request.getParameter(name));
            }
            LinkHandler linkHandler = new LinkHandler(this, params);
            if (connection.isConnected())
                linkHandler.handle();
            else
                this.linkHandler = linkHandler;
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
