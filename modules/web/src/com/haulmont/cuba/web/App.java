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

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.exception.*;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.sys.WebSecurityUtils;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileOutputStream;

/**
 * Main class of the web application. Each client connection has its own App.
 * Use {@link #getInstance()} static method to obtain the reference to the current App instance
 * throughout the application code.
 * <p>
 * Specific application should inherit from this class and set derived class name
 * in <code>application</code> servlet parameter of <code>web.xml</code>
 */
public class App extends Application
        implements ConnectionListener, ApplicationContext.TransactionListener, HttpServletRequestListener {
    private static final long serialVersionUID = -3435976475534930050L;

    private static final Pattern WIN_PATTERN = Pattern.compile("win([0-9]{1,4})");

    private static Log log = LogFactory.getLog(App.class);

    public static final String THEME_NAME = "blacklabel";

    protected Connection connection;
    private WebWindowManager windowManager;

    private AppLog appLog;

    protected ExceptionHandlers exceptionHandlers;

    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();

    private transient ThreadLocal<String> currentWindowName = new ThreadLocal<String>();

    private boolean principalIsWrong;

    private LinkHandler linkHandler;

    protected Map<Window, WindowTimers> windowTimers = new HashMap<Window, WindowTimers>();
    protected Map<Timer, Window> timerWindow = new HashMap<Timer, Window>();

    protected transient Map<Object, Long> requestStartTimes = new WeakHashMap<Object, Long>();

    private static volatile boolean viewsDeployed;

    private volatile String contextName;

    private transient HttpServletResponse response;
    
    private AppCookies cookies;

    static {
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }

    public App() {
        appLog = new AppLog();
        connection = new Connection();
        connection.addListener(this);
        windowManager = createWindowManager();
        exceptionHandlers = new ExceptionHandlers(this);
        cookies = new AppCookies() {
            protected void addCookie(Cookie cookie) {
                response.addCookie(cookie);
            }
        };
        cookies.setCookiesEnabled(true);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        currentWindowName = new ThreadLocal<String>();
        requestStartTimes = new WeakHashMap<Object, Long>();
    }

    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        cookies.updateCookies(request);
    }

    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
        //do nothing
    }

    public static Application.SystemMessages getSystemMessages() {
        String webContext = AppContext.getProperty("cuba.webContextName");
        Application.CustomizedSystemMessages msgs = new Application.CustomizedSystemMessages();
        msgs.setInternalErrorURL("/" + webContext + "?restartApplication");
        msgs.setOutOfSyncNotificationEnabled(false);
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

        if (!viewsDeployed) {
            deployViews();
            viewsDeployed = true;
        }

        String themeName = AppContext.getProperty(AppConfig.THEME_NAME_PROP);
        if (themeName == null) themeName = THEME_NAME;
        setTheme(themeName);
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
            exceptionHandlers.addHandler(new SilentExceptionHandler());
            exceptionHandlers.addHandler(new UniqueConstraintViolationHandler());
            exceptionHandlers.addHandler(new AccessDeniedHandler());
            exceptionHandlers.addHandler(new NoSuchScreenHandler());
            exceptionHandlers.addHandler(new DeletePolicyHandler());
            exceptionHandlers.addHandler(new NumericOverflowExceptionHandler());
            exceptionHandlers.addHandler(new OptimisticExceptionHandler());
            exceptionHandlers.addHandler(new JPAOptimisticExceptionHandler());
        } else {
            exceptionHandlers.getHandlers().clear();
        }
    }

    /**
     * Should be overridden in descendant to create an application-specific login window
     */
    protected LoginWindow createLoginWindow() {
        LoginWindow window = new LoginWindow(this, connection);
        return window;
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
        AppWindow appWindow = new AppWindow(connection);
        return appWindow;
    }

    public AppWindow getAppWindow() {
        String name = currentWindowName.get();
        //noinspection deprecation
        return (AppWindow) (name == null ? getMainWindow() : getWindow(name));
    }

    /**
     * Don't use this method in application code.<br>
     * Use {@link #getAppWindow} instead
     */
    @Deprecated
    @Override
    public Window getMainWindow() {
        return super.getMainWindow();
    }

    @Override
    public void removeWindow(Window window) {
        super.removeWindow(window);
        if (window instanceof AppWindow) {
            connection.removeListener((AppWindow) window);
        }
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

            for (Object win : new ArrayList(getWindows())) {
                if (win instanceof AppWindow)
                    removeWindow((Window) win);
            }

            String name = createWindowName(true);

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
                if (win instanceof LoginWindow)
                    removeWindow((Window) win);
            }

            String name = createWindowName(false);

            Window window = createLoginWindow();
            window.setName(name);
            setMainWindow(window);

            currentWindowName.set(window.getName());

            initExceptionHandlers(false);
        }
    }

    private String createWindowName(boolean main) {
        String name = main ? AppContext.getProperty("cuba.web.mainWindowName") : AppContext.getProperty("cuba.web.loginWindowName");
        if (StringUtils.isBlank(name))
            name = GlobalUtils.generateWebWindowName();
        return name;
    }

    public void userSubstituted(Connection connection) {
    }

    public void terminalError(Terminal.ErrorEvent event) {
        GlobalConfig config = ConfigProvider.getConfig(GlobalConfig.class);
        if (config.getTestMode()) {
            String fileName = AppContext.getProperty("cuba.testModeExceptionLog");
            if (!StringUtils.isBlank(fileName)) {
                try {
                    FileOutputStream stream = new FileOutputStream(fileName);
                    try {
                        stream.write(ExceptionUtils.getStackTrace(event.getThrowable()).getBytes());
                    } finally {
                        stream.close();
                    }
                } catch (Exception e) {
                    log.debug(e);
                }
            }
        }

        if (event instanceof AbstractApplicationServlet.RequestError) {
            log.error("RequestError:", event.getThrowable());
        } else {
            exceptionHandlers.handle(event);
            getAppLog().log(event);
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

                connection.addListener(appWindow);

                return appWindow;
            } else {
                String newWindowName = createWindowName(false);

                final Window loginWindow = createLoginWindow();
                loginWindow.setName(newWindowName);
                addWindow(loginWindow);

                loginWindow.open(new ExternalResource(loginWindow.getURL()));

                return loginWindow;
            }
        }

        return window;
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

        if (contextName == null) {
            contextName = request.getContextPath().substring(1);
        }

        String requestURI = request.getRequestURI();

        setupCurrentWindowName(requestURI);

        if (!connection.isConnected()
                && request.getUserPrincipal() != null
                && !principalIsWrong
                && ActiveDirectoryHelper.useActiveDirectory()
                && !(requestURI.endsWith("/login") || requestURI.endsWith("/UIDL/")))
        {
            String userName = request.getUserPrincipal().getName();
            log.debug("Trying to login ActiveDirectory as " + userName);
            try {
                connection.loginActiveDirectory(userName, request.getLocale());
                principalIsWrong = false;
                setupCurrentWindowName(requestURI);
            } catch (LoginException e) {
                principalIsWrong = true;
            }
        }

        if (connection.isConnected()) {
            UserSession userSession = connection.getSession();
            if (userSession != null) {
                WebSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());
                application.setLocale(userSession.getLocale());
            }
            requestStartTimes.put(transactionData, System.currentTimeMillis());
        }

        processExternalLink(request, requestURI);
    }


    private void setupCurrentWindowName(String requestURI) {
        //noinspection deprecation
        currentWindowName.set(getMainWindow() == null ? null : getMainWindow().getName());

        if (connection.isConnected()) {
            String[] parts = requestURI.split("/");
            boolean contextFound = false;
            for (String part : parts) {
                if (StringUtils.isEmpty(part)) {
                    continue;
                }
                if (part.equals(contextName) && !contextFound) {
                    contextFound = true;
                    continue;
                }
                if (contextFound && part.equals("UIDL")) {
                    continue;
                }
                Matcher m = WIN_PATTERN.matcher(part);
                if (m.matches()) {
                    currentWindowName.set(part);
                    break;
                }
            }
        }
    }
    /*
    private void setupCurrentWindowName(String requestURI) {
        //noinspection deprecation
        currentWindowName.set(getMainWindow() == null ? null : getMainWindow().getName());

        if (connection.isConnected()) {
            String[] parts = requestURI.split("/");
            boolean contextFound = false;
            for (String part : parts) {
                if (StringUtils.isEmpty(part)) {
                    continue;
                }
                if (part.equals(contextName) && !contextFound) {
                    contextFound = true;
                    continue;
                }
                if (contextFound && part.equals("UIDL")) {
                    continue;
                }
                if (!part.startsWith("open")) {
                    currentWindowName.set(part);
                }
                break;
            }
        }
    }*/

    private void processExternalLink(HttpServletRequest request, String requestURI) {
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
        Long start = requestStartTimes.remove(transactionData);
        if (start != null) {
            long t = System.currentTimeMillis() - start;
            WebConfig config = ConfigProvider.getConfig(WebConfig.class);
            if (t > (config.getLogLongRequestsThresholdSec() * 1000)) {
                log.warn(String.format("Too long request processing [%d ms]: ip=%s, url=%s",
                        t, ((HttpServletRequest)transactionData).getRemoteAddr(), ((HttpServletRequest)transactionData).getRequestURI()));
            }
        }

        if (application == App.this) {
            currentApp.set(null);
            currentApp.remove();
        }

        WebSecurityUtils.clearSecurityAssociation();

        if (log.isTraceEnabled()) {
            log.trace("requestEnd: [@" + Integer.toHexString(System.identityHashCode(transactionData)) + "]");
        }
    }

    /**
     * Adds a timer on the application level
     * @param timer new timer
     */
    public void addTimer(final Timer timer) {
        addTimer(timer, null, getCurrentWindow());
    }

    /**
     * Adds a timer for the defined window
     * @param timer new timer
     * @param owner component that owns a timer
     */
    public void addTimer(final Timer timer, com.haulmont.cuba.gui.components.Window owner) {
        addTimer(timer, owner, getCurrentWindow());
    }

    /**
     * Do not use this method in application code
     */
    public void addTimer(final Timer timer, Window mainWindow) {
        addTimer(timer, null, mainWindow);
    }

    /**
     * Do not use this method in application code
     */
    public void addTimer(final Timer timer, com.haulmont.cuba.gui.components.Window owner, Window mainWindow) {
        WindowTimers wt = windowTimers.get(mainWindow);
        if (wt == null) {
            wt = new WindowTimers();
            windowTimers.put(mainWindow, wt);
        }

        if (wt.timers.add(timer))
        {
            timerWindow.put(timer, mainWindow);

            timer.addListener(new Timer.Listener() {
                public void onTimer(Timer timer) {
                }

                public void onStopTimer(Timer timer) {
                    Window window = timerWindow.get(timer);
                    if (window != null)
                    {
                        WindowTimers wt = windowTimers.get(window);
                        if (wt != null) {
                            wt.timers.remove(timer);
                            if (timer instanceof WebTimer) {
                                wt.idTimers.remove(((WebTimer) timer).getId());
                            }
                        }
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
                    wt.idTimers.put(webTimer.getId(), webTimer);
                }
            }
        }
    }

    private void stopTimers() {
        Set<Timer> timers = new HashSet<Timer>(timerWindow.keySet());
        for (final Timer timer : timers) {
            if (timer != null && !timer.isStopped()) {
                timer.stopTimer();
            }
        }
    }

    /**
     * Returns a timer by id
     * @param id timer id
     * @return timer or <code>null</code>
     */
    public Timer getTimer(String id) {
        Window currentWindow = getCurrentWindow();
        WindowTimers wt = windowTimers.get(currentWindow);
        if (wt != null) {
            return wt.idTimers.get(id);
        } else {
            return null;
        }
    }

    /**
     * Do not use this method in application code
     * @param currentWindow current window
     * @return collection of timers that applied for the current window
     */
    public Collection<Timer> getAppTimers(Window currentWindow) {
        WindowTimers wt = windowTimers.get(currentWindow);
        if (wt != null) {
            return Collections.unmodifiableSet(wt.timers);
        } else {
            return Collections.emptySet();
        }
    }

    protected static class WindowTimers implements Serializable {
        private static final long serialVersionUID = 2038659815683284376L;
        
        protected Map<String, Timer> idTimers = new HashMap<String, Timer>();
        protected Set<Timer> timers = new HashSet<Timer>();
    }

    private Window getCurrentWindow() {
        String name = currentWindowName.get();
        return (name == null ? getMainWindow() : getWindow(name));
    }

    public AppCookies getCookies() {
        return cookies;
    }

    public String getCookieValue(String name) {
        return cookies.getCookieValue(name);
    }

    public int getCookieMaxAge(String name) {
        return cookies.getCookieMaxAge(name);
    }

    public void addCookie(String name, String value, int maxAge) {
        cookies.addCookie(name, value, maxAge);
    }

    public void addCookie(String name, String value) {
        cookies.addCookie(name, value);
    }

    public void removeCookie(String name) {
        cookies.removeCookie(name);
    }

    public boolean isCookiesEnabled() {
        return cookies.isCookiesEnabled();
    }

    public void setCookiesEnabled(boolean cookiesEnabled) {
        cookies.setCookiesEnabled(cookiesEnabled);
    }
}
