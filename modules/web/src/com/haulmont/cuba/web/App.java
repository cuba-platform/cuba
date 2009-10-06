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
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.exception.*;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Main class of the web application. Each client connection has its own App.
 * Use {@link #getInstance()} static method to obtain the reference to the current App instance
 * throughout the application code.
 * <p>
 * Specific application should inherit from this class and set derived class name
 * in <code>application</code> servlet parameter of <code>web.xml</code>
 */
@SuppressWarnings("serial")
public class App extends Application implements ConnectionListener, ApplicationContext.TransactionListener
{
    private Log log = LogFactory.getLog(App.class);

    private Connection connection;
    private WebWindowManager windowManager;

    private AppLog appLog;

    private ExceptionHandlers exceptionHandlers;

    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();

    private boolean principalIsWrong;

    private LinkHandler linkHandler;

    protected Map<String, Timer> idTimers = new HashMap<String, Timer>();
    protected Set<Timer> timers = new HashSet<Timer>();

    static {
        // set up system properties necessary for com.haulmont.cuba.gui.AppConfig
        System.setProperty(AppConfig.PERMISSION_CONFIG_XML_PROP, "cuba/permission-config.xml");
        System.setProperty(AppConfig.MENU_CONFIG_XML_PROP, "cuba/client/web/menu-config.xml");
        System.setProperty(AppConfig.WINDOW_CONFIG_XML_PROP, "cuba/client/web/screen-config.xml");
        System.setProperty(AppConfig.WINDOW_CONFIG_IMPL_PROP, "com.haulmont.cuba.web.WebWindowConfig");
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

    /**
     * Can be overridden in descendant to create an application-specific {@link WebWindowManager}
     */
    protected WebWindowManager createWindowManager() {
        return new WebWindowManager(this);
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

    /**
     * Current App instance. Can be invoked anywhere in application code.
     */
    public static App getInstance() {
        App app = currentApp.get();
        if (app == null)
            throw new IllegalStateException("No App bound to the current thread. This may be the result of hot-deployment.");
        return app;
    }

    /**
     * Can be overridden in descendant to add application-specific exception handlers
     */
    protected void initExceptionHandlers(boolean isConnected) {
        if (isConnected) {
            exceptionHandlers.addHandler(new NoUserSessionHandler()); // must be the first handler
            exceptionHandlers.addHandler(new UniqueConstraintViolationHandler());
            exceptionHandlers.addHandler(new AccessDeniedHandler());
            exceptionHandlers.addHandler(new NoSuchScreenHandler());
            exceptionHandlers.addHandler(new DeletePolicyHandler());
        } else {
            exceptionHandlers.getHandlers().clear();
        }
    }

    /**
     * Should be overridden in descendant to create an application-specific login window
     */
    protected LoginWindow createLoginWindow() {
        return new LoginWindow(this, connection);
    }

    /**
     * Should be overridden in descendant to deploy views needed for main window
     */
    protected void deployViews() {
        MetadataProvider.getViewRepository().deployViews("/com/haulmont/cuba/web/app.views.xml");
    }

    /**
     * Should be overridden in descendant to create an application-specific main window
     */
    protected AppWindow createAppWindow() {
        return new AppWindow(connection);
    }

    public AppWindow getAppWindow() {
        return (AppWindow) getMainWindow();
    }

    /**
     * Get current connection object
     */
    public Connection getConnection() {
        return connection;                       
    }

    public WebWindowManager getWindowManager() {
        return windowManager;
    }

    public AppLog getAppLog() {
        return appLog;
    }

    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected()) {
            log.debug("Creating AppWindow");

            stopTimers();

            AppWindow window = createAppWindow();
            setMainWindow(window);

            connection.addListener(window);

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

            connection.removeListener(getAppWindow());

            Window window = createLoginWindow();
            setMainWindow(window);

            initExceptionHandlers(false);
        }
    }

    public void userSubstituted(Connection connection) {
    }

    public void terminalError(Terminal.ErrorEvent event) {
        if (event instanceof AbstractApplicationServlet.RequestError) {
            log.error("RequestError:", event.getThrowable());
        } else {
            exceptionHandlers.handle(event);
            getAppLog().log(event);
        }
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

    public void addTimer(final Timer timer) {
        addTimer(timer, null);
    }

    public void addTimer(final Timer timer, com.haulmont.cuba.gui.components.Window owner) {
        if (timers.add(timer)) {
            timer.addListener(new Timer.Listener() {
                public void onTimer(Timer timer) {
                }

                public void onStopTimer(Timer timer) {
                    timers.remove(timer);
                    if (timer instanceof WebTimer) {
                        idTimers.remove(((WebTimer) timer).getId());
                    }
                }
            });
            if (timer instanceof WebTimer) {
                final WebTimer webTimer = (WebTimer) timer;
                if (owner != null) {
                    owner.addListener(new com.haulmont.cuba.gui.components.Window.CloseListener() {
                        public void windowClosed(String actionId) {
                            timer.stopTimer();
                        }
                    });
                }
                if (webTimer.getId() != null) {
                    idTimers.put(webTimer.getId(), webTimer);
                }
            }
        }
    }

    private void stopTimers() {
        Set<Timer> timers = new HashSet<Timer>(this.timers);
        for (final Timer timer : timers) {
            if (!timer.isStopped()) {
                timer.stopTimer();
            }
        }
    }

    public Timer getTimer(String id) {
        return idTimers.get(id);
    }

    public Set<Timer> getApplicationTimers() {
        return Collections.unmodifiableSet(timers);
    }
}
