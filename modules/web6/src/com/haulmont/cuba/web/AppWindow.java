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
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.actions.ChangeSubstUserAction;
import com.haulmont.cuba.web.actions.DoNotChangeSubstUserAction;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.toolkit.MenuShortcutAction;
import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import com.haulmont.cuba.web.toolkit.ui.RichNotification;
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
@SuppressWarnings("unused")
public class AppWindow extends Window implements UserSubstitutionListener,
        JavaScriptHost.HistoryBackHandler, JavaScriptHost.ServerCallHandler {

    private static final long serialVersionUID = 7269808125566032433L;

    private static final Log log = LogFactory.getLog(AppWindow.class);

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

    private AbstractSelect substUserSelect;

    private JavaScriptHost scriptHost;

    public AppWindow(Connection connection) {
        super();

        Configuration configuration = AppBeans.get(Configuration.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);
        webConfig = configuration.getConfig(WebConfig.class);

        messages = AppBeans.get(Messages.class);
        userSettingsTools = AppBeans.get(UserSettingsTools.class);

        this.connection = connection;
        setCaption(getAppCaption());

        mode = userSettingsTools.loadAppWindowMode();

        rootLayout = createLayout();
        initLayout();
        setContent(rootLayout);
        postInitLayout();
        initStartupLayout();

        initStaticComponents();

        updateClientSystemMessages();
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
    protected VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();

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

                if (webConfig.getUseLightHeader()) {
                    foldersSplit.setShowHookButton(true);
                    foldersSplit.setImmediate(true);
                    foldersPane.setVisible(true);
                    foldersSplit.setDefaultPosition(webConfig.getFoldersPaneDefaultWidth() + "px");
                }

                foldersSplit.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
                foldersSplit.setSplitPosition(0, UNITS_PIXELS);

                if (!webConfig.getUseLightHeader())
                    foldersSplit.setLocked(true);

                foldersSplit.addComponent(foldersPane);

                middleLayout.addComponent(foldersSplit);
                middleLayout.setExpandRatio(foldersSplit, 1);

                foldersPane.init(foldersSplit);
            }
        }

        layout.addComponent(middleLayout);
        layout.setExpandRatio(middleLayout, 1);

        return layout;
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
     * Can be overridden in descendant to init an app-specific layout
     */
    protected void initLayout() {
    }

    private void genericStartupLayout() {
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

    /* Draw startup screen layout */

    protected void initStartupLayout() {
        genericStartupLayout();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);
    }

    /*  */

    protected void unInitStartupLayout() {
        genericStartupLayout();
        mainLayout.setMargin(new Layout.MarginInfo(true,false,false,false));
        mainLayout.setSpacing(true);
    }

    /**
     * Can be overridden in descendant to init an app-specific layout
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

        if (AppBeans.get(Configuration.class).getConfig(FtsConfig.class).getEnabled()) {
            HorizontalLayout searchLayout = new HorizontalLayout();
            searchLayout.setMargin(false, true, false, true);

            final TextField searchField = new com.haulmont.cuba.web.toolkit.ui.TextField();
            searchField.setWidth(120, Sizeable.UNITS_PIXELS);
            searchField.setDebugId("ftsField." + (int) (Math.random() * 1000000));
            searchField.addShortcutListener(new ShortcutListener("fts", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    openSearchWindow(searchField);
                }
            });

            Button searchBtn = new Button();
            searchBtn.setStyleName(BaseTheme.BUTTON_LINK);
            searchBtn.setIcon(new ThemeResource("select/img/fts-btn.png"));
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

        if (webConfig.getUseLightHeader()){
            addUserSelect(layout);

            addNewWindowButton(layout);

            addLogoutButton(layout);
        }

        return layout;
    }

    protected void openSearchWindow(TextField searchField) {
        String searchTerm = (String) searchField.getValue();
        if (StringUtils.isBlank(searchTerm))
            return;

        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);

        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo("fts$Search");
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
     * Can be overridden in descendant to create an app-specific menu bar
     *
     * @return MenuBar
     */
    protected com.haulmont.cuba.web.toolkit.ui.MenuBar createMenuBar() {
        menuBar = new com.haulmont.cuba.web.toolkit.ui.MenuBar();
        menuBar.setWidth("100%");
        menuBar.setMoreMenuItem(null);
        menuBar.getMoreMenuItem().setIcon(new ThemeResource("icons/more-item.png"));

        if (globalConfig.getTestMode()) {
            App.getInstance().getWindowManager().setDebugId(menuBar, "appMenu");
        }

        final UserSession session = connection.getSession();
        final MenuConfig menuConfig = AppBeans.get(MenuConfig.class);
        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem menuItem : rootItems) {
            if (menuItem.isPermitted(session)) {
                createMenuBarItem(menuBar, menuItem);
            }
        }
        removeExtraSeparators(menuBar);

        return menuBar;
    }

    private void removeExtraSeparators(MenuBar menuBar) {
        for (MenuBar.MenuItem item : new ArrayList<>(menuBar.getItems())) {
            removeExtraSeparators(item);
            if (isMenuItemEmpty(item))
                menuBar.removeItem(item);
        }
    }

    private void removeExtraSeparators(MenuBar.MenuItem item) {
        if (!item.hasChildren())
            return;

        boolean done;
        do {
            done = true;
            if (item.hasChildren()) {
                List<MenuBar.MenuItem> children = new ArrayList<>(item.getChildren());
                for (int i = 0; i < children.size(); i++) {
                    MenuBar.MenuItem child = children.get(i);
                    removeExtraSeparators(child);
                    if (isMenuItemEmpty(child) && (i == 0 || i == children.size() - 1 || isMenuItemEmpty(children.get(i + 1)))) {
                        item.removeChild(child);
                        done = false;
                    }
                }
            }
        } while (!done);
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

        addUserSelect(titleLayout);

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

        return new Embedded(null, new ThemeResource(logoImagePath));
    }

    private void assignShortcut(MenuBar.MenuItem menuItem, MenuItem item) {
        if (item.getShortcut() != null) {
            MenuShortcutAction shortcut = new MenuShortcutAction(menuItem, "shortcut_" + item.getId(), item.getShortcut());
            this.addAction(shortcut);
            menuBar.setShortcut(menuItem, item.getShortcut());
        }
    }

    private boolean isMenuItemEmpty(MenuBar.MenuItem menuItem) {
        return !menuItem.hasChildren() && menuItem.getCommand() == null;
    }

    private void createMenuBarItem(MenuBar menuBar, MenuItem item) {
        if (!connection.isConnected()) return;

        final UserSession session = connection.getSession();
        if (item.isPermitted(session)) {
            MenuBar.MenuItem menuItem = menuBar.addItem(MenuConfig.getMenuItemCaption(item.getId()), createMenuBarCommand(item));
            assignShortcut(menuItem, item);
            createSubMenu(menuItem, item, session);
            assignDebugIds(menuItem, item);
            if (isMenuItemEmpty(menuItem)) {
                menuBar.removeItem(menuItem);
            }
        }
    }

    protected void addUserSelect(HorizontalLayout parentLayout) {

        if (webConfig.getUseLightHeader()) {
            substUserSelect = new FilterSelect();
            substUserSelect.setWidth("200px");
        } else
            substUserSelect = new NativeSelect();

        substUserSelect.setNullSelectionAllowed(false);
        substUserSelect.setImmediate(true);
        substUserSelect.setStyleName("select-label");

        fillSubstitutedUsers(substUserSelect);
        if (substUserSelect.getItemIds().size() > 1) {
            UserSession us = App.getInstance().getConnection().getSession();
            if (us == null)
                throw new RuntimeException("No user session found");

            substUserSelect.select(us.getSubstitutedUser() == null ? us.getUser() : us.getSubstitutedUser());
            substUserSelect.addListener(new SubstitutedUserChangeListener(substUserSelect));

            parentLayout.addComponent(substUserSelect);
            parentLayout.setComponentAlignment(substUserSelect, Alignment.MIDDLE_RIGHT);
        } else {
            Label userNameLabel = new Label(getSubstitutedUserCaption((User) substUserSelect.getItemIds().iterator().next()));
            userNameLabel.setStyleName("select-label");
            userNameLabel.setSizeUndefined();
            parentLayout.addComponent(userNameLabel);
            parentLayout.setComponentAlignment(userNameLabel, Alignment.MIDDLE_RIGHT);
        }
    }

    private Button createLogoutButton() {
        String buttonTitle = "";
        if (!webConfig.getUseLightHeader())
            buttonTitle = messages.getMessage(getMessagesPack(), "logoutBtn");

        Button logoutBtn = new Button(
                buttonTitle,
                new LogoutBtnClickListener()
        );
        logoutBtn.setDescription(messages.getMessage(getMessagesPack(), "logoutBtnDescription"));
        logoutBtn.setStyleName("white-border");
        logoutBtn.setIcon(new ThemeResource("images/exit.png"));
        App.getInstance().getWindowManager().setDebugId(logoutBtn, "logoutBtn");
        return logoutBtn;
    }

    private Button createNewWindowButton() {
        String buttonTitle = "";
        if (!webConfig.getUseLightHeader())
            buttonTitle = messages.getMessage(getMessagesPack(), "newWindowBtn");

        Button newWindowBtn = new Button(buttonTitle,
                new Button.ClickListener() {
                    private static final long serialVersionUID = -2017737447316558248L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        String name = App.generateWebWindowName();
                        open(new ExternalResource(App.getInstance().getURL() + name), "_new");
                    }
                }
        );
        newWindowBtn.setDescription(messages.getMessage(getMessagesPack(), "newWindowBtnDescription"));
        newWindowBtn.setStyleName("white-border");
        newWindowBtn.setIcon(new ThemeResource("images/new-window.png"));
        return newWindowBtn;
    }

    private void createSubMenu(MenuBar.MenuItem vItem, MenuItem item, UserSession session) {
        if (item.isPermitted(session) && !item.getChildren().isEmpty()) {
            for (MenuItem child : item.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = (child.isSeparator()) ? vItem.addSeparator() : vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), createMenuBarCommand(child));
                        assignShortcut(menuItem, child);
                        assignDebugIds(menuItem, child);
                    }
                } else {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), null);
                        assignShortcut(menuItem, child);
                        createSubMenu(menuItem, child, session);
                        assignDebugIds(menuItem, child);
                        if (isMenuItemEmpty(menuItem)) {
                            vItem.removeChild(menuItem);
                        }
                    }
                }
            }
        }
    }

    private void assignDebugIds(MenuBar.MenuItem menuItem, MenuItem conf) {
        if (menuBar.getDebugId() != null && !conf.isSeparator()) {
            menuBar.setDebugId(menuItem, menuBar.getDebugId() + ":" + conf.getId());
        }
    }

    private MenuBar.Command createMenuBarCommand(final MenuItem item) {
        final WindowInfo windowInfo;
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
        try {
            windowInfo = windowConfig.getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            return null;
        }
        final MenuCommand command = new MenuCommand(App.getInstance().getWindowManager(), item, windowInfo);
        return new com.vaadin.ui.MenuBar.Command() {

            @Override
            public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                command.execute();
            }
        };
    }

    protected void fillSubstitutedUsers(AbstractSelect select) {
        UserSession userSession = App.getInstance().getConnection().getSession();

        if (userSession == null)
            throw new RuntimeException("No user session found");

        select.addItem(userSession.getUser());
        select.setItemCaption(userSession.getUser(), getSubstitutedUserCaption(userSession.getUser()));

        LoadContext ctx = new LoadContext(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate >= :currentDate) " +
                "and (us.startDate is null or us.startDate <= :currentDate) " +
                "and (us.substitutedUser.active = true or us.substitutedUser.active is null) order by us.substitutedUser.name");
        query.addParameter("userId", userSession.getUser().getId());
        query.addParameter("currentDate", AppBeans.get(TimeSource.class).currentTimestamp());
        ctx.setView("app");
        List<UserSubstitution> usList = AppBeans.get(DataService.class).loadList(ctx);
        for (UserSubstitution substitution : usList) {
            User substitutedUser = substitution.getSubstitutedUser();
            select.addItem(substitutedUser);
            select.setItemCaption(substitutedUser, getSubstitutedUserCaption(substitutedUser));
        }
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
            if (foldersPane != null) {
                foldersPane.init(foldersSplit);
            }
            foldersSplit.replaceComponent(oldFoldersPane, foldersPane);
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

    private void revertToCurrentUser() {
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
                App.getInstance().getWindowManager().showOptionDialog(
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

    private class LogoutBtnClickListener implements Button.ClickListener {

        private static final long serialVersionUID = 4885156177472913997L;

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if (foldersPane != null) {
                foldersPane.savePosition();
            }
            final App app = App.getInstance();
            app.cleanupBackgroundTasks();
            app.getTimers().stopAll();
            app.reinitializeAppearanceProperties();
            app.getWindowManager().checkModificationsAndCloseAll(
                    new Runnable() {
                        @Override
                        public void run() {
                            App.getInstance().getWindowManager().reset();
                            String redirectionUrl = connection.logout();
                            open(new ExternalResource(App.getInstance().getURL() + redirectionUrl));
                        }
                    },
                    null
            );
        }
    }
}
