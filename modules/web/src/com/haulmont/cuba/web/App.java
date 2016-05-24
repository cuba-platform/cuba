/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.settings.SettingsClient;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.web.auth.RequestContext;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.exception.ExceptionHandlers;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.settings.WebSettingsClient;
import com.haulmont.cuba.web.sys.AppCookies;
import com.haulmont.cuba.web.sys.BackgroundTaskManager;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Central class of the web application. An instance of this class is created for each client's session and is bound
 * to {@link VaadinSession}.
 * <p/>
 * Use {@link #getInstance()} static method to obtain the reference to the current App instance.
 */
public abstract class App {

    public static final String USER_SESSION_ATTR = "userSessionId";

    public static final String APP_THEME_COOKIE_PREFIX = "APP_THEME_NAME_";

    private static Logger log = LoggerFactory.getLogger(App.class);

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

    protected String webResourceTimestamp = "DEBUG";

    protected String clientAddress;

    protected ThemeConstants themeConstants;

    public App() {
        log.trace("Creating application " + this);
        try {
            Configuration configuration = AppBeans.get(Configuration.NAME);

            webConfig = configuration.getConfig(WebConfig.class);
            webAuthConfig = configuration.getConfig(WebAuthConfig.class);
            globalConfig = configuration.getConfig(GlobalConfig.class);

            appLog = new AppLog();

            connection = createConnection();
            exceptionHandlers = new ExceptionHandlers(this);
            cookies = new AppCookies();
            backgroundTaskManager = new BackgroundTaskManager();

            themeConstants = loadTheme();

            VaadinServlet vaadinServlet = VaadinServlet.getCurrent();
            ServletContext sc = vaadinServlet.getServletContext();
            String resourcesTimestamp = sc.getInitParameter("webResourcesTs");
            if (StringUtils.isNotEmpty(resourcesTimestamp)) {
                this.webResourceTimestamp = resourcesTimestamp;
            }
        } catch (Exception e) {
            log.error("Error initializing application", e);

            throw new Error("Error initializing application. See log for details.");
        }
    }

    protected ThemeConstants loadTheme() {
        ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
        String appWindowTheme = webConfig.getAppWindowTheme();
        String userAppTheme = cookies.getCookieValue(APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName());
        if (userAppTheme != null) {
            if (!StringUtils.equals(userAppTheme, appWindowTheme)) {
                // check theme support
                Set<String> supportedThemes = themeRepository.getAvailableThemes();
                if (supportedThemes.contains(userAppTheme)) {
                    appWindowTheme = userAppTheme;
                }
            }
        }

        ThemeConstants theme = themeRepository.getConstants(appWindowTheme);
        if (theme == null) {
            throw new IllegalStateException("Unable to use theme constants '" + appWindowTheme + "'");
        }

        return theme;
    }

    protected void applyTheme(String appWindowTheme) {
        ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
        ThemeConstants theme = themeRepository.getConstants(appWindowTheme);

        if (theme == null) {
            log.warn("Unable to use theme constants '" + appWindowTheme + "'");
        } else {
            this.themeConstants = theme;
            setUserAppTheme(appWindowTheme);
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

    public ThemeConstants getThemeConstants() {
        return themeConstants;
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
        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        locale = messageTools.getDefaultLocale();

        if (webAuthConfig.getExternalAuthentication()) {
            principal = RequestContext.get().getRequest().getUserPrincipal();
        }
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

            try {
                UserSessionService service = AppBeans.get(UserSessionService.NAME);
                String message = service.getMessages();
                if (message != null) {
                    message = message.replace("\n", "<br/>");
                    getWindowManager().showNotification(message, Frame.NotificationType.ERROR_HTML);
                }
            } catch (NoUserSessionException ignored) {
                // ignore no user session exception
            } catch (Exception e) {
                log.warn("Exception while session ping", e);
            }
        }
    }

    /**
     * @return Current App instance. Can be invoked anywhere in application code.
     * @throws IllegalStateException if no application instance is bound to the current {@link VaadinSession}
     */
    public static App getInstance() {
        VaadinSession vSession = VaadinSession.getCurrent();
        if (vSession == null) {
            throw new IllegalStateException("No VaadinSession found");
        }
        App app = vSession.getAttribute(App.class);
        if (app == null) {
            throw new IllegalStateException("No App is bound to the current VaadinSession");
        }
        return app;
    }

    /**
     * @return true if an {@link App} instance is currently bound and can be safely obtained by {@link #getInstance()}
     */
    public static boolean isBound() {
        VaadinSession vSession = VaadinSession.getCurrent();
        return vSession != null && vSession.getAttribute(App.class) != null;
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

    public void setUserAppTheme(String themeName) {
        addCookie(APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName(), themeName);
    }

    public String getWebResourceTimestamp() {
        return webResourceTimestamp;
    }

    public BackgroundTaskManager getTaskManager() {
        return backgroundTaskManager;
    }

    public void addBackgroundTask(Future task) {
        backgroundTaskManager.addTask(task);
    }

    public void removeBackgroundTask(Future task) {
        backgroundTaskManager.removeTask(task);
    }

    public void cleanupBackgroundTasks() {
        backgroundTaskManager.cleanupTasks();
    }

    public void closeAllWindows() {
        log.debug("Closing all windows");
        try {
            for (final AppUI ui : getAppUIs()) {
                ui.accessSynchronously(() -> {
                    AppWindow appWindow = ui.getAppWindow();
                    if (appWindow != null) {
                        WebWindowManager webWindowManager = appWindow.getWindowManager();
                        webWindowManager.disableSavingScreenHistory = true;
                        webWindowManager.closeAll();
                    }

                    for (com.vaadin.ui.Window win : new ArrayList<>(ui.getWindows())) {
                        ui.removeWindow(win);
                    }
                });
            }
        } catch (Throwable e) {
            log.error("Error closing all windows", e);
        }
    }

    protected void clearSettingsCache() {
        WebSettingsClient webSettingsClient = AppBeans.get(SettingsClient.NAME);
        webSettingsClient.clearCache();
    }
}