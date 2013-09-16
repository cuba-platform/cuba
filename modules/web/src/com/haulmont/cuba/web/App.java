/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.auth.RequestContext;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.exception.ExceptionHandlers;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class App implements Serializable {

    public static final String USER_SESSION_ATTR = "userSessionId";

    private static Log log = LogFactory.getLog(App.class);

    private AppLog appLog;

    private WebWindowManager windowManager;

    protected Connection connection;

    protected ExceptionHandlers exceptionHandlers;

    protected final GlobalConfig globalConfig;

    protected final WebConfig webConfig;

    protected final WebAuthConfig webAuthConfig;

    private AppCookies cookies;

    protected LinkHandler linkHandler;

    protected final BackgroundTaskManager backgroundTaskManager;

    protected Principal principal;

    protected Locale locale = Locale.getDefault();

    protected boolean themeInitialized = false;

    protected boolean testModeRequest = false;

    protected String webResourceTimestamp = "null";

    static {
        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }

    private String clientAddress;

    public App() {
        try {
            Configuration configuration = AppBeans.get(Configuration.class);

            webConfig = configuration.getConfig(WebConfig.class);
            webAuthConfig = configuration.getConfig(WebAuthConfig.class);
            globalConfig = configuration.getConfig(GlobalConfig.class);

            appLog = new AppLog();

            connection = createConnection();
            windowManager = createWindowManager();
            exceptionHandlers = new ExceptionHandlers(this);
            cookies = new AppCookies();
            backgroundTaskManager = new BackgroundTaskManager();

            String resourcesTimestampPath = webConfig.getResourcesTimestampPath();
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

    public AppWindow getAppWindow() {
        return AppUI.getCurrent().getAppWindow();
    }

    public AppUI getAppUI() {
        return AppUI.getCurrent();
    }

    protected abstract boolean loginOnStart();

    protected abstract Connection createConnection();

    protected void init() {
        log.debug("Initializing application");

        // get default locale from config
        locale = globalConfig.getAvailableLocales().entrySet().iterator().next().getValue();

        if (ActiveDirectoryHelper.useActiveDirectory())
            principal = RequestContext.get().getRequest().getUserPrincipal();
    }

    protected void initView() {
    }

    /**
     * Can be overridden in descendant to create an application-specific {@link WebWindowManager}
     */
    protected WebWindowManager createWindowManager() {
        return new WebWindowManager(this);
    }

    /**
     * @return Current App instance. Can be invoked anywhere in application code.
     */
    public static App getInstance() {
        App app = getSessionApplication();
        if (app == null)
            throw new IllegalStateException("No App bound to the current thread. This may be the result of hot-deployment.");
        return app;
    }

    private static App getSessionApplication() {
        return VaadinSession.getCurrent().getAttribute(App.class);
    }

    public static boolean isBound() {
        return getSessionApplication() != null;
    }

    /**
     * @return Current connection object
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

    public ExceptionHandlers getExceptionHandlers() {
        return exceptionHandlers;
    }

    public void showView(UIView view) {
        getAppUI().setContent(view);
        getAppUI().getPage().setTitle(view.getTitle());
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

    public Principal getPrincipal() {
        return principal;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;

        UI.getCurrent().setLocale(locale);
        VaadinSession.getCurrent().setLocale(locale);
    }

    public String getClientAddress() {
        if (clientAddress == null) {
            HttpServletRequest request = RequestContext.get().getRequest();
            String xForwardedFor = request.getHeader("X_FORWARDED_FOR");
            if (!StringUtils.isBlank(xForwardedFor)) {
                String[] strings = xForwardedFor.split(",");
                clientAddress = strings[strings.length - 1].trim();
            } else
                clientAddress = request.getRemoteAddr();
        }

        return clientAddress;
    }

    public void reinitializeAppearanceProperties() {
        themeInitialized = false;
    }

    public boolean isTestModeRequest() {
        return testModeRequest;
    }

    public String getWebResourceTimestamp() {
        return webResourceTimestamp;
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
    }
}