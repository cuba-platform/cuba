/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.haulmont.cuba.web.toolkit.ui.*;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Extension;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.peter.contextmenu.ContextMenu;

import java.util.*;

/**
 * Standard main application window.
 * <p/>
 * To use a specific implementation override {@link App#createAppWindow(AppUI)} method.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AppWindow extends UIView implements CubaHistoryControl.HistoryBackHandler {

    private static final Log log = LogFactory.getLog(AppWindow.class);

    protected final AppUI ui;

    protected final App app;

    protected final Connection connection;

    protected final WebWindowManager windowManager;

    protected CubaClientManager clientManager;

    protected CubaFileDownloader fileDownloader;

    protected CubaHistoryControl historyControl;

    protected CubaTimer workerTimer;

    protected WebConfig webConfig;

    protected Window.MainWindow mainWindow;

    protected Messages messages = AppBeans.get(Messages.NAME);

    public AppWindow(AppUI ui) {
        log.trace("Creating " + this);

        this.ui = ui;
        this.app = ui.getApp();
        this.connection = app.getConnection();
        this.windowManager = createWindowManager();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        webConfig = configuration.getConfig(WebConfig.class);

        setSizeFull();

        initInternalComponents();

        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        if (clientConfig.getLayoutAnalyzerEnabled()) {
            final ContextMenu contextMenu = new ContextMenu();
            contextMenu.setOpenAutomatically(true);
            ContextMenu.ContextMenuItem analyzeLayout = contextMenu.addItem(messages.getMainMessage("actions.analyzeLayout"));
            analyzeLayout.addItemClickListener(new ContextMenu.ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenu.ContextMenuItemClickEvent event) {
                    Window window = getMainWindow();
                    if (window != null) {
                        LayoutAnalyzer analyzer = new LayoutAnalyzer();
                        List<LayoutTip> tipsList = analyzer.analyze(window);

                        if (tipsList.isEmpty()) {
                            window.showNotification("No layout problems found", IFrame.NotificationType.HUMANIZED);
                        } else {
                            window.openWindow("layoutAnalyzer", WindowManager.OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                        }
                    }
                }
            });
            contextMenu.setAsContextMenuOf(this);

            addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getButton() == MouseEventDetails.MouseButton.RIGHT) {
                        Component targetComponent = event.getClickedComponent();

                        if (targetComponent != null) {
                            if (getMainWindow().getWorkArea() != null) {
                                WebAppWorkArea workArea = (WebAppWorkArea) getMainWindow().getWorkArea();
                                CubaTabSheet tabbedWindowContainer = workArea.getTabbedWindowContainer();
                                VerticalLayout singleWindowContainer = workArea.getSingleWindowContainer();

                                Component parent = targetComponent.getParent();
                                while (parent != null) {
                                    if (parent == tabbedWindowContainer
                                            || parent == singleWindowContainer) {
                                        return;
                                    }
                                    parent = parent.getParent();
                                }
                            }

                            contextMenu.open(event.getClientX(), event.getClientY());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void show() {
        checkSessions();

        updateClientSystemMessages();

        beforeInitLayout();

        initAppMainWindow();
    }

    protected void initAppMainWindow() {
        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo mainWindowInfo = windowConfig.getWindowInfo("mainWindow");
        windowManager.initMainWindow(mainWindowInfo);
    }

    public AppUI getAppUI() {
        return ui;
    }

    public WebWindowManager getWindowManager() {
        return windowManager;
    }

    /**
     * @return a new instance of {@link WebWindowManager}
     */
    protected WebWindowManager createWindowManager() {
        return new WebWindowManager(app, this);
    }

    /**
     * init system components
     */
    protected void initInternalComponents() {
        clientManager = new CubaClientManager();
        clientManager.extend(this);

        workerTimer = new CubaTimer();
        workerTimer.setTimerId("backgroundWorkerTimer");

        workerTimer.extend(this);

        workerTimer.setRepeating(true);
        workerTimer.setDelay(webConfig.getUiCheckInterval());
        workerTimer.start();

        fileDownloader = new CubaFileDownloader();
        fileDownloader.extend(this);

        if (webConfig.getAllowHandleBrowserHistoryBack()) {
            historyControl = new CubaHistoryControl();
            historyControl.extend(this, this);
        }
    }
    
    protected void updateClientSystemMessages() {
        CubaClientManager.SystemMessages msgs = new CubaClientManager.SystemMessages();
        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        Locale locale = sessionSource.getLocale();

        msgs.communicationErrorCaption = messages.getMainMessage("communicationErrorCaption", locale);
        msgs.communicationErrorMessage = messages.getMainMessage("communicationErrorMessage", locale);

        msgs.sessionExpiredErrorCaption = messages.getMainMessage("sessionExpiredErrorCaption", locale);
        msgs.sessionExpiredErrorMessage = messages.getMainMessage("sessionExpiredErrorMessage", locale);

        msgs.authorizationErrorCaption = messages.getMainMessage("authorizationErrorCaption", locale);
        msgs.authorizationErrorMessage = messages.getMainMessage("authorizationErrorMessage", locale);

        clientManager.updateSystemMessagesLocale(msgs);
    }

    /**
     * Called when the user presses browser "Back" button and {@code cuba.web.allowHandleBrowserHistoryBack}
     * application property is true.
     * <p>Override this method and implement your logic to handle the "Back" button.
     */
    @Override
    public void onHistoryBackPerformed() {
    }

    private void checkSessions() {
        UserSessionService userSessionService = AppBeans.get(UserSessionService.NAME);
        Map<String, Object> info = userSessionService.getLicenseInfo();
        Integer licensed = (Integer) info.get("licensedSessions");
        if (licensed < 0) {
            Notification.show("Invalid CUBA platform license. See server log for details.",
                    Notification.Type.ERROR_MESSAGE);
        } else {
            Integer active = (Integer) info.get("activeSessions");
            if (licensed != 0 && active > licensed) {
                Notification.show("Number of licensed sessions exceeded", "active: " + active + ", licensed: " + licensed,
                        Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public String getTitle() {
        return getAppCaption();
    }

    public Window.MainWindow getMainWindow() {
        return mainWindow;
    }

    protected void setMainWindow(Window.MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * @return Application caption to be shown in browser page title
     */
    protected String getAppCaption() {
        return messages.getMainMessage("application.caption");
    }

    public CubaFileDownloader getFileDownloader() {
        return fileDownloader;
    }

    public CubaTimer getWorkerTimer() {
        return workerTimer;
    }

    public List<CubaTimer> getTimers() {
        List<CubaTimer> timers = new LinkedList<>();
        for (Extension extension : this.getExtensions()) {
            if (extension instanceof CubaTimer) {
                timers.add((CubaTimer) extension);
            }
        }
        return timers;
    }

    public void addTimer(CubaTimer timer) {
        if (!getExtensions().contains(timer)) {
            timer.extend(this);
        }
    }

    public void removeTimer(CubaTimer timer) {
        removeExtension(timer);
    }

    protected void beforeInitLayout() {
        // load theme from user settings
        String themeName = webConfig.getAppWindowTheme();
        UserSettingsTools userSettingsTools = AppBeans.get(UserSettingsTools.NAME);
        themeName = userSettingsTools.loadAppWindowTheme() == null ? themeName : userSettingsTools.loadAppWindowTheme();

        if (!Objects.equals(themeName, ui.getTheme())) {
            // check theme support
            ThemeConstantsRepository themeRepository = AppBeans.get(ThemeConstantsRepository.NAME);
            Set<String> supportedThemes = themeRepository.getAvailableThemes();
            if (supportedThemes.contains(themeName)) {
                app.applyTheme(themeName);
                ui.setTheme(themeName);
            }
        }
    }

    public void refreshUserSubstitutions() {
        UserIndicator userIndicator = getMainWindow().getUserIndicator();
        if (userIndicator != null) {
            userIndicator.refreshUserSubstitutions();
        }
    }
}