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
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.actions.ChangeSubstUserAction;
import com.haulmont.cuba.web.actions.DoNotChangeSubstUserAction;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.WebTimer;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.sys.MenuBuilder;
import com.haulmont.cuba.web.sys.WindowBreadCrumbs;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import com.haulmont.cuba.web.toolkit.ui.RichNotification;
import com.haulmont.cuba.web.toolkit.ui.WindowOpenButton;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.terminal.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Main application window.
 * <p/>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link com.haulmont.cuba.web.App#createAppWindow()} method
 *
 * @author krivopustov
 * @version $Id$
 */
public class AppWindow extends Window implements UserSubstitutionListener,
        JavaScriptHost.HistoryBackHandler, JavaScriptHost.ServerCallHandler {

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

    private static final Log log = LogFactory.getLog(AppWindow.class);

    protected Connection connection;

    protected GlobalConfig globalConfig;
    protected WebConfig webConfig;

    protected com.haulmont.cuba.web.toolkit.ui.MenuBar menuBar;
    protected TabSheet tabSheet;
    protected WebSplitPanel foldersSplit;

    protected Mode mode;

    protected LinkedList<RichNotification> richNotifications;

    /**
     * Very root layout of the window. Contains all other layouts
     */
    protected VerticalLayout rootLayout;

    /**
     * Title layout. Topmost by default
     */
    protected Layout titleLayout;

    /**
     * Layout containing the menu bar. Next to title layout by default
     */
    protected HorizontalLayout menuBarLayout;

    /**
     * Empty layout, below menu bar layout by default
     */
    protected HorizontalLayout emptyLayout;

    protected HorizontalLayout middleLayout;

    protected FoldersPane foldersPane;

    /**
     * Layout containing application screens
     */
    protected VerticalLayout mainLayout;

    protected Messages messages;

    protected UserSettingsTools userSettingsTools;

    protected AbstractSelect substUserSelect;

    protected JavaScriptHost scriptHost;

    protected final App app;

    protected WebWindowManager windowManager;

    protected WebTimer workerTimer;

    protected ShortcutListener nextTabShortcut;
    protected ShortcutListener previousTabShortcut;

    public AppWindow(App app) {
        log.trace("Creating " + this);
        this.app = app;

        Configuration configuration = AppBeans.get(Configuration.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);

        messages = AppBeans.get(Messages.class);
        userSettingsTools = AppBeans.get(UserSettingsTools.class);

        this.connection = app.getConnection();
        windowManager = createWindowManager();
        setCaption(getAppCaption());

        mode = userSettingsTools.loadAppWindowMode();

        setSizeFull();

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

    public WebTimer getWorkerTimer() {
        if (workerTimer != null) {
            return workerTimer;
        }

        workerTimer = new WebTimer(webConfig.getUiCheckInterval(), true);
        return workerTimer;
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

    private void updateClientSystemMessages() {
        Map<String, String> localeMessages = new HashMap<>();
        App.CubaSystemMessages systemMessages = App.compileSystemMessages(App.getInstance().getLocale());

        localeMessages.put("communicationErrorCaption", systemMessages.getCommunicationErrorCaption());
        localeMessages.put("communicationErrorMessage", systemMessages.getCommunicationErrorMessage());

        localeMessages.put("authorizationErrorCaption", systemMessages.getAuthenticationErrorCaption());
        localeMessages.put("authorizationErrorMessage", systemMessages.getCommunicationErrorMessage());

        localeMessages.put("blockUiMessage",systemMessages.getUiBlockingMessage());

        getScriptHost().updateLocale(localeMessages);
    }

    private void initStaticComponents() {
        scriptHost = new JavaScriptHost();
        if (webConfig.getAllowHandleBrowserHistoryBack()) {
            scriptHost.setHistoryBackHandler(this);
        }
        scriptHost.setServerCallHandler(this);
        addComponent(scriptHost);
    }

    @Override
    public void onHistoryBackPerformed() {
        // Go back to the Future!
    }

    private void checkSessions() {
        Map<String, Object> info = AppBeans.get(UserSessionService.class).getLicenseInfo();
        Integer licensed = (Integer) info.get("licensedSessions");
        if (licensed < 0) {
            showNotification("Invalid CUBA platform license", Notification.TYPE_WARNING_MESSAGE);
        } else {
            Integer active = (Integer) info.get("activeSessions");
            if (licensed != 0 && active > licensed) {
                showNotification("Number of licensed sessions exceeded", "active: " + active + ", licensed: " + licensed,
                        Notification.TYPE_WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void onJsServerCall(String[] params) {
        // handle js api call
        if (params != null)
            log.debug("Client JS API Call with params [" + StringUtils.join(params, ',') + "]");
    }

    /**
     * @return Current mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Creates root and enclosed layouts.
     * <br>Can be overridden in descendant to create an app-specific root layout
     *
     * @return App layout
     */
    protected void createLayout(VerticalLayout layout) {
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setSizeFull();

        if (!webConfig.getUseLightHeader()) {
            titleLayout = createTitleLayout();
            layout.addComponent(titleLayout);
        }

        menuBarLayout = createMenuBarLayout();

        layout.addComponent(menuBarLayout);

        emptyLayout = new HorizontalLayout();
        emptyLayout.setMargin(false);
        emptyLayout.setSpacing(false);
        emptyLayout.setSizeFull();

        layout.addComponent(emptyLayout);

        middleLayout = new HorizontalLayout();

        if (Mode.TABBED.equals(getMode())) {
            middleLayout.addStyleName("work-area");
        } else
            middleLayout.addStyleName("work-area-single");

        middleLayout.setSizeFull();

        if (webConfig.getFoldersPaneEnabled()) {
            foldersPane = createFoldersPane();

            if (foldersPane != null) {
                foldersSplit = new WebSplitPanel();
                SplitPanel vFoldersSplit = foldersSplit.getComponent();

                if (webConfig.getUseLightHeader()) {
                    foldersSplit.setShowHookButton(true);
                    vFoldersSplit.setImmediate(true);
                    foldersPane.setVisible(true);
                    foldersSplit.setDefaultPosition(webConfig.getFoldersPaneDefaultWidth() + "px");
                }

                foldersSplit.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
                vFoldersSplit.setSplitPosition(0, UNITS_PIXELS);

                if (!webConfig.getUseLightHeader())
                    foldersSplit.setLocked(true);

                vFoldersSplit.addComponent(foldersPane);

                middleLayout.addComponent(vFoldersSplit);
                middleLayout.setExpandRatio(vFoldersSplit, 1);

                foldersPane.init(vFoldersSplit);
            }
        }

        layout.addComponent(middleLayout);
        layout.setExpandRatio(middleLayout, 1);
    }

    /**
     * Creates folders pane.
     * <br>Can be overridden in descendant to create an app-specific folders pane.
     * <br>If this method returns null, no folders functionality is available for application.
     * @return FoldersPane container
     */
    @Nullable
    protected FoldersPane createFoldersPane() {
        return new FoldersPane(menuBar, this);
    }

    /**
     * Can be overridden in descendant to create an app-specific caption
     * @return Application caption
     */
    protected String getAppCaption() {
        return messages.getMessage(getMessagesPack(), "application.caption");
    }

    /**
     * Enclosed Tabsheet
     *
     * @return the tabsheet in TABBED mode, null in SINGLE mode
     */
    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public void setTabSheet(@Nullable TabSheet tabSheet) {
        this.tabSheet = tabSheet;
    }

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

    @Nullable
    public FoldersPane getFoldersPane() {
        return foldersPane;
    }

    /**
     * Native client script invoker
     * @return JavaScriptHost - specific client side bridge
     */
    public JavaScriptHost getScriptHost() {
        return scriptHost;
    }

    /**
     *  Initialize the startup screen - main window content when no application screens are open.
     */
    protected void initStartupScreen() {
        if (mainLayout != null) {
            if (foldersPane != null) {
                SplitPanel vFoldersSplit = foldersSplit.getComponent();

                vFoldersSplit.removeComponent(mainLayout);
            } else {
                middleLayout.removeComponent(mainLayout);
            }
            mainLayout = null;
        }
        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        if (foldersPane != null) {
            SplitPanel vFoldersSplit = foldersSplit.getComponent();

            vFoldersSplit.addComponent(mainLayout);
        } else {
            middleLayout.addComponent(mainLayout);
            middleLayout.setExpandRatio(mainLayout, 1);
        }
        mainLayout.setSpacing(false);
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
                if (app.isTestMode()) {
                    tabSheet.setCubaId("appTabSheet");
                }
                tabSheet.setSizeFull();
                mainLayout.addComponent(tabSheet);
                mainLayout.setExpandRatio(tabSheet, 1);

                createTabShortcuts();
            }
        }

        if (closeShortcut == null)
            closeShortcut = getWindowManager().createCloseShortcut();
        addAction(closeShortcut);
    }

    protected void createTabShortcuts() {
        if (nextTabShortcut == null) {
            nextTabShortcut = windowManager.createNextWindowTabShortcut();

            addAction(nextTabShortcut);
        }

        if (previousTabShortcut == null) {
            previousTabShortcut = windowManager.createPreviousWindowTabShortcut();

            addAction(previousTabShortcut);
        }
    }

    /**
     * Called by constructor when all layouts are created but before {@link #initStartupScreen()}.
     */
    protected void postInitLayout() {
        String themeName = AppContext.getProperty("cuba.web.theme");
        if (themeName == null) themeName = App.THEME_NAME;
        themeName = userSettingsTools.loadAppWindowTheme() == null ? themeName : userSettingsTools.loadAppWindowTheme();
        if (!StringUtils.equals(themeName, getTheme())) {
            setTheme(themeName);
            // set cookie
            App.getInstance().setUserAppTheme(themeName);
        }
    }

    /**
     * Can be overridden in descendant to create an app-specific menu bar layout
     *
     * @return MenuBar layout
     */
    protected HorizontalLayout createMenuBarLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setStyleName("menubar");
        layout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        if (webConfig.getUseLightHeader()){
            layout.setHeight(40, Sizeable.UNITS_PIXELS);
        } else {
            layout.setHeight(28, Sizeable.UNITS_PIXELS);
        }

        if (webConfig.getUseLightHeader()) {
            Embedded appIcon = getLogoImage();
            if (appIcon != null) {
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
            searchLayout.setMargin(false, true, false, true);

            final TextField searchField = new com.haulmont.cuba.web.toolkit.ui.TextField();
            searchField.setWidth(120, Sizeable.UNITS_PIXELS);
            searchField.setDebugId("ftsField." + (int) (Math.random() * 1000000));
            if (app.isTestMode()) {
                searchField.setCubaId("ftsField");
            }
            searchField.addShortcutListener(new ShortcutListener("fts", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    openSearchWindow(searchField);
                }
            });

            Button searchBtn = new Button();
            searchBtn.setStyleName(BaseTheme.BUTTON_LINK);
            searchBtn.addStyleName("cuba-fts-button");
            searchBtn.setIcon(new VersionedThemeResource("app/images/fts-button.png"));
            searchBtn.addListener(
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
        String searchTerm = (String) searchField.getValue();
        if (StringUtils.isBlank(searchTerm))
            return;

        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);

        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo("ftsSearch");
        getWindowManager().openWindow(
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
     * Can be overridden in descendant to create an app-specific menu bar
     *
     * @return MenuBar
     */
    protected com.haulmont.cuba.web.toolkit.ui.MenuBar createMenuBar() {
        menuBar = new com.haulmont.cuba.web.toolkit.ui.MenuBar();
        menuBar.setWidth("100%");
        menuBar.setMoreMenuItem(null);
        menuBar.getMoreMenuItem().setIcon(new VersionedThemeResource("app/images/more-item.png"));

        if (globalConfig.getTestMode()) {
            getWindowManager().setDebugId(menuBar, "appMenu");
        }

        if (app.isTestMode()) {
            menuBar.setCubaId("appMenu");
        }

        MenuBuilder menuBuilder = new MenuBuilder(this, connection.getSession(), menuBar);
        menuBuilder.build();

        return menuBar;
    }

    /*
     * Can be overriding by client application to change title caption
     */

    protected String getLogoLabelCaption() {
        return messages.getMessage(getMessagesPack(), "application.logoLabel");
    }

    /**
     * Can be overridden in descendant to create an app-specific title layout
     *
     * @return Title layout
     */
    protected Layout createTitleLayout() {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setStyleName("titlebar");

        titleLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        titleLayout.setHeight(41, Sizeable.UNITS_PIXELS);

        titleLayout.setMargin(false, true, false, true);
        titleLayout.setSpacing(true);

        Embedded logoImage = getLogoImage();
        if (logoImage != null) {
            titleLayout.addComponent(logoImage);
            titleLayout.setComponentAlignment(logoImage, Alignment.MIDDLE_LEFT);
        }

        Label logoLabel = new Label(getLogoLabelCaption());
        logoLabel.setStyleName("appname");

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
        userLabel.setStyleName("select-label");
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
    protected Embedded getLogoImage() {
        String logoImagePath = messages.getMainMessage("application.logoImage");
        if ("application.logoImage".equals(logoImagePath))
            return null;

        return new Embedded(null, new VersionedThemeResource(logoImagePath));
    }

    protected void addUserIndicator(HorizontalLayout parentLayout) {
        UserSession session = App.getInstance().getConnection().getSession();
        if (session == null)
            throw new RuntimeException("No user session found");

        List<UserSubstitution> substitutions = getUserSubstitutions(session);

        if (substitutions.isEmpty()) {
            Label userNameLabel = new Label(getSubstitutedUserCaption(session.getUser()));
            userNameLabel.setStyleName("select-label");
            userNameLabel.setSizeUndefined();
            parentLayout.addComponent(userNameLabel);
            parentLayout.setComponentAlignment(userNameLabel, Alignment.MIDDLE_RIGHT);
        } else {
            if (webConfig.getUseLightHeader()) {
                substUserSelect = new ComboBox();
                substUserSelect.setWidth("200px");
            } else
                substUserSelect = new NativeSelect();

            substUserSelect.setNullSelectionAllowed(false);
            substUserSelect.setImmediate(true);
            substUserSelect.setStyleName("user-select-combobox");
            substUserSelect.addItem(session.getUser());
            substUserSelect.setItemCaption(session.getUser(), getSubstitutedUserCaption(session.getUser()));

            for (UserSubstitution substitution : substitutions) {
                User substitutedUser = substitution.getSubstitutedUser();
                substUserSelect.addItem(substitutedUser);
                substUserSelect.setItemCaption(substitutedUser, getSubstitutedUserCaption(substitutedUser));
            }

            substUserSelect.select(session.getSubstitutedUser() == null ? session.getUser() : session.getSubstitutedUser());
            substUserSelect.addListener(new SubstitutedUserChangeListener(substUserSelect));

            parentLayout.addComponent(substUserSelect);
            parentLayout.setComponentAlignment(substUserSelect, Alignment.MIDDLE_RIGHT);

            if (app.isTestMode()) {
                substUserSelect.setCubaId("substitutedUserSelect");
            }
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

    protected Button createLogoutButton() {
        String buttonTitle = "";
        if (!webConfig.getUseLightHeader())
            buttonTitle = messages.getMessage(getMessagesPack(), "logoutBtn");

        Button logoutBtn = new Button(
                buttonTitle,
                new LogoutBtnClickListener()
        );
        logoutBtn.setDescription(messages.getMessage(getMessagesPack(), "logoutBtnDescription"));
        logoutBtn.setStyleName("white-border");
        logoutBtn.setIcon(new VersionedThemeResource("app/images/exit.png"));
        getWindowManager().setDebugId(logoutBtn, "logoutBtn");

        if (app.isTestMode()) {
            logoutBtn.setCubaId("logoutBtn");
        }

        return logoutBtn;
    }

    protected Button createNewWindowButton() {
        String buttonTitle = "";
        if (!webConfig.getUseLightHeader())
            buttonTitle = messages.getMessage(getMessagesPack(), "newWindowBtn");

        // Use special button to force open new window as tab
        WindowOpenButton newWindowBtn = new WindowOpenButton();
        newWindowBtn.setCaption(buttonTitle);
        newWindowBtn.setUrlProvider(new WindowOpenButton.UrlProvider() {
            @Override
            public String getUrl() {
                return App.getInstance().getURL() + App.generateWebWindowName();
            }
        });

        newWindowBtn.setDescription(messages.getMessage(getMessagesPack(), "newWindowBtnDescription"));
        newWindowBtn.setStyleName("white-border");
        newWindowBtn.setIcon(new VersionedThemeResource("app/images/new-window.png"));

        newWindowBtn.setCubaId("newAppWindowBtn");

        return newWindowBtn;
    }

    protected String getSubstitutedUserCaption(User user) {
        return InstanceUtils.getInstanceName(user);
    }

    @Override
    public void userSubstituted(Connection connection) {
        menuBarLayout.replaceComponent(menuBar, createMenuBar());
        placeMenuBar(menuBarLayout);

        if (webConfig.getFoldersPaneEnabled() && foldersPane != null) {
            foldersPane.savePosition();
            FoldersPane oldFoldersPane = foldersPane;
            foldersPane = createFoldersPane();

            SplitPanel vFoldersSplit = foldersSplit.getComponent();

            if (foldersPane != null) {
                foldersPane.init(vFoldersSplit);
            }
            vFoldersSplit.replaceComponent(oldFoldersPane, foldersPane);
        }
        substUserSelect.select(connection.getSession().getCurrentOrSubstitutedUser());
    }

    protected String getMessagesPack() {
        return AppConfig.getMessagesPack();
    }

    public void showRichNotification(RichNotification notification) {
        if (richNotifications == null) {
            richNotifications = new LinkedList<>();
        }
        if (notification.getLayout() != null) {
            notification.getLayout().setParent(this);
        }
        richNotifications.add(notification);
        requestRepaint();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        // Paint richNotifications
        if (richNotifications != null) {
            target.startTag("richNotifications");
            for (final RichNotification n : richNotifications) {
                target.startTag("richNotification");
                if (n.getCaption() != null) {
                    target.addAttribute("caption", n.getCaption());
                }
                if (n.getDescription() != null) {
                    target.addAttribute("message", n.getDescription());
                }
                if (n.getIcon() != null) {
                    target.addAttribute("icon", n.getIcon());
                }
                if (n.isAutoFade()) {
                    target.addAttribute("autoFade", true);
                }
                target.addAttribute("position", n.getPosition());
                target.addAttribute("delay", n.getDelayMsec());
                if (n.getStyleName() != null) {
                    target.addAttribute("style", n.getStyleName());
                }
                if (n.getLayout() != null) {
                    n.getLayout().paint(target);
                }
                target.endTag("richNotification");
            }
            target.endTag("richNotifications");

            richNotifications = null;
        }
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
        UserSession us = App.getInstance().getConnection().getSession();
        if (us == null)
            throw new RuntimeException("No user session found");

        substUserSelect.select(us.getCurrentOrSubstitutedUser());
    }

    protected class SubstitutedUserChangeListener implements Property.ValueChangeListener {

        private final AbstractSelect substUserSelect;

        public SubstitutedUserChangeListener(AbstractSelect substUserSelect) {
            this.substUserSelect = substUserSelect;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            User newUser = (User) event.getProperty().getValue();
            UserSession userSession = App.getInstance().getConnection().getSession();
            if (userSession == null)
                throw new RuntimeException("No user session found");

            User oldUser = userSession.getSubstitutedUser() == null ? userSession.getUser() : userSession.getSubstitutedUser();

            if (!oldUser.equals(newUser)) {
                String name = StringUtils.isBlank(newUser.getName()) ? newUser.getLogin() : newUser.getName();
                getWindowManager().showOptionDialog(
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

    public static class AppTabSheet extends ActionsTabSheet implements com.vaadin.event.Action.Handler {

        private static final long serialVersionUID = 623307791240239175L;

        private Map<Component, TabCloseHandler> closeHandlers = null;

        private com.vaadin.event.Action closeAllTabs;

        private com.vaadin.event.Action closeOtherTabs;

        private com.vaadin.event.Action closeCurrentTab;

        private com.vaadin.event.Action showInfo;

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

        public void setTabCloseHandler(Component tabContent, TabCloseHandler closeHandler) {
            if (closeHandlers == null) {
                closeHandlers = new LinkedHashMap<>();
            }
            closeHandlers.put(tabContent, closeHandler);
        }

         public com.haulmont.cuba.gui.components.Window.Editor findEditor(Layout layout) {
            Iterator<Component> iterator = layout.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component instanceof WindowBreadCrumbs) {
                    WindowBreadCrumbs breadCrumbs = (WindowBreadCrumbs) component;
                    if (breadCrumbs.getCurrentWindow() instanceof com.haulmont.cuba.gui.components.Window.Editor)
                        return (com.haulmont.cuba.gui.components.Window.Editor) breadCrumbs.getCurrentWindow();
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
            final App app = App.getInstance();
            app.reinitializeAppearanceProperties();
            getWindowManager().checkModificationsAndCloseAll(
                    new Runnable() {
                        @Override
                        public void run() {
                            String redirectionUrl = connection.logout();
                            open(new ExternalResource(App.getInstance().getURL() + redirectionUrl));
                        }
                    },
                    null
            );
        }
    }
}