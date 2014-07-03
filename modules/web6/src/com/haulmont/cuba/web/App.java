/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.auth.RequestContext;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.exception.ExceptionHandlers;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.*;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class of the web application. Each client connection has its own App.
 * Use {@link #getInstance()} static method to obtain the reference to the current App instance
 * throughout the application code.
 * <p/>
 * Specific application should inherit from this class and set derived class name
 * in <code>application</code> servlet parameter of <code>web.xml</code>
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class App extends Application
        implements ApplicationContext.TransactionListener, HttpServletRequestListener {

    private static final long serialVersionUID = -3435976475534930050L;

    public static final Pattern WIN_PATTERN = Pattern.compile("win([0-9]{1,4})");

    private static Log log = LogFactory.getLog(App.class);

    public static final String THEME_NAME = "havana";

    public static final String LAST_REQUEST_PARAMS_ATTR = "lastRequestParams";

    public static final String LAST_REQUEST_ACTION_ATTR = "lastRequestAction";

    public static final String FROM_HTML_REDIRECT_PARAM = "fromCubaHtmlRedirect";

    public static final String USER_SESSION_ATTR = "userSessionId";

    public static final String APP_THEME_COOKIE_PREFIX = "APP_THEME_NAME_";

    protected Connection connection;

    protected AppLog appLog;

    protected ExceptionHandlers exceptionHandlers;

    private static ThreadLocal<App> currentApp = new ThreadLocal<>();

    protected transient ThreadLocal<String> currentWindowName = new ThreadLocal<>();

    protected LinkHandler linkHandler;

    protected AppTimers timers;

    protected transient Map<Object, Long> requestStartTimes = new WeakHashMap<>();

    protected volatile String contextName;

    protected transient HttpServletResponse response;

    protected transient HttpSession httpSession;

    protected AppCookies cookies;

    protected BackgroundTaskManager backgroundTaskManager;

    protected boolean testModeRequest = false;

    protected boolean themeInitialized = false;

    protected String clientAddress;

    protected final GlobalConfig globalConfig;

    protected final WebConfig webConfig;

    protected final WebAuthConfig webAuthConfig;

    protected String webResourceTimestamp = "DEBUG";

    protected boolean testMode = false;

    protected App() {
        log.trace("Creating application " + this);
        try {
            Configuration configuration = AppBeans.get(Configuration.class);
            webConfig = configuration.getConfig(WebConfig.class);
            globalConfig = configuration.getConfig(GlobalConfig.class);
            webAuthConfig = configuration.getConfig(WebAuthConfig.class);

            testMode = globalConfig.getTestMode();

            appLog = new AppLog();
            connection = createConnection();
            exceptionHandlers = new ExceptionHandlers(this);
            cookies = new AppCookies() {
                @Override
                protected void addCookie(Cookie cookie) {
                    response.addCookie(cookie);
                }
            };
            cookies.setCookiesEnabled(true);
            timers = new AppTimers(this);
            backgroundTaskManager = new BackgroundTaskManager();

            RequestContext rc = RequestContext.get();
            ServletContext servletContext = rc.getRequest().getServletContext();
            String resourcesTimestampPath = servletContext.getInitParameter("webResourcesTs");
            if (StringUtils.isNotEmpty(resourcesTimestampPath)) {
                String timestamp = AppBeans.get(Resources.class).getResourceAsString(resourcesTimestampPath);
                if (StringUtils.isNotEmpty(timestamp))
                    this.webResourceTimestamp = timestamp;
            }
        } catch (Exception e) {
            log.fatal("Error initializing application", e);
            throw new Error("Error initializing application. See log for details.");
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        currentWindowName = new ThreadLocal<>();
        requestStartTimes = new WeakHashMap<>();
    }

    @Override
    public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        cookies.updateCookies(request);

        if (!themeInitialized) {
            String userAppTheme = cookies.getCookieValue(APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName());
            if (userAppTheme != null) {
                if (!StringUtils.equals(userAppTheme, getTheme())) {
                    // check theme support
                    List<String> supportedThemes = webConfig.getAvailableAppThemes();
                    if (supportedThemes.contains(userAppTheme)) {
                        setTheme(userAppTheme);
                    }
                }
            }
            themeInitialized = true;
        }

        if (testMode) {
            String paramName = webConfig.getTestModeParamName();
            testModeRequest = (paramName == null || request.getParameter(paramName) != null);
        }
    }

    @Override
    public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
        testModeRequest = false;
    }

    /**
     * Used from CubaApplicationServlet by reflection
     */
    @SuppressWarnings("unused")
    public static Application.SystemMessages getSystemMessages() {
        Locale defaultLocale;
        if (!AppContext.isStarted()) {
            defaultLocale = Locale.getDefault();
        } else {
            defaultLocale = AppBeans.get(MessageTools.class).getDefaultLocale();
        }
        return compileSystemMessages(defaultLocale);
    }

    public static CubaSystemMessages compileSystemMessages(Locale locale) {
        CubaSystemMessages msgs = new CubaSystemMessages();

        String webContext = AppContext.getProperty("cuba.webContextName");

        if (AppContext.isStarted()) {
            try {
                Messages messages = AppBeans.get(Messages.class);
                String messagePack = messages.getMainMessagePack();

                msgs.setSessionExpiredCaption(messages.getMessage(messagePack, "sessionExpiredCaption", locale));
                msgs.setSessionExpiredMessage(messages.getMessage(messagePack, "sessionExpiredMessage", locale));

                msgs.setCommunicationErrorCaption(messages.getMessage(messagePack, "communicationErrorCaption", locale));
                msgs.setCommunicationErrorMessage(messages.getMessage(messagePack, "communicationErrorMessage", locale));

                msgs.setInternalErrorCaption(messages.getMessage(messagePack, "internalErrorCaption", locale));
                msgs.setInternalErrorMessage(messages.getMessage(messagePack, "internalErrorMessage", locale));

                msgs.setUiBlockingMessage(messages.getMessage(messagePack, "uiBlockingMessage", locale));
            } catch (Exception e) {
                log.error("Unable to set system messages", e);
                throw new RuntimeException("Unable to set system messages. " +
                        "It usually happens when the middleware web application is not responding due to " +
                        "errors on start. See logs for details.", e);
            }
        }

        msgs.setInternalErrorURL("/" + webContext + "?restartApp");
        msgs.setOutOfSyncNotificationEnabled(false);
        return msgs;
    }

    public static class CubaSystemMessages extends Application.CustomizedSystemMessages {

        private String uiBlockingMessage = "";

        public String getUiBlockingMessage() {
            return uiBlockingMessage;
        }

        public void setUiBlockingMessage(String uiBlockingMessage) {
            this.uiBlockingMessage = uiBlockingMessage;
        }
    }

    protected abstract boolean loginOnStart(HttpServletRequest request);

    protected abstract Connection createConnection();

    /**
     * @return Current App instance. Can be invoked anywhere in application code.
     */
    public static App getInstance() {
        App app = currentApp.get();
        if (app == null) {
            throw new IllegalStateException("No App bound to the current thread. This may be the result of hot-deployment.");
        }
        return app;
    }

    public static boolean isBound() {
        return currentApp.get() != null;
    }

    public static String generateWebWindowName() {
        Double d = Math.random() * 10000;
        return "win" + d.intValue();
    }

    /**
     * Initializes exception handlers immediately after login and logout.
     * Can be overridden in descendants to manipulate exception handlers programmatically.
     *
     * @param isConnected true after login, false after logout
     */
    protected void initExceptionHandlers(boolean isConnected) {
        if (isConnected) {
            exceptionHandlers.createByConfiguration();
        } else {
            exceptionHandlers.removeAll();
        }
    }

    /**
     * Should be overridden in descendant to create an application-specific main window
     */
    protected AppWindow createAppWindow() {
        AppWindow appWindow = new AppWindow(this);

        Timer timer = createSessionPingTimer(true);
        if (timer != null) {
            timers.add(timer, appWindow);
            timer.start();
        }

        return appWindow;
    }

    public AppWindow getAppWindow() {
        String name = currentWindowName.get();
        //noinspection deprecation
        Window window = name == null ? getMainWindow() : getWindow(name);
        if (window instanceof AppWindow) {
            return (AppWindow) window;
        } else {
            return null;
        }
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
     * @return Current connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return WindowManager instance or null if the current UI has no AppWindow
     */
    public WebWindowManager getWindowManager() {
        AppWindow appWindow = getAppWindow();
        return appWindow != null ? appWindow.getWindowManager() : null;
    }

    public AppLog getAppLog() {
        return appLog;
    }

    protected String createWindowName(boolean main) {
        String name = main ? AppContext.getProperty("cuba.web.mainWindowName") : AppContext.getProperty("cuba.web.loginWindowName");
        if (StringUtils.isBlank(name)) {
            name = generateWebWindowName();
        }
        return name;
    }

    @Override
    public void terminalError(Terminal.ErrorEvent event) {
        if (testMode) {
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
            getAppLog().log(event);
            exceptionHandlers.handle(event);
        }
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        try {
            HttpServletRequest request = (HttpServletRequest) transactionData;

            this.httpSession = request.getSession();

            httpSession.setMaxInactiveInterval(webConfig.getHttpSessionExpirationTimeoutSec());

            setClientAddress(request);

            if (log.isTraceEnabled()) {
                log.trace("requestStart: [@" + Integer.toHexString(System.identityHashCode(request)) + "] " +
                        request.getRequestURI() +
                        (request.getUserPrincipal() != null ? " [" + request.getUserPrincipal() + "]" : "") +
                        " from " + clientAddress);
            }

            if (application == App.this) {
                currentApp.set((App) application);
            }
            application.setLocale(request.getLocale());

            if (ActiveDirectoryHelper.useActiveDirectory()) {
                setUser(request.getUserPrincipal());
            }

            if (contextName == null) {
                contextName = request.getContextPath().substring(1);
            }

            String requestURI = request.getRequestURI();
            String windowName = request.getParameter("windowName");

            setupCurrentWindowName(requestURI, windowName);

            String action = (String) httpSession.getAttribute(LAST_REQUEST_ACTION_ATTR);


            if (!connection.isConnected() &&
                    !((webConfig.getLoginAction().equals(action)) || auxillaryUrl(requestURI))) {
                if (loginOnStart(request)) {
                    setupCurrentWindowName(requestURI, windowName);
                }
            }

            if (connection.isConnected()) {
                UserSession userSession = connection.getSession();
                if (userSession != null) {
                    AppContext.setSecurityContext(new SecurityContext(userSession));
                    application.setLocale(userSession.getLocale());
                }
                requestStartTimes.put(transactionData, System.currentTimeMillis());
            }

            processExternalLink(request, requestURI);
        } catch (final Exception ex) {
            getErrorHandler().terminalError(new Terminal.ErrorEvent() {
                public Throwable getThrowable() {
                    return ex;
                }
            });
        }
    }

    protected void setClientAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X_FORWARDED_FOR");
        if (!StringUtils.isBlank(xForwardedFor)) {
            String[] strings = xForwardedFor.split(",");
            clientAddress = strings[strings.length - 1].trim();
        } else {
            clientAddress = request.getRemoteAddr();
        }
    }

    public static boolean auxillaryUrl(String uri) {
        return uri.contains("/UIDL/") || uri.contains("/APP/") || uri.contains("/VAADIN/");
    }

    private void setupCurrentWindowName(String requestURI, String windowName) {
        //noinspection deprecation
        if (StringUtils.isEmpty(windowName)) {
            currentWindowName.set(getMainWindow() == null ? null : getMainWindow().getName());
        } else {
            currentWindowName.set(windowName);
        }

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

    protected void processExternalLink(HttpServletRequest request, String requestURI) {
        String action = (String) request.getSession().getAttribute(LAST_REQUEST_ACTION_ATTR);

        if (!auxillaryUrl(requestURI) && webConfig.getLinkHandlerActions().contains(action)) {
            Map<String, String> params = (Map<String, String>) request.getSession().getAttribute(LAST_REQUEST_PARAMS_ATTR);
            if (params == null) {
                log.warn("Unable to process the external link: lastRequestParams not found in session");
                return;
            }
            LinkHandler linkHandler = AppBeans.getPrototype(LinkHandler.NAME, this, action, params);
            if (connection.isConnected()) {
                linkHandler.handle();
            } else {
                this.linkHandler = linkHandler;
            }
        }
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        HttpServletRequest request = (HttpServletRequest) transactionData;
        if (connection.isConnected()) {
            UserSession userSession = connection.getSession();
            if (userSession != null) {
                request.getSession().setAttribute(USER_SESSION_ATTR, userSession);
            } else {
                request.getSession().setAttribute(USER_SESSION_ATTR, null);
            }
        } else {
            request.getSession().setAttribute(USER_SESSION_ATTR, null);
        }

        Long start = requestStartTimes.remove(transactionData);
        if (start != null) {
            long t = System.currentTimeMillis() - start;
            if (t > (webConfig.getLogLongRequestsThresholdSec() * 1000)) {
                log.warn(String.format("Too long request processing [%d ms]: ip=%s, url=%s",
                        t, ((HttpServletRequest) transactionData).getRemoteAddr(), ((HttpServletRequest) transactionData).getRequestURI()));
            }
        }

        if (application == App.this) {
            currentApp.set(null);
            currentApp.remove();
        }

        AppContext.setSecurityContext(null);

        HttpSession httpSession = ((HttpServletRequest) transactionData).getSession();
        httpSession.setAttribute(LAST_REQUEST_ACTION_ATTR, null);
        httpSession.setAttribute(LAST_REQUEST_PARAMS_ATTR, null);

        if (log.isTraceEnabled()) {
            log.trace("requestEnd: [@" + Integer.toHexString(System.identityHashCode(transactionData)) + "]");
        }
    }

    public BackgroundTaskManager getTaskManager() {
        return backgroundTaskManager;
    }

    public void addBackgroundTask(Thread task) {
        backgroundTaskManager.addTask(task);
    }

    public void removeBackgroundTask(Thread task) {
        backgroundTaskManager.removeTask(task);
    }

    public void cleanupBackgroundTasks() {
        backgroundTaskManager.cleanupTasks();
        for (Window w : getWindows()) {
            if (w instanceof AppWindow) {
                ((AppWindow) w).getWorkerTimer().removeAllListeners();
                ((AppWindow) w).getWorkerTimer().stop();
            }
        }
    }

    /**
     * For internal use only
     */
    public Window getCurrentWindow() {
        String name = currentWindowName.get();
        return (name == null ? getMainWindow() : getWindow(name));
    }

    public String getCurrentWindowName() {
        return currentWindowName.get();
    }

    public AppTimers getTimers() {
        return timers;
    }

    /**
     * Adds a timer on the application level
     *
     * @param timer new timer
     */
    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    /**
     * Adds a timer for the defined window
     *
     * @param timer new timer
     * @param owner component that owns a timer
     */
    public void addTimer(final Timer timer, com.haulmont.cuba.gui.components.Window owner) {
        timers.add(timer, owner);
    }

    public void reinitializeAppearanceProperties() {
        themeInitialized = false;
    }

    public void setUserAppTheme(String themeName) {
        addCookie(APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName(), themeName);
        super.setTheme(themeName);
    }

    protected Timer createSessionPingTimer(final boolean connected) {
        int sessionExpirationTimeout = webConfig.getHttpSessionExpirationTimeoutSec();
        int sessionPingPeriod = sessionExpirationTimeout / 3;
        if (sessionPingPeriod > 0) {
            Timer timer = new Timer(sessionPingPeriod * 1000, true);
            timer.addListener(new Timer.Listener() {
                @Override
                public void onTimer(Timer timer) {
                    if (connected) {
                        log.debug("Ping session");
                        UserSessionService service = AppBeans.get(UserSessionService.NAME);
                        String message = service.getMessages();
                        if (message != null) {
                            message = message.replace("\n", "<br/>");
                            getAppWindow().showNotification(message, Window.Notification.TYPE_ERROR_MESSAGE);
                        }
                    }
                }

                @Override
                public void onStopTimer(Timer timer) {
                }
            });
            return timer;
        }
        return null;
    }

    public AppCookies getCookies() {
        return cookies;
    }

    public HttpSession getHttpSession() {
        return httpSession;
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

    public boolean isTestModeRequest() {
        return testModeRequest;
    }

    public String getWebResourceTimestamp() {
        return webResourceTimestamp;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void closeAllWindows() {
        log.debug("Closing all windows");
        try {
            Collection<Window> windows = App.getInstance().getWindows();
            for (Window win : new ArrayList<>(windows)) {
                if (win instanceof AppWindow) {
                    WebWindowManager wm = ((AppWindow) win).getWindowManager();
                    wm.disableSavingScreenHistory = true;
                    wm.closeAll();
                }

                WebWindowManager.removeCloseListeners(win);
                removeWindow(win);
            }
        } catch (Throwable e) {
            log.error("Error closing all windows", e);
        }
    }

    public boolean isTestMode() {
        return testMode;
    }
}