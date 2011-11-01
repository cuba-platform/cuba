/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.12.2008 12:02:22
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.config.*;
import com.haulmont.cuba.gui.export.ResourceDataProvider;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.UserSettingHelper;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.components.WebEmbeddedApplicationResource;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.toolkit.MenuShortcutAction;
import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import com.haulmont.cuba.web.toolkit.ui.MenuBar;
import com.haulmont.cuba.web.toolkit.ui.RichNotification;
import com.haulmont.cuba.web.ui.WindowBreadCrumbs;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.service.FileTypeResolver;
import com.vaadin.terminal.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Main application window.
 * <p/>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link com.haulmont.cuba.web.App#createAppWindow()} method
 */
public class AppWindow extends Window implements UserSubstitutionListener {

    private static final long serialVersionUID = 7269808125566032433L;

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
    protected SplitPanel foldersSplit;

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

    protected String messagePack;

    private NativeSelect substUserSelect;

    private JavaScriptHost scriptHost;

    public AppWindow(Connection connection) {
        super();

        globalConfig = ConfigProvider.getConfig(GlobalConfig.class);
        webConfig = ConfigProvider.getConfig(WebConfig.class);

        this.connection = connection;
        setCaption(getAppCaption());

        messagePack = AppConfig.getMessagesPack();

        mode = UserSettingHelper.loadAppWindowMode();

        rootLayout = createLayout();
        initLayout();
        setContent(rootLayout);
        postInitLayout();
        initStartupLayout();

        initStaticComponents();

        updateClientSystemMessages();
    }

    private void updateClientSystemMessages() {
        Map<String, String> localeMessages = new HashMap<String, String>();
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
        addComponent(scriptHost);
    }

    /**
     * Current mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Creates root and enclosed layouts.
     * <br>Can be overridden in descendant to create an app-specific root layout
     */
    protected VerticalLayout createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setSizeFull();

        titleLayout = createTitleLayout();
        layout.addComponent(titleLayout);

        menuBarLayout = createMenuBarLayout();

        layout.addComponent(menuBarLayout);

        emptyLayout = new HorizontalLayout();
        emptyLayout.setMargin(false);
        emptyLayout.setSpacing(false);
        emptyLayout.setSizeFull();

        layout.addComponent(emptyLayout);

        middleLayout = new HorizontalLayout();
        middleLayout.addStyleName("work-area");
        middleLayout.setSizeFull();

        foldersPane = createFoldersPane();

        if (foldersPane != null) {
            foldersSplit = new WebSplitPanel();
            foldersSplit.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
            foldersSplit.setSplitPosition(0, UNITS_PIXELS);
            foldersSplit.setLocked(true);

            foldersSplit.addComponent(foldersPane);
            
            middleLayout.addComponent(foldersSplit);
            middleLayout.setExpandRatio(foldersSplit, 1);

            foldersPane.init(foldersSplit);
        }

        layout.addComponent(middleLayout);
        layout.setExpandRatio(middleLayout, 1);

        return layout;
    }

    /**
     * Creates folders pane.
     * <br>Can be overridden in descendant to create an app-specific folders pane.
     * <br>If this method returns null, no folders functionality is available for application.
     */
    @Nullable
    protected FoldersPane createFoldersPane() {
        return new FoldersPane(menuBar, this);
    }

    /**
     * Can be overridden in descendant to create an app-specific caption
     */
    protected String getAppCaption() {
        return MessageProvider.getMessage(getMessagesPack(), "application.caption");
    }

    /**
     * Enclosed Tabsheet
     *
     * @return the tabsheet in TABBED mode, null in SINGLE mode
     */
    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public void setTabSheet(TabSheet tabSheet) {
        this.tabSheet = tabSheet;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * See {@link #rootLayout}
     */
    public VerticalLayout getRootLayout() {
        return rootLayout;
    }

    /**
     * See {@link #titleLayout}
     */
    public Layout getTitleLayout() {
        return titleLayout;
    }

    /**
     * See {@link #menuBarLayout}
     */
    public HorizontalLayout getMenuBarLayout() {
        return menuBarLayout;
    }

    /**
     * See {@link #emptyLayout}
     */
    public HorizontalLayout getEmptyLayout() {
        return emptyLayout;
    }

    /**
     * See {@link #mainLayout}
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
     * @return JavaScriptHost
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
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
    }

    /**
     * Can be overridden in descendant to init an app-specific layout
     */
    protected void postInitLayout() {
        String themeName = AppContext.getProperty(AppConfig.THEME_NAME_PROP);
        if (themeName == null) themeName = App.THEME_NAME;
        themeName = UserSettingHelper.loadAppWindowTheme() == null ? themeName : UserSettingHelper.loadAppWindowTheme();
        setTheme(themeName);
    }

    /**
     * Can be overridden in descendant to create an app-specific menu bar layout
     */
    protected HorizontalLayout createMenuBarLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setStyleName("menubar");
        layout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        layout.setHeight(28, Sizeable.UNITS_PIXELS);
        layout.setMargin(false, false, false, false);
        layout.setSpacing(true);
        menuBar = createMenuBar();
        layout.addComponent(menuBar);

        if (ConfigProvider.getConfig(FtsConfig.class).getEnabled()) {
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

        return layout;
    }

    protected void openSearchWindow(TextField searchField) {
        String searchTerm = (String) searchField.getValue();
        if (StringUtils.isBlank(searchTerm))
            return;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchTerm", searchTerm);

        WindowInfo windowInfo = AppContext.getBean(WindowConfig.class).getWindowInfo("fts$Search");
        App.getInstance().getWindowManager().openWindow(
                windowInfo,
                WindowManager.OpenType.NEW_TAB,
                params
        );
    }

    /**
     * Can be overridden in descendant to create an app-specific menu bar
     */
    protected com.haulmont.cuba.web.toolkit.ui.MenuBar createMenuBar() {
        menuBar = new com.haulmont.cuba.web.toolkit.ui.MenuBar();
        if (globalConfig.getTestMode()) {
            App.getInstance().getWindowManager().setDebugId(menuBar, "appMenu");
        }

        final UserSession session = connection.getSession();
        final MenuConfig menuConfig = AppContext.getBean(MenuConfig.class);
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
        for (MenuBar.MenuItem item : new ArrayList<MenuBar.MenuItem>(menuBar.getItems())) {
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
                List<MenuBar.MenuItem> children = new ArrayList<MenuBar.MenuItem>(item.getChildren());
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
        return MessageProvider.getMessage(getMessagesPack(), "logoLabel");
    }

    /**
     * Can be overridden in descendant to create an app-specific title layout
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
        }

        Label logoLabel = new Label(getLogoLabelCaption());
        logoLabel.setStyleName("appname");

        titleLayout.addComponent(logoLabel);
        titleLayout.setExpandRatio(logoLabel, 1);
        titleLayout.setComponentAlignment(logoLabel, Alignment.MIDDLE_LEFT);

        Label userLabel = new Label(MessageProvider.getMessage(getMessagesPack(), "loggedInLabel"));
        userLabel.setStyleName("select-label");
        userLabel.setSizeUndefined();

        titleLayout.addComponent(userLabel);
        titleLayout.setComponentAlignment(userLabel, Alignment.MIDDLE_RIGHT);

        substUserSelect = new NativeSelect();
        substUserSelect.setNullSelectionAllowed(false);
        substUserSelect.setImmediate(true);
        substUserSelect.setStyleName("select-label");

        fillSubstitutedUsers(substUserSelect);
        if (substUserSelect.getItemIds().size() > 1) {
            UserSession us = App.getInstance().getConnection().getSession();
            substUserSelect.select(us.getSubstitutedUser() == null ? us.getUser() : us.getSubstitutedUser());
            substUserSelect.addListener(new SubstitutedUserChangeListener(substUserSelect));

            titleLayout.addComponent(substUserSelect);
            titleLayout.setComponentAlignment(substUserSelect, Alignment.MIDDLE_RIGHT);
        } else {
            Label userNameLabel = new Label(getSubstitutedUserCaption((User) substUserSelect.getItemIds().iterator().next()));
            userNameLabel.setStyleName("select-label");
            userNameLabel.setSizeUndefined();
            titleLayout.addComponent(userNameLabel);
            titleLayout.setComponentAlignment(userNameLabel, Alignment.MIDDLE_RIGHT);
        }

        Button logoutBtn = new Button(
                MessageProvider.getMessage(getMessagesPack(), "logoutBtn"),
                new LogoutBtnClickListener()
        );
        logoutBtn.setStyleName("white-border");
        logoutBtn.setIcon(new ThemeResource("images/exit.gif"));
        //logoutBtn.setIcon(new ThemeResource("images/logout.png"));
        App.getInstance().getWindowManager()
                .setDebugId(logoutBtn, "logoutBtn");

        titleLayout.addComponent(logoutBtn);
        titleLayout.setComponentAlignment(logoutBtn, Alignment.MIDDLE_RIGHT);

        Button newWindowBtn = new Button(MessageProvider.getMessage(getMessagesPack(), "newWindowBtn"),
                new Button.ClickListener() {
                    private static final long serialVersionUID = -2017737447316558248L;

                    public void buttonClick(Button.ClickEvent event) {
                        String name = App.generateWebWindowName();
                        open(new ExternalResource(App.getInstance().getURL() + name), "_new");
                    }
                }
        );
        newWindowBtn.setStyleName("white-border");
        newWindowBtn.setIcon(new ThemeResource("images/clean.gif"));

        titleLayout.addComponent(newWindowBtn);
        titleLayout.setComponentAlignment(newWindowBtn, Alignment.MIDDLE_RIGHT);

        return titleLayout;
    }

    @Nullable
    protected Embedded getLogoImage() {
        String logoImagePath = webConfig.getAppLogoImagePath();
        if (logoImagePath == null)
            return null;

        ResourceDataProvider dataProvider = new ResourceDataProvider(logoImagePath);
        InputStream stream = dataProvider.provide();
        if (stream != null) {
            IOUtils.closeQuietly(stream);
            WebEmbeddedApplicationResource resource = new WebEmbeddedApplicationResource(
                    dataProvider,
                    "logoImage",
                    FileTypeResolver.getMIMEType(logoImagePath),
                    App.getInstance()
            );
            return new Embedded(null, resource);
        }
        return null;
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
        final WindowConfig windowConfig = AppContext.getBean(WindowConfig.class);
        try {
            windowInfo = windowConfig.getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            return null;
        }
        final MenuCommand command = new MenuCommand(App.getInstance().getWindowManager(), item, windowInfo);
        return new com.vaadin.ui.MenuBar.Command() {
            public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                command.execute();
            }
        };
    }

    protected void fillSubstitutedUsers(AbstractSelect select) {
        UserSession userSession = App.getInstance().getConnection().getSession();

        select.addItem(userSession.getUser());
        select.setItemCaption(userSession.getUser(), getSubstitutedUserCaption(userSession.getUser()));

        LoadContext ctx = new LoadContext(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate > :currentDate) " +
                "and (us.startDate is null or us.startDate < :currentDate) " +
                "and (us.substitutedUser.active = true or us.substitutedUser.active is null) order by us.substitutedUser.name");
        query.addParameter("userId", userSession.getUser().getId());
        query.addParameter("currentDate", TimeProvider.currentTimestamp());
        ctx.setView("app");
        List<UserSubstitution> usList = ServiceLocator.getDataService().loadList(ctx);
        for (UserSubstitution substitution : usList) {
            User substitutedUser = substitution.getSubstitutedUser();
            select.addItem(substitutedUser);
            select.setItemCaption(substitutedUser, getSubstitutedUserCaption(substitutedUser));
        }
    }

    protected String getSubstitutedUserCaption(User user) {
        return InstanceUtils.getInstanceName(user);
    }

    public void userSubstituted(Connection connection) {
        menuBarLayout.replaceComponent(menuBar, createMenuBar());
        if (foldersPane != null) {
            foldersPane.savePosition();
            FoldersPane oldFoldersPane = foldersPane;
            foldersPane = createFoldersPane();
            if (foldersPane != null) {
                foldersPane.init(foldersSplit);
            }
            foldersSplit.replaceComponent(oldFoldersPane, foldersPane);
        }
    }

    protected String getMessagesPack() {
        return AppConfig.getMessagesPack();
    }

    public void showRichNotification(RichNotification notification) {
        if (richNotifications == null) {
            richNotifications = new LinkedList<RichNotification>();
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
            for (final Iterator<RichNotification> it = richNotifications.iterator(); it
                    .hasNext();) {
                final RichNotification n = it.next();

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
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        final Object target = variables.get("notificationHidden");
        if (target != null) {
            com.vaadin.ui.Component component = (com.vaadin.ui.Component) target;
            component.setParent(null);
        }
    }

    private void revertToCurrentUser() {
        UserSession us = App.getInstance().getConnection().getSession();
        substUserSelect.select(us.getCurrentOrSubstitutedUser());
    }

    private class ChangeSubstUserAction extends AbstractAction {
        private AbstractSelect substUserSelect;

        protected ChangeSubstUserAction(AbstractSelect substUserSelect) {
            super("changeSubstUserAction");
            this.substUserSelect = substUserSelect;
        }

        @Override
        public String getIcon() {
            return "icons/ok.png";
        }

        public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
            final App app = App.getInstance();
            app.getWindowManager().checkModificationsAndCloseAll(
                    new Runnable() {
                        public void run() {
                            app.getWindowManager().closeAll();
                            User user = (User) substUserSelect.getValue();
                            try {
                                app.getConnection().substituteUser(user);
                            } catch (javax.persistence.NoResultException e) {
                                showNotification(
                                        MessageProvider.formatMessage(getMessagesPack(), "userDeleteMsg", user.getName()),
                                        Window.Notification.TYPE_WARNING_MESSAGE
                                );
                                revertToCurrentUser();
                            }
                        }
                    },
                    new Runnable() {
                        public void run() {
                            revertToCurrentUser();
                        }
                    }
            );
        }
    }

    private class DoNotChangeSubstUserAction extends AbstractAction {

        protected DoNotChangeSubstUserAction() {
            super("doNotChangeSubstUserAction");
        }

        @Override
        public String getIcon() {
            return "icons/cancel.png";
        }

        public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
            revertToCurrentUser();
        }
    }

    protected class SubstitutedUserChangeListener implements Property.ValueChangeListener {

        private final AbstractSelect substUserSelect;

        public SubstitutedUserChangeListener(AbstractSelect substUserSelect) {
            this.substUserSelect = substUserSelect;
        }

        public void valueChange(Property.ValueChangeEvent event) {
            User newUser = (User) event.getProperty().getValue();
            UserSession userSession = App.getInstance().getConnection().getSession();
            User oldUser = userSession.getSubstitutedUser() == null ? userSession.getUser() : userSession.getSubstitutedUser();

            if (!oldUser.equals(newUser)) {
                String name = StringUtils.isBlank(newUser.getName()) ? newUser.getLogin() : newUser.getName();
                App.getInstance().getWindowManager().showOptionDialog(
                        MessageProvider.getMessage(getMessagesPack(), "substUserSelectDialog.title"),
                        MessageProvider.formatMessage(getMessagesPack(), "substUserSelectDialog.msg", name),
                        IFrame.MessageType.WARNING,
                        new Action[]{new ChangeSubstUserAction(substUserSelect), new DoNotChangeSubstUserAction()}
                );
            }

        }
    }

    @SuppressWarnings("serial")
    public static class AppTabSheet extends ActionsTabSheet implements com.vaadin.event.Action.Handler {

        private Map<Component, TabCloseHandler> closeHandlers = null;

        private com.vaadin.event.Action closeAllTabs = new com.vaadin.event.Action(
                MessageProvider.getMessage(getMessagesPack(), "actions.closeAllTabs")
        );

        private com.vaadin.event.Action closeOtherTabs = new com.vaadin.event.Action(
                MessageProvider.getMessage(getMessagesPack(), "actions.closeOtherTabs")
        );

        private com.vaadin.event.Action closeCurrentTab = new com.vaadin.event.Action(
                MessageProvider.getMessage(getMessagesPack(), "actions.closeCurrentTab")
        );

        private com.vaadin.event.Action showInfo = new com.vaadin.event.Action(
                MessageProvider.getMessage(getMessagesPack(), "actions.showInfo")
        );

        public AppTabSheet() {
            setCloseHandler(new CloseHandler() {
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
                closeHandlers = new LinkedHashMap<Component, TabCloseHandler>();
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

        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            if (target != null) {
                if (UserSessionProvider.getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION) &&
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

        protected String getMessagesPack() {
            return AppConfig.getMessagesPack();
        }

        public void closeAllTabs() {
            Set<Component> tabs = new HashSet<Component>(this.tabs.keySet());
            for (final Component tab : tabs) {
                closeTab(tab);
            }
        }

        public void closeOtherTabs(Component currentTab) {
            Set<Component> tabs = new HashSet<Component>(this.tabs.keySet());
            for (final Component tab : tabs) {
                if (tab.equals(currentTab)) continue;
                closeTab(tab);
            }
        }

        public void showInfo(Object target) {
            com.haulmont.cuba.gui.components.Window.Editor editor = findEditor((Layout) target);
            Entity entity = editor.getItem();
            MetaClass metaClass = MetadataProvider.getSession().getClass(entity.getClass());
            new ShowInfoAction().showInfo(entity, metaClass, editor);
        }

        public interface TabCloseHandler extends Serializable {
            void onClose(TabSheet tabSheet, Component tabContent);
        }
    }

    private class LogoutBtnClickListener implements Button.ClickListener {

        private static final long serialVersionUID = 4885156177472913997L;

        public void buttonClick(Button.ClickEvent event) {
            if (foldersPane != null) {
                foldersPane.savePosition();
            }
            App.getInstance().getWindowManager().checkModificationsAndCloseAll(
                    new Runnable() {
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
