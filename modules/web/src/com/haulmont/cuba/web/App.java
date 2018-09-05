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
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.executors.IllegalConcurrentAccessException;
import com.haulmont.cuba.gui.settings.SettingsClient;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.haulmont.cuba.web.exception.ExceptionHandlers;
import com.haulmont.cuba.web.log.AppLog;
import com.haulmont.cuba.web.security.events.SessionHeartbeatEvent;
import com.haulmont.cuba.web.settings.WebSettingsClient;
import com.haulmont.cuba.web.sys.AppCookies;
import com.haulmont.cuba.web.sys.BackgroundTaskManager;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Central class of the web application. An instance of this class is created for each client's session and is bound
 * to {@link VaadinSession}.
 * <br>
 * Use {@link #getInstance()} static method to obtain the reference to the current App instance.
 */
public abstract class App {

    public static final String NAME = "cuba_App";

    public static final String USER_SESSION_ATTR = "userSessionId";

    public static final String APP_THEME_COOKIE_PREFIX = "APP_THEME_NAME_";

    public static final String COOKIE_LOCALE = "LAST_LOCALE";

    private static final Logger log = LoggerFactory.getLogger(App.class);

    static {
        AbstractClientConnector.setIncorrectConcurrentAccessHandler(() -> {
            throw new IllegalConcurrentAccessException();
        });
    }

    protected AppLog appLog;

    protected Connection connection;

    protected ExceptionHandlers exceptionHandlers;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected ThemeConstantsRepository themeConstantsRepository;

    @Inject
    protected UserSessionService userSessionService;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected SettingsClient settingsClient;

    @Inject
    protected Events events;

    protected AppCookies cookies;

    protected LinkHandler linkHandler;

    protected BackgroundTaskManager backgroundTaskManager = new BackgroundTaskManager();

    protected ThemeConstants themeConstants;

    public App() {
        log.trace("Creating application {}", this);
    }

    protected ThemeConstants loadTheme() {
        String appWindowTheme = webConfig.getAppWindowTheme();
        String userAppTheme = cookies.getCookieValue(APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName());
        if (userAppTheme != null) {
            if (!Objects.equals(userAppTheme, appWindowTheme)) {
                // check theme support
                Set<String> supportedThemes = themeConstantsRepository.getAvailableThemes();
                if (supportedThemes.contains(userAppTheme)) {
                    appWindowTheme = userAppTheme;
                }
            }
        }

        ThemeConstants theme = themeConstantsRepository.getConstants(appWindowTheme);
        if (theme == null) {
            throw new IllegalStateException("Unable to use theme constants '" + appWindowTheme + "'");
        }

        return theme;
    }

    protected void applyTheme(String appWindowTheme) {
        ThemeConstants theme = themeConstantsRepository.getConstants(appWindowTheme);

        if (theme == null) {
            log.warn("Unable to use theme constants '{}'", appWindowTheme);
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
     * @return currently displayed top-level window
     */
    public Window.TopLevelWindow getTopLevelWindow() {
        return getAppUI().getTopLevelWindow();
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
                log.warn("Invalid UI in the session: {}", ui);
        }
        return list;
    }

    public abstract void loginOnStart() throws LoginException;

    protected Connection createConnection() {
        return AppBeans.getPrototype(Connection.NAME);
    }

    /**
     * Called when <em>the first</em> UI of the session is initialized.
     */
    protected void init(Locale requestLocale) {
        VaadinSession vSession = VaadinSession.getCurrent();
        vSession.setAttribute(App.class, this);

        vSession.setLocale(messageTools.getDefaultLocale());

        // set root error handler for all session
        vSession.setErrorHandler(event -> {
            try {
                getExceptionHandlers().handle(event);
                getAppLog().log(event);
            } catch (Throwable e) {
                //noinspection ThrowableResultOfMethodCallIgnored
                log.error("Error handling exception\nOriginal exception:\n{}\nException in handlers:\n{}",
                        ExceptionUtils.getStackTrace(event.getThrowable()), ExceptionUtils.getStackTrace(e)
                );
            }
        });

        appLog = new AppLog();

        connection = createConnection();
        exceptionHandlers = new ExceptionHandlers(this);
        cookies = new AppCookies();

        themeConstants = loadTheme();

        log.debug("Initializing application");

        // get default locale from config
        Locale targetLocale = resolveLocale(requestLocale);
        setLocale(targetLocale);
    }

    protected Locale resolveLocale(@Nullable Locale requestLocale) {
        Map<String, Locale> locales = globalConfig.getAvailableLocales();

        if (globalConfig.getLocaleSelectVisible()) {
            String lastLocale = getCookieValue(COOKIE_LOCALE);
            if (lastLocale != null) {
                for (Locale locale : locales.values()) {
                    if (locale.toLanguageTag().equals(lastLocale)) {
                        return locale;
                    }
                }
            }
        }

        if (requestLocale != null) {
            Locale requestTrimmedLocale = messageTools.trimLocale(requestLocale);
            if (locales.containsValue(requestTrimmedLocale)) {
                return requestTrimmedLocale;
            }

            // if not found and application locale contains country, try to match by language only
            if (!StringUtils.isEmpty(requestLocale.getCountry())) {
                Locale appLocale = Locale.forLanguageTag(requestLocale.getLanguage());
                for (Locale locale : locales.values()) {
                    if (Locale.forLanguageTag(locale.getLanguage()).equals(appLocale)) {
                        return locale;
                    }
                }
            }
        }

        // return default locale
        return messageTools.getDefaultLocale();
    }

    /**
     * Called on each browser tab initialization.
     */
    public void createTopLevelWindow(AppUI ui) {
        WebWindowManager wm = AppBeans.getPrototype(WebWindowManager.NAME);
        wm.setUi(ui);

        String topLevelWindowId = routeTopLevelWindowId();
        wm.createTopLevelWindow(windowConfig.getWindowInfo(topLevelWindowId));
    }

    protected abstract String routeTopLevelWindowId();

    public void createTopLevelWindow() {
        createTopLevelWindow(AppUI.getCurrent());
    }

    /**
     * Initialize new TopLevelWindow and replace current
     *
     * @param topLevelWindowId target top level window id
     */
    public void navigateTo(String topLevelWindowId) {
        WebWindowManager wm = AppBeans.getPrototype(WebWindowManager.NAME);
        wm.setUi(AppUI.getCurrent());

        wm.createTopLevelWindow(windowConfig.getWindowInfo(topLevelWindowId));
    }

    /**
     * Called from heartbeat request. <br>
     * Used for ping middleware session and show session messages
     */
    public void onHeartbeat() {
        Connection connection = getConnection();

        boolean sessionIsAlive = false;
        if (connection.isAuthenticated()) {
            // Ping middleware session if connected and show messages
            log.debug("Ping middleware session");

            try {
                String message = userSessionService.getMessages();

                sessionIsAlive = true;

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

        if (sessionIsAlive) {
            events.publish(new SessionHeartbeatEvent(this));
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
        if (!vSession.hasLock()) {
            throw new IllegalStateException("VaadinSession is not owned by the current thread");
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
        return vSession != null
                && vSession.hasLock()
                && vSession.getAttribute(App.class) != null;
    }

    /**
     * @return Current connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return WindowManager instance or null if the current UI has no MainWindow
     */
    public WebWindowManager getWindowManager() {
        if (getAppUI() == null) {
            return null;
        }

        Window.TopLevelWindow topLevelWindow = getTopLevelWindow();

        return topLevelWindow != null ? (WebWindowManager) topLevelWindow.getWindowManager() : null;
    }

    public AppLog getAppLog() {
        return appLog;
    }

    public ExceptionHandlers getExceptionHandlers() {
        return exceptionHandlers;
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

    public Locale getLocale() {
        return VaadinSession.getCurrent().getLocale();
    }

    public void setLocale(Locale locale) {
        UserSession session = getConnection().getSession();
        if (session != null) {
            session.setLocale(locale);
        }

        AppUI currentUi = AppUI.getCurrent();
        // it can be null if we handle request in a custom RequestHandler
        if (currentUi != null) {
            currentUi.setLocale(locale);
            currentUi.updateClientSystemMessages(locale);
        }

        VaadinSession.getCurrent().setLocale(locale);

        for (AppUI ui : getAppUIs()) {
            if (ui != currentUi) {
                ui.accessSynchronously(() -> {
                    ui.setLocale(locale);
                    ui.updateClientSystemMessages(locale);
                });
            }
        }
    }

    public void setUserAppTheme(String themeName) {
        addCookie(APP_THEME_COOKIE_PREFIX + globalConfig.getWebContextName(), themeName);
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
            for (AppUI ui : getAppUIs()) {
                ui.accessSynchronously(() -> {
                    Window.TopLevelWindow topLevelWindow = getTopLevelWindow();
                    if (topLevelWindow != null) {
                        WebWindowManager webWindowManager = (WebWindowManager) topLevelWindow.getWindowManager();
                        webWindowManager.disableSavingScreenHistory = true;
                        webWindowManager.closeAll();
                    }

                    // also remove all native Vaadin windows, that is not under CUBA control
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
        ((WebSettingsClient) settingsClient).clearCache();
    }

    /**
     * Try to perform logout. If there are unsaved changes in opened windows then logout will not be performed and
     * unsaved changes dialog will appear.
     */
    public void logout() {
        logout(null);
    }

    /**
     * Try to perform logout. If there are unsaved changes in opened windows then logout will not be performed and
     * unsaved changes dialog will appear.
     *
     * @param runWhenLoggedOut runnable that will be invoked if user decides to logout
     */
    public void logout(@Nullable Runnable runWhenLoggedOut) {
        try {
            Window.TopLevelWindow topLevelWindow = getTopLevelWindow();
            if (topLevelWindow != null) {
                topLevelWindow.saveSettings();

                WebWindowManager wm = (WebWindowManager) topLevelWindow.getWindowManager();
                wm.checkModificationsAndCloseAll(() -> {
                    Connection connection = getConnection();
                    connection.logout();

                    if (runWhenLoggedOut != null) {
                        runWhenLoggedOut.run();
                    }
                });
            } else {
                Connection connection = getConnection();
                connection.logout();

                if (runWhenLoggedOut != null) {
                    runWhenLoggedOut.run();
                }
            }
        } catch (Exception e) {
            log.error("Error on logout", e);
            String url = ControllerUtils.getLocationWithoutParams() + "?restartApp";
            AppUI.getCurrent().getPage().open(url, "_self");
        }
    }
}