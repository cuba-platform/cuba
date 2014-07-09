/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.auth.RequestContext;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.exception.ExceptionHandlers;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.sys.AppCookies;
import com.haulmont.cuba.web.sys.BackgroundTaskManager;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Central class of the web application. An instance of this class is created for each client's session and is bound
 * to {@link VaadinSession}.
 * <p/>
 * Use {@link #getInstance()} static method to obtain the reference to the current App instance.
 *
 * @author artamonov
 * @version $Id$
 */
public abstract class App {

    public static final String USER_SESSION_ATTR = "userSessionId";

    private static Log log = LogFactory.getLog(App.class);

    protected AppLog appLog;

    protected Connection connection;

    protected ExceptionHandlers exceptionHandlers;

    protected final GlobalConfig globalConfig;

    protected final WebConfig webConfig;

    protected final WebAuthConfig webAuthConfig;

    protected AppCookies cookies;

    protected LinkHandler linkHandler;

    protected final BackgroundTaskManager backgroundTaskManager;

    protected Principal principal;

    protected Locale locale = Locale.getDefault();

    protected boolean themeInitialized = false;

    protected String webResourceTimestamp = "DEBUG";

    protected String clientAddress;

    public App() {
        log.trace("Creating application " + this);
        try {
            Configuration configuration = AppBeans.get(Configuration.class);

            webConfig = configuration.getConfig(WebConfig.class);
            webAuthConfig = configuration.getConfig(WebAuthConfig.class);
            globalConfig = configuration.getConfig(GlobalConfig.class);

            appLog = new AppLog();

            connection = createConnection();
            exceptionHandlers = new ExceptionHandlers(this);
            cookies = new AppCookies();
            backgroundTaskManager = new BackgroundTaskManager();

            VaadinServlet vaadinServlet = VaadinServlet.getCurrent();
            ServletContext sc = vaadinServlet.getServletContext();
            String resourcesTimestamp = sc.getInitParameter("webResourcesTs");
            if (StringUtils.isNotEmpty(resourcesTimestamp)) {
                this.webResourceTimestamp = resourcesTimestamp;
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

    /**
     * @return AppWindow displayed in the current UI. Can be null if not logged in.
     */
    public AppWindow getAppWindow() {
        return AppUI.getCurrent().getAppWindow();
    }

    /**
     * @return current UI
     */
    public AppUI getAppUI() {
        return AppUI.getCurrent();
    }

    public List<AppUI> getAppUIs() {
        List<AppUI> list = new ArrayList<>();
        for (UI ui : VaadinSession.getCurrent().getUIs()) {
            if (ui instanceof AppUI)
                list.add((AppUI) ui);
            else
                log.warn("Invalid UI in the session: " + ui);
        }
        return list;
    }

    protected abstract boolean loginOnStart();

    protected abstract Connection createConnection();

    /**
     * Called when <em>the first</em> UI of the session is initialized.
     */
    protected void init() {
        log.debug("Initializing application");

        // get default locale from config
        locale = AppBeans.get(MessageTools.class).getDefaultLocale();

        if (ActiveDirectoryHelper.useActiveDirectory())
            principal = RequestContext.get().getRequest().getUserPrincipal();
    }

    /**
     * Called on each UI initialization.
     *
     * @param ui initialized UI
     */
    protected void initView(AppUI ui) {
    }

    /**
     * Called from heartbeat request. <br/>
     * Used for ping middleware session and show session messages
     */
    public void onHeartbeat() {
        if (getConnection().isConnected()) {
            // Ping middleware session if connected and show messages
            log.debug("Ping session");

            UserSessionService service = AppBeans.get(UserSessionService.NAME);
            String message = service.getMessages();
            if (message != null) {
                message = message.replace("\n", "<br/>");
                getWindowManager().showNotification(message, IFrame.NotificationType.ERROR_HTML);
            }
        }
    }

    /**
     * @return Current App instance. Can be invoked anywhere in application code.
     * @throws IllegalStateException if no application instance is bound to the current {@link VaadinSession}
     */
    public static App getInstance() {
        App app = VaadinSession.getCurrent().getAttribute(App.class);
        if (app == null)
            throw new IllegalStateException("No App is bound to the current VaadinSession");
        return app;
    }

    /**
     * @return true if an {@link App} instance is currently bound and can be safely obtained by {@link #getInstance()}
     */
    public static boolean isBound() {
        return VaadinSession.getCurrent().getAttribute(App.class) != null;
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
        AppWindow appWindow = getAppUI().getAppWindow();
        return appWindow != null ? appWindow.getWindowManager() : null;
    }

    public AppLog getAppLog() {
        return appLog;
    }

    public ExceptionHandlers getExceptionHandlers() {
        return exceptionHandlers;
    }

    /**
     * Create the login window instance.
     *
     * @param ui current UI
     * @return login window
     */
    protected UIView createLoginWindow(AppUI ui) {
        return new LoginWindow(ui);
    }

    /**
     * Create the main window instance.
     *
     * @param ui current UI
     * @return main window
     */
    protected AppWindow createAppWindow(AppUI ui) {
        return new AppWindow(ui);
    }

    /**
     * @deprecated Unused. In next minor release will be removed
     */
    @Deprecated
    protected CubaTimer createSessionPingTimer() {
        return null;
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

    public void closeAllWindows() {
        log.debug("Closing all windows");
        try {
            for (AppUI ui : getAppUIs()) {
                AppWindow appWindow = ui.getAppWindow();
                if (appWindow != null) {
                    WebWindowManager webWindowManager = appWindow.getWindowManager();
                    webWindowManager.disableSavingScreenHistory = true;
                    webWindowManager.closeAll();
                }

                for (com.vaadin.ui.Window win : new ArrayList<>(ui.getWindows())) {
                    WebWindowManager.removeCloseListeners(win);
                    ui.removeWindow(win);
                }
            }
        } catch (Throwable e) {
            log.error("Error closing all windows", e);
        }
    }
}