/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.actions.ChangeSubstUserAction;
import com.haulmont.cuba.web.actions.DoNotChangeSubstUserAction;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.sys.MenuBuilder;
import com.haulmont.cuba.web.sys.WindowBreadCrumbs;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.*;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Standard main application window.
 * <p/>
 * To use a specific implementation override {@link App#createAppWindow(AppUI)} method.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AppWindow extends UIView implements UserSubstitutionListener, CubaHistoryControl.HistoryBackHandler {

    /**
     * Main window mode. See {@link #TABBED}, {@link #SINGLE}
     */
    public enum Mode {
        /**
         * If the main window is in TABBED mode, it creates the Tabsheet inside
         * and opens screens with {@link com.haulmont.cuba.gui.WindowManager.OpenType#NEW_TAB} as tabs.
         */
        TABBED,

        /**
         * In SINGLE mode each new screen opened with {@link com.haulmont.cuba.gui.WindowManager.OpenType#NEW_TAB}
         * opening type will replace the current screen.
         */
        SINGLE
    }

    private static final long serialVersionUID = 7269808125566032433L;

    private Log log = LogFactory.getLog(AppWindow.class);

    protected CubaClientManager clientManager;

    protected CubaFileDownloader fileDownloader;

    protected CubaHistoryControl historyControl;

    protected CubaTimer workerTimer;

    protected final AppUI ui;

    protected final App app;

    protected WebWindowManager windowManager;

    protected Connection connection;

    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected CubaMenuBar menuBar;
    protected TabSheet tabSheet;
    protected CubaHorizontalSplitPanel foldersSplit;

    protected Mode mode;

    /**
     * Very root layout of the window. Contains all other layouts.
     */
    protected VerticalLayout rootLayout;

    /**
     * Title layout. Created only if {@code cuba.web.useLightHeader} is off.
     */
    protected Layout titleLayout;

    /**
     * Layout containing the menu bar. Next to {@link #titleLayout} by default.
     */
    protected HorizontalLayout menuBarLayout;

    /**
     * Empty layout, below {@link #menuBarLayout} by default.
     */
    protected HorizontalLayout emptyLayout;

    /**
     * Layout that contains FoldersPane (if it is created) and {@link #mainLayout}.
     * Below {@link #emptyLayout} by default.
     */
    protected HorizontalLayout middleLayout;

    protected FoldersPane foldersPane;

    /**
     * Layout containing main tabsheet in {@link Mode#TABBED} or directly application screens in {@link Mode#SINGLE}.
     */
    protected VerticalLayout mainLayout;

    protected Messages messages;

    protected UserSettingsTools userSettingsTools;

    protected AbstractSelect substUserSelect;

    protected ShortcutListener closeShortcut;

    protected ShortcutListener nextTabShortcut;
    protected ShortcutListener previousTabShortcut;

    public AppWindow(AppUI ui) {
        log.trace("Creating " + this);
        this.ui = ui;
        app = ui.getApp();
        connection = app.getConnection();
        windowManager = createWindowManager();

        Configuration configuration = AppBeans.get(Configuration.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);

        messages = AppBeans.get(Messages.class);
        userSettingsTools = AppBeans.get(UserSettingsTools.class);

        mode = userSettingsTools.loadAppWindowMode();

        setSizeFull();
        setBaseStyle("cuba-app-window");

        rootLayout = new VerticalLayout();

        createLayout(rootLayout);
        setContent(rootLayout);

        postInitLayout();
        initStartupScreen();

        initStaticComponents();

        updateClientSystemMessages();

        checkSessions();

        connection.addListener(this);
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

    protected void updateClientSystemMessages() {
        CubaClientManager.SystemMessages msgs = new CubaClientManager.SystemMessages();
        Locale locale = AppBeans.get(UserSessionSource.class).getLocale();

        msgs.communicationErrorCaption = messages.getMainMessage("communicationErrorCaption", locale);
        msgs.communicationErrorMessage = messages.getMainMessage("communicationErrorMessage", locale);

        msgs.sessionExpiredErrorCaption = messages.getMainMessage("sessionExpiredErrorCaption", locale);
        msgs.sessionExpiredErrorMessage = messages.getMainMessage("sessionExpiredErrorMessage", locale);

        msgs.authorizationErrorCaption = messages.getMainMessage("authorizationErrorCaption", locale);
        msgs.authorizationErrorMessage = messages.getMainMessage("authorizationErrorMessage", locale);

        clientManager.updateSystemMessagesLocale(msgs);
    }

    protected void initStaticComponents() {
        clientManager = new CubaClientManager();
        clientManager.extend(rootLayout);

        workerTimer = new CubaTimer();
        workerTimer.setTimerId("backgroundWorkerTimer");

        if (ui.isTestMode()) {
            workerTimer.setCubaId("backgroundWorkerTimer");
            workerTimer.setId(ui.getTestIdManager().reserveId("backgroundWorkerTimer"));
        }
        rootLayout.addComponent(workerTimer);

        workerTimer.setRepeating(true);
        workerTimer.setDelay(webConfig.getUiCheckInterval());
        workerTimer.start();

        fileDownloader = new CubaFileDownloader();
        fileDownloader.extend(rootLayout);

        if (webConfig.getAllowHandleBrowserHistoryBack()) {
            historyControl = new CubaHistoryControl();
            historyControl.extend(rootLayout, this);
        }
    }

    @Override
    public void onHistoryBackPerformed() {
        // Go back to the future
    }

    private void checkSessions() {
        Map<String, Object> info = AppBeans.get(UserSessionService.class).getLicenseInfo();
        Integer licensed = (Integer) info.get("licensedSessions");
        if (licensed < 0) {
            Notification.show("Invalid CUBA platform license", Notification.Type.WARNING_MESSAGE);
        } else {
            Integer active = (Integer) info.get("activeSessions");
            if (licensed != 0 && active > licensed) {
                Notification.show("Number of licensed sessions exceeded", "active: " + active + ", licensed: " + licensed,
                        Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    /**
     * @return current mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Create layouts under {@link #rootLayout}.
     *
     * @param layout {@link #rootLayout}
     */
    protected void createLayout(VerticalLayout layout) {
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setSizeFull();

        if (!webConfig.getUseLightHeader()) {
            titleLayout = createTitleLayout();
            layout.addComponent(titleLayout);
        }

        if (webConfig.getUseLightHeader())
            layout.addStyleName("cuba-app-light-header");

        menuBarLayout = createMenuBarLayout();

        layout.addComponent(menuBarLayout);

        emptyLayout = new HorizontalLayout();
        emptyLayout.setStyleName("cuba-app-empty-layout");
        emptyLayout.setMargin(false);
        emptyLayout.setSpacing(false);
        emptyLayout.setSizeFull();

        layout.addComponent(emptyLayout);

        middleLayout = new HorizontalLayout();

        if (Mode.TABBED.equals(getMode())) {
            middleLayout.addStyleName("cuba-app-work-area");
        } else {
            middleLayout.addStyleName("cuba-app-work-area-single");
        }

        middleLayout.setSizeFull();

        if (webConfig.getFoldersPaneEnabled()) {
            foldersPane = createFoldersPane();

            if (foldersPane != null) {
                foldersSplit = new CubaHorizontalSplitPanel();

                if (webConfig.getUseLightHeader()) {
                    foldersSplit.setDockable(true);
                    foldersSplit.setImmediate(true);
                    foldersPane.setVisible(true);
                    foldersSplit.setDefaultPosition(webConfig.getFoldersPaneDefaultWidth() + "px");
                }

                foldersSplit.setSplitPosition(0, Unit.PIXELS);
                foldersSplit.setMaxSplitPosition(50, Unit.PERCENTAGE);

                if (!webConfig.getUseLightHeader()) {
                    foldersSplit.setLocked(true);
                }

                foldersSplit.addComponent(foldersPane);

                middleLayout.addComponent(foldersSplit);
                middleLayout.setExpandRatio(foldersSplit, 1);

                foldersPane.init(foldersSplit);
            }
        }

        layout.addComponent(middleLayout);
        layout.setExpandRatio(middleLayout, 1);
    }

    /**
     * Creates folders pane.
     * <br>Can be overridden in descendant to create an app-specific folders pane.
     * <br>If this method returns null, no folders functionality is available for application.
     * @return a new FoldersPane instance
     */
    @Nullable
    protected FoldersPane createFoldersPane() {
        return new FoldersPane(menuBar, this);
    }

    @Override
    public String getTitle() {
        return getAppCaption();
    }

    /**
     * @return Application caption to be shown in browser page title
     */
    protected String getAppCaption() {
        return messages.getMessage(getMessagesPack(), "application.caption");
    }

    /**
     * @return main tabsheet in TABBED mode, null in SINGLE mode
     */
    public TabSheet getTabSheet() {
        return tabSheet;
    }

    /**
     * @return main menu bar
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * See {@link #rootLayout}
     * @return Very root layout of the window
     */
    public VerticalLayout getRootLayout() {
        return rootLayout;
    }

    /**
     * See {@link #titleLayout}
     * @return Optional title layout
     */
    @Nullable
    public Layout getTitleLayout() {
        return titleLayout;
    }

    /**
     * See {@link #menuBarLayout}
     * @return Application MenuBar
     */
    public HorizontalLayout getMenuBarLayout() {
        return menuBarLayout;
    }

    /**
     * See {@link #emptyLayout}
     * @return Layout bellow menu bar
     */
    public HorizontalLayout getEmptyLayout() {
        return emptyLayout;
    }

    /**
     * See {@link #mainLayout}
     * @return Main Application layout
     */
    public VerticalLayout getMainLayout() {
        return mainLayout;
    }

    /**
     * @return folders pane or null if it is not created
     */
    @Nullable
    public FoldersPane getFoldersPane() {
        return foldersPane;
    }

    public CubaFileDownloader getFileDownloader() {
        return fileDownloader;
    }

    public CubaTimer getWorkerTimer() {
        return workerTimer;
    }

    public List<CubaTimer> getTimers() {
        List<CubaTimer> timers = new LinkedList<>();
        if (rootLayout != null) {
            for (Component component : rootLayout) {
                if (component instanceof CubaTimer) {
                    timers.add((CubaTimer) component);
                }
            }
        }
        return timers;
    }

    public void addTimer(CubaTimer timer) {
        rootLayout.addComponent(timer);
    }

    public void removeTimer(CubaTimer timer) {
        rootLayout.removeComponent(timer);
    }

    /**
     *  Initialize the startup screen - main window content when no application screens are open.
     */
    protected void initStartupScreen() {
        if (mainLayout != null) {
            if (foldersPane != null) {
                foldersSplit.removeComponent(mainLayout);
            } else {
                middleLayout.removeComponent(mainLayout);
            }
            mainLayout = null;
        }
        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        if (foldersPane != null) {
            foldersSplit.addComponent(mainLayout);
        } else {
            middleLayout.addComponent(mainLayout);
            middleLayout.setExpandRatio(mainLayout, 1);
        }
    }

    /**
     * Called by constructor when all layouts are created but before {@link #initStartupScreen()}.
     */
    protected void postInitLayout() {
//        String themeName = AppContext.getProperty("cuba.web.theme");
//        vaadin7 Theme switch
//        if (themeName == null) themeName = AppUI.THEME_NAME;
//        themeName = userSettingsTools.loadAppWindowTheme() == null ? themeName : userSettingsTools.loadAppWindowTheme();
//        if (!StringUtils.equals(themeName, getTheme())) {
//            setTheme(themeName);
//            // set cookie
//            AppUI.getInstance().setUserAppTheme(themeName);
//        }
    }

    /**
     * Remove all applications screens and show startup screen defined by {@link #initStartupScreen()}.
     */
    public void showStartupScreen() {
        getMainLayout().removeAllComponents();
        tabSheet = null;
        initStartupScreen();
        focus();
    }

    /**
     * Close startup screen defined by {@link #initStartupScreen()} and prepare main window to show application screens.
     */
    public void closeStartupScreen() {
        if (AppWindow.Mode.TABBED.equals(getMode())) {
            if (tabSheet == null) {
                tabSheet = new AppWindow.AppTabSheet();
                if (ui.isTestMode()) {
                    tabSheet.setCubaId("appTabSheet");
                    tabSheet.setId(ui.getTestIdManager().reserveId("appTabSheet"));
                }
                tabSheet.setSizeFull();
                mainLayout.addComponent(tabSheet);
                mainLayout.setExpandRatio(tabSheet, 1);

                createTabShortcuts();
            }
        }

        if (closeShortcut == null)
            closeShortcut = windowManager.createCloseShortcut();
        addAction(closeShortcut);
    }

    protected void createTabShortcuts() {
        if (nextTabShortcut == null) {
            nextTabShortcut = windowManager.createNextWindowTabShortcut();

            ui.addShortcutListener(nextTabShortcut);
        }

        if (previousTabShortcut == null) {
            previousTabShortcut = windowManager.createPreviousWindowTabShortcut();

            ui.addShortcutListener(previousTabShortcut);
        }
    }

    protected boolean hasDialogWindows() {
        return !ui.getWindows().isEmpty();
    }

    /**
     * @return main menu bar layout
     */
    protected HorizontalLayout createMenuBarLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setStyleName("cuba-app-menubar");
        layout.setWidth(100, Unit.PERCENTAGE);
        if (webConfig.getUseLightHeader()){
            layout.addStyleName("cuba-app-light-header");
            layout.setHeight(40, Unit.PIXELS);
        } else {
            layout.setHeight(28, Unit.PIXELS);
        }

        if (webConfig.getUseLightHeader()) {
            Component appIcon = getLogoImage();
            if (appIcon != null) {
                appIcon.setStyleName("cuba-app-icon");
                layout.addComponent(appIcon);
                layout.setComponentAlignment(appIcon, Alignment.MIDDLE_LEFT);
            }
        }

        menuBar = createMenuBar();
        layout.addComponent(menuBar);
        placeMenuBar(layout);

        createSearchLayout(layout);

        if (webConfig.getUseLightHeader()){
            addUserIndicator(layout);

            addNewWindowButton(layout);

            addLogoutButton(layout);
        }

        return layout;
    }

    /**
     * Create layout containing search components.
     *
     * @param layout parent layout
     */
    protected void createSearchLayout(HorizontalLayout layout) {
        if (AppBeans.get(Configuration.class).getConfig(FtsConfig.class).getEnabled()) {
            HorizontalLayout searchLayout = new HorizontalLayout();
            searchLayout.setMargin(new MarginInfo(false, true, false, true));

            final TextField searchField = new CubaTextField();

            ThemeConstants theme = App.getInstance().getThemeConstants();
            searchField.setWidth(theme.get("cuba.web.AppWindow.searchField.width"));

            if (ui.isTestMode()) {
                searchField.setCubaId("ftsField");
                searchField.setId(ui.getTestIdManager().reserveId("ftsField"));
            }
            searchField.addShortcutListener(new ShortcutListener("fts", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    openSearchWindow(searchField);
                }
            });

            Button searchBtn = new CubaButton();
            searchBtn.setStyleName(BaseTheme.BUTTON_LINK);
            searchBtn.addStyleName("cuba-fts-button");
            searchBtn.setIcon(new VersionedThemeResource("app/images/fts-button.png"));
            searchBtn.addClickListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            openSearchWindow(searchField);
                        }
                    }
            );

            searchLayout.addComponent(searchField);
            searchLayout.addComponent(searchBtn);

            layout.addComponent(searchLayout);
            layout.setComponentAlignment(searchLayout, Alignment.MIDDLE_RIGHT);
        }
    }

    protected void openSearchWindow(TextField searchField) {
        String searchTerm = searchField.getValue();
        if (StringUtils.isBlank(searchTerm))
            return;

        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);

        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo("ftsSearch");
        App.getInstance().getWindowManager().openWindow(
                windowInfo,
                WindowManager.OpenType.NEW_TAB,
                params
        );
    }

    protected void placeMenuBar(HorizontalLayout layout) {
        layout.setComponentAlignment(menuBar, Alignment.MIDDLE_LEFT);
        layout.setExpandRatio(menuBar, 1);
    }

    /**
     * @return a new instance of main menu
     */
    protected CubaMenuBar createMenuBar() {
        menuBar = new CubaMenuBar();
        menuBar.setWidth("100%");
        menuBar.setMoreMenuItem(null); // force usage more item menu

        if (ui.isTestMode()) {
            menuBar.setCubaId("appMenu");
            menuBar.setId(ui.getTestIdManager().reserveId("appMenu"));
        }

        MenuBuilder menuBuilder = new MenuBuilder(this, connection.getSession(), menuBar);
        menuBuilder.build();

        return menuBar;
    }

    /**
     * @return logo label text to show in {@link #titleLayout}
     */
    protected String getLogoLabelCaption() {
        return messages.getMessage(getMessagesPack(), "application.logoLabel");
    }

    /**
     * @return a new instance of {@link #titleLayout}
     */
    protected Layout createTitleLayout() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setStyleName("cuba-app-titlebar");

        titleLayout.setWidth(100, Unit.PERCENTAGE);
        titleLayout.setHeight(41, Unit.PIXELS);

        titleLayout.setMargin(new MarginInfo(false, true, false, true));
        titleLayout.setSpacing(true);

        Component logoImage = getLogoImage();
        if (logoImage != null) {
            logoImage.setStyleName("cuba-app-icon");
            titleLayout.addComponent(logoImage);
            titleLayout.setComponentAlignment(logoImage, Alignment.MIDDLE_LEFT);
        }

        Label logoLabel = new Label(getLogoLabelCaption());
        logoLabel.setStyleName("cuba-app-appname-label");

        titleLayout.addComponent(logoLabel);
        titleLayout.setExpandRatio(logoLabel, 1);
        titleLayout.setComponentAlignment(logoLabel, Alignment.MIDDLE_LEFT);

        addUserLabel(titleLayout);

        addUserIndicator(titleLayout);

        addLogoutButton(titleLayout);

        addNewWindowButton(titleLayout);

        return titleLayout;
    }

    protected void addUserLabel(HorizontalLayout layout) {
        Label userLabel = new Label(messages.getMessage(getMessagesPack(), "loggedInLabel"));
        userLabel.setStyleName("cuba-user-select-label");
        userLabel.setSizeUndefined();

        layout.addComponent(userLabel);
        layout.setComponentAlignment(userLabel, Alignment.MIDDLE_RIGHT);
    }

    protected void addNewWindowButton(HorizontalLayout layout) {
        Button newWindowBtn = createNewWindowButton();

        layout.addComponent(newWindowBtn);
        layout.setComponentAlignment(newWindowBtn, Alignment.MIDDLE_RIGHT);
    }

    protected void addLogoutButton(HorizontalLayout layout) {
        Button logoutBtn = createLogoutButton();

        layout.addComponent(logoutBtn);
        layout.setComponentAlignment(logoutBtn, Alignment.MIDDLE_RIGHT);
    }

    @Nullable
    protected Component getLogoImage() {
        String logoImagePath = messages.getMainMessage("application.logoImage");
        if (StringUtils.isBlank(logoImagePath) || "application.logoImage".equals(logoImagePath))
            return null;

        return new Image(null, new VersionedThemeResource(logoImagePath));
    }

    protected void addUserIndicator(HorizontalLayout parentLayout) {
        UserSession session = App.getInstance().getConnection().getSession();
        if (session == null)
            throw new RuntimeException("No user session found");

        List<UserSubstitution> substitutions = getUserSubstitutions(session);

        if (substitutions.isEmpty()) {
            Label userNameLabel = new Label(getSubstitutedUserCaption(session.getUser()));
            userNameLabel.setStyleName("cuba-user-select-label");
            userNameLabel.setSizeUndefined();
            parentLayout.addComponent(userNameLabel);
            parentLayout.setComponentAlignment(userNameLabel, Alignment.MIDDLE_RIGHT);
        } else {
            if (webConfig.getUseLightHeader()) {
                substUserSelect = new ComboBox();

                ThemeConstants theme = App.getInstance().getThemeConstants();
                substUserSelect.setWidth(theme.get("cuba.web.AppWindow.substUserSelect.width"));
            } else {
                substUserSelect = new NativeSelect();
            }

            substUserSelect.setNullSelectionAllowed(false);
            substUserSelect.setImmediate(true);
            if (ui.isTestMode()) {
                substUserSelect.setCubaId("substitutedUserSelect");
                substUserSelect.setId(ui.getTestIdManager().reserveId("substitutedUserSelect"));
            }
            substUserSelect.setStyleName("cuba-user-select-combobox");
            substUserSelect.addItem(session.getUser());
            substUserSelect.setItemCaption(session.getUser(), getSubstitutedUserCaption(session.getUser()));

            for (UserSubstitution substitution : substitutions) {
                User substitutedUser = substitution.getSubstitutedUser();
                substUserSelect.addItem(substitutedUser);
                substUserSelect.setItemCaption(substitutedUser, getSubstitutedUserCaption(substitutedUser));
            }

            substUserSelect.select(session.getSubstitutedUser() == null ? session.getUser() : session.getSubstitutedUser());
            substUserSelect.addValueChangeListener(new SubstitutedUserChangeListener(substUserSelect));

            parentLayout.addComponent(substUserSelect);
            parentLayout.setComponentAlignment(substUserSelect, Alignment.MIDDLE_RIGHT);
        }
    }

    protected List<UserSubstitution> getUserSubstitutions(UserSession userSession) {
        LoadContext ctx = new LoadContext(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate >= :currentDate) " +
                "and (us.startDate is null or us.startDate <= :currentDate) " +
                "and (us.substitutedUser.active = true or us.substitutedUser.active is null) order by us.substitutedUser.name");
        query.setParameter("userId", userSession.getUser().getId());
        query.setParameter("currentDate", AppBeans.get(TimeSource.class).currentTimestamp());
        ctx.setView("app");
        return AppBeans.get(DataService.class).loadList(ctx);
    }

    protected String getSubstitutedUserCaption(User user) {
        return InstanceUtils.getInstanceName(user);
    }

    protected Button createLogoutButton() {
        String buttonTitle = "";
        if (!webConfig.getUseLightHeader()) {
            buttonTitle = messages.getMessage(getMessagesPack(), "logoutBtn");
        }

        Button logoutBtn = new CubaButton(buttonTitle, new LogoutBtnClickListener());

        logoutBtn.setDescription(messages.getMessage(getMessagesPack(), "logoutBtnDescription"));
        logoutBtn.setStyleName("cuba-buttons-white-border");

        if (webConfig.getUseLightHeader())
            logoutBtn.addStyleName("nocaption");

        logoutBtn.setIcon(new VersionedThemeResource("app/images/exit.png"));

        if (ui.isTestMode()) {
            logoutBtn.setCubaId("logoutBtn");
            logoutBtn.setId(ui.getTestIdManager().reserveId("logoutBtn"));
        }

        return logoutBtn;
    }

    protected Button createNewWindowButton() {
        String buttonTitle = "";
        if (!webConfig.getUseLightHeader()) {
            buttonTitle = messages.getMessage(getMessagesPack(), "newWindowBtn");
        }

        Button newWindowBtn = new CubaButton(buttonTitle);

        URL pageUrl = null;
        try {
            pageUrl = Page.getCurrent().getLocation().toURL();
        } catch (MalformedURLException ignored) {
            log.warn("Couldn't get URL of current Page");
        }

        if (pageUrl != null) {
            ExternalResource currentPage = new ExternalResource(pageUrl);
            final BrowserWindowOpener opener = new BrowserWindowOpener(currentPage);
            opener.setWindowName("_blank");

            opener.extend(newWindowBtn);
        } else
            newWindowBtn.setVisible(false);

        newWindowBtn.setDescription(messages.getMessage(getMessagesPack(), "newWindowBtnDescription"));
        newWindowBtn.setStyleName("cuba-buttons-white-border");

        if (webConfig.getUseLightHeader())
            newWindowBtn.addStyleName("nocaption");

        if (ui.isTestMode()) {
            newWindowBtn.setCubaId("newAppWindowBtn");
            newWindowBtn.setId(ui.getTestIdManager().reserveId("newAppWindowBtn"));
        }

        newWindowBtn.setIcon(new VersionedThemeResource("app/images/new-window.png"));
        return newWindowBtn;
    }

    @Override
    public void userSubstituted(Connection connection) {
        menuBarLayout.replaceComponent(menuBar, createMenuBar());
        placeMenuBar(menuBarLayout);

        if (webConfig.getFoldersPaneEnabled() && foldersPane != null) {
            foldersPane.savePosition();
            FoldersPane oldFoldersPane = foldersPane;
            foldersPane = createFoldersPane();
            if (foldersPane != null) {
                foldersPane.init(foldersSplit);
            }
            foldersSplit.replaceComponent(oldFoldersPane, foldersPane);
        }
        substUserSelect.select(connection.getSession().getCurrentOrSubstitutedUser());

        closeStartupScreen();
        showStartupScreen();
    }

    protected String getMessagesPack() {
        return AppConfig.getMessagesPack();
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        final Object target = variables.get("notificationHidden");
        if (target != null) {
            com.vaadin.ui.Component component = (com.vaadin.ui.Component) target;
            component.setParent(null);
        }
    }

    protected void revertToCurrentUser() {
        UserSession us = app.getConnection().getSession();
        if (us == null)
            throw new RuntimeException("No user session found");

        substUserSelect.select(us.getCurrentOrSubstitutedUser());
    }

    protected class SubstitutedUserChangeListener implements Property.ValueChangeListener {

        protected final AbstractSelect substUserSelect;

        public SubstitutedUserChangeListener(AbstractSelect substUserSelect) {
            this.substUserSelect = substUserSelect;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            User newUser = (User) event.getProperty().getValue();
            UserSession userSession = app.getConnection().getSession();
            if (userSession == null)
                throw new RuntimeException("No user session found");

            User oldUser = userSession.getSubstitutedUser() == null ? userSession.getUser() : userSession.getSubstitutedUser();

            if (!oldUser.equals(newUser)) {
                String name = StringUtils.isBlank(newUser.getName()) ? newUser.getLogin() : newUser.getName();
                app.getWindowManager().showOptionDialog(
                        messages.getMessage(getMessagesPack(), "substUserSelectDialog.title"),
                        messages.formatMessage(getMessagesPack(), "substUserSelectDialog.msg", name),
                        IFrame.MessageType.WARNING,
                        new Action[]{new ChangeSubstUserAction((User) substUserSelect.getValue()) {
                            @Override
                            public void doRevert() {
                                super.doRevert();
                                revertToCurrentUser();
                            }
                        }, new DoNotChangeSubstUserAction() {
                            @Override
                            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                super.actionPerform(component);
                                revertToCurrentUser();
                            }
                        }}
                );
            }
        }
    }

    public static class AppTabSheet extends CubaTabSheet implements com.vaadin.event.Action.Handler {

        private static final long serialVersionUID = 623307791240239175L;

        protected Map<Component, TabCloseHandler> closeHandlers = null;

        protected com.vaadin.event.Action closeAllTabs;

        protected com.vaadin.event.Action closeOtherTabs;

        protected com.vaadin.event.Action closeCurrentTab;

        protected com.vaadin.event.Action showInfo;

        public AppTabSheet() {
            setCloseHandler(new CloseHandler() {
                @Override
                public void onTabClose(TabSheet tabsheet, Component tabContent) {
                    if (closeHandlers != null) {
                        TabCloseHandler closeHandler = closeHandlers.get(tabContent);
                        if (closeHandler != null) {
                            closeHandler.onClose(AppTabSheet.this, tabContent);
                        }
                    }
                }
            });

            addActionHandler(this);

            Messages messages = AppBeans.get(Messages.class);
            closeAllTabs = new com.vaadin.event.Action(messages.getMainMessage("actions.closeAllTabs"));
            closeOtherTabs = new com.vaadin.event.Action(messages.getMainMessage("actions.closeOtherTabs"));
            closeCurrentTab = new com.vaadin.event.Action(messages.getMainMessage("actions.closeCurrentTab"));
            showInfo = new com.vaadin.event.Action(messages.getMainMessage("actions.showInfo"));
        }

        @Override
        public void removeComponent(Component c) {
            super.removeComponent(c);
            if (c != null && closeHandlers != null) {
                closeHandlers.remove(c);
                if (closeHandlers.isEmpty()) {
                    closeHandlers = null;
                }
            }
        }

        public void moveTab(Component c, int position) {
            Tab oldTab = getTab(c);

            // do not detach close handler
            // call super
            super.removeComponent(oldTab.getComponent());

            Tab newTab = addTab(c, position);

            newTab.setCaption(oldTab.getCaption());
            newTab.setDescription(oldTab.getDescription());
            newTab.setClosable(oldTab.isClosable());
            newTab.setEnabled(oldTab.isEnabled());
            newTab.setVisible(oldTab.isVisible());
            newTab.setIcon(oldTab.getIcon());
            newTab.setStyleName(oldTab.getStyleName());
        }

        public void setTabCloseHandler(Component tabContent, TabCloseHandler closeHandler) {
            if (closeHandlers == null) {
                closeHandlers = new LinkedHashMap<>();
            }
            closeHandlers.put(tabContent, closeHandler);
        }

         public com.haulmont.cuba.gui.components.Window.Editor findEditor(Layout layout) {
             for (Object component : layout) {
                 if (component instanceof WindowBreadCrumbs) {
                     WindowBreadCrumbs breadCrumbs = (WindowBreadCrumbs) component;
                     if (breadCrumbs.getCurrentWindow() instanceof Window.Editor)
                         return (Window.Editor) breadCrumbs.getCurrentWindow();
                 }
             }
             return null;
        }

        @Override
        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            if (target != null) {
                UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
                if (userSession.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION) &&
                        findEditor((Layout) target) != null ) {
                    return new com.vaadin.event.Action[]{
                            closeCurrentTab, closeOtherTabs, closeAllTabs, showInfo
                    };
                }
            } else {
                return new com.vaadin.event.Action[]{
                        closeCurrentTab, closeOtherTabs, closeAllTabs, showInfo
                };
            }
            return new com.vaadin.event.Action[]{
                    closeCurrentTab, closeOtherTabs, closeAllTabs
            };
        }

        @Override
        public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
            if (action.equals(closeCurrentTab)) {
                closeTab((com.vaadin.ui.Component) target);
            } else if (action.equals(closeOtherTabs)) {
                closeOtherTabs((com.vaadin.ui.Component) target);
            } else if (action.equals(closeAllTabs)) {
                closeAllTabs();
            } else if (action.equals(showInfo)) {
                showInfo(target);
            }
        }

        public void closeAllTabs() {
            Set<Component> tabs = new HashSet<>(this.tabs.keySet());
            for (final Component tab : tabs) {
                closeTab(tab);
            }
        }

        public void closeOtherTabs(Component currentTab) {
            Set<Component> tabs = new HashSet<>(this.tabs.keySet());
            for (final Component tab : tabs) {
                if (tab.equals(currentTab)) continue;
                closeTab(tab);
            }
        }

        public void showInfo(Object target) {
            com.haulmont.cuba.gui.components.Window.Editor editor = findEditor((Layout) target);
            Entity entity = editor.getItem();
            MetaClass metaClass = AppBeans.get(Metadata.class).getSession().getClass(entity.getClass());
            new ShowInfoAction().showInfo(entity, metaClass, editor);
        }

        public interface TabCloseHandler {
            void onClose(TabSheet tabSheet, Component tabContent);
        }
    }

    protected class LogoutBtnClickListener implements Button.ClickListener {

        private static final long serialVersionUID = 4885156177472913997L;

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if (foldersPane != null) {
                foldersPane.savePosition();
            }
            app.reinitializeAppearanceProperties();
            app.getWindowManager().checkModificationsAndCloseAll(
                    new Runnable() {
                        @Override
                        public void run() {
                            String redirectionUrl = connection.logout();
                            // vaadin7 unused redirectionUrl
                        }
                    },
                    null
            );
        }
    }
}