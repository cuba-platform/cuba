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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
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
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.gui.config.MenuItem;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSubstitution;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.UserSettingHelper;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.toolkit.MenuShortcutAction;
import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.haulmont.cuba.web.toolkit.ui.MenuBar;
import com.haulmont.cuba.web.toolkit.ui.RichNotification;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.terminal.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.io.File;
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

    public AppWindow(Connection connection) {
        super();

        this.connection = connection;
        setCaption(getAppCaption());

        messagePack = AppConfig.getInstance().getMessagesPack();

        mode = UserSettingHelper.loadAppWindowMode();

        rootLayout = createLayout();
        initLayout();
        setContent(rootLayout);
        postInitLayout();
        initStartupLayout();
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

        WindowInfo windowInfo = AppConfig.getInstance().getWindowConfig().getWindowInfo("fts$Search");
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
        if (ConfigProvider.getConfig(GlobalConfig.class).getTestMode()) {
            App.getInstance().getWindowManager().setDebugId(menuBar, "appMenu");
        }

        final UserSession session = connection.getSession();
        final MenuConfig menuConfig = AppConfig.getInstance().getMenuConfig();
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

    protected Embedded getLogoImage() {
        String confDirPath = AppContext.getProperty("cuba.confDir");
        String logoImagePath = AppContext.getProperty("cuba.appLogoImagePath");
        if (confDirPath == null || logoImagePath == null)
            return null;
        File file = new File(confDirPath + logoImagePath);
        if (file.exists())
            return new Embedded(null, new FileResource(file, App.getInstance()));
        return null;
    }

    private void createShortcut(MenuBar.MenuItem menuItem, MenuItem item) {
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
            createShortcut(menuItem, item);
            createSubMenu(menuItem, item, session);
            createDebugIds(menuItem, item);
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
                        createShortcut(menuItem, child);
                        createDebugIds(menuItem, child);
                    }
                } else {
                    if (child.isPermitted(session)) {
                        MenuBar.MenuItem menuItem = vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), null);
                        createShortcut(menuItem, child);
                        createSubMenu(menuItem, child, session);
                        createDebugIds(menuItem, child);
                        if (isMenuItemEmpty(menuItem)) {
                            vItem.removeChild(menuItem);
                        }
                    }
                }
            }
        }
    }

    private void createDebugIds(MenuBar.MenuItem menuItem, MenuItem conf) {
        if (menuBar.getDebugId() != null && !conf.isSeparator()) {
            menuBar.setDebugId(menuItem, menuBar.getDebugId() + ":" + conf.getId());
        }
    }

    private MenuBar.Command createMenuBarCommand(final MenuItem item) {
        final WindowInfo windowInfo;
        final com.haulmont.cuba.gui.config.WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        try {
            windowInfo = windowConfig.getWindowInfo(item.getId());
        } catch (NoSuchScreenException e) {
            return null;
        }
        return new MainMenuCommand(item, windowInfo);
    }

    protected void fillSubstitutedUsers(AbstractSelect select) {
        UserSession userSession = App.getInstance().getConnection().getSession();

        select.addItem(userSession.getUser());
        select.setItemCaption(userSession.getUser(), getSubstitutedUserCaption(userSession.getUser()));

        LoadContext ctx = new LoadContext(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate > :currentDate) " +
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
        return InstanceUtils.getInstanceName((Instance) user);
    }

    public void userSubstituted(Connection connection) {
        menuBarLayout.replaceComponent(menuBar, createMenuBar());
        if (foldersPane != null) {
            foldersPane.savePosition();
            FoldersPane oldFoldersPane = foldersPane;
            foldersPane = createFoldersPane();
            foldersPane.init(foldersSplit);
            foldersSplit.replaceComponent(oldFoldersPane, foldersPane);
        }
    }

    protected String getMessagesPack() {
        return AppConfig.getInstance().getMessagesPack();
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
        private AbstractSelect substUserSelect;

        protected DoNotChangeSubstUserAction(AbstractSelect substUserSelect) {
            super("doNotChangeSubstUserAction");
            this.substUserSelect = substUserSelect;
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
                        new Action[]{new ChangeSubstUserAction(substUserSelect), new DoNotChangeSubstUserAction(substUserSelect)}
                );
            }

        }
    }

    public static class MainMenuCommand implements MenuBar.Command {

        private final MenuItem item;
        private final WindowInfo windowInfo;

        public MainMenuCommand(MenuItem item, WindowInfo windowInfo) {
            this.item = item;
            this.windowInfo = windowInfo;
        }

        public void menuSelected(MenuBar.MenuItem selectedItem) {
            String caption = MenuConfig.getMenuItemCaption(item.getId());

            Map<String, Object> params = new HashMap<String, Object>();
            Element descriptor = item.getDescriptor();
            for (Element element : Dom4j.elements(descriptor, "param")) {
                String value = element.attributeValue("value");
                EntityLoadInfo info = EntityLoadInfo.parse(value);
                if (info == null) {
                    if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                        Boolean booleanValue = Boolean.valueOf(value);
                        params.put(element.attributeValue("name"), booleanValue);
                    } else {
                        params.put(element.attributeValue("name"), value);
                    }
                } else
                    params.put(element.attributeValue("name"), loadEntityInstance(info));
            }
            params.put("caption", caption);

            WindowManager.OpenType openType = WindowManager.OpenType.NEW_TAB;
            String openTypeStr = descriptor.attributeValue("openType");
            if (openTypeStr != null) {
                openType = WindowManager.OpenType.valueOf(openTypeStr);
            }

            if (openType == WindowManager.OpenType.DIALOG) {
                String resizable = descriptor.attributeValue("resizable");
                if (!StringUtils.isEmpty(resizable)) {
                    App.getInstance().getWindowManager().getDialogParams()
                            .setResizable(BooleanUtils.toBoolean(resizable));
                }
            }

            final String id = windowInfo.getId();
            if (id.endsWith(".create") || id.endsWith(".edit")) {
                Entity entityItem;
                if (params.containsKey("item")) {
                    entityItem = (Entity) params.get("item");
                } else {
                    final String[] strings = id.split("[.]");
                    String metaClassName;
                    if (strings.length == 2)
                        metaClassName = strings[0];
                    else if (strings.length == 3)
                        metaClassName = strings[1];
                    else
                        throw new UnsupportedOperationException();

                    final MetaClass metaClass = MetadataProvider.getSession().getClass(metaClassName);
                    if (metaClass == null)
                        throw new IllegalStateException(String.format("Can't find metaClass %s", metaClassName));

                    try {
                        entityItem = metaClass.createInstance();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                App.getInstance().getWindowManager().openEditor(
                        windowInfo,
                        entityItem,
                        openType,
                        params
                );
            } else {
                App.getInstance().getWindowManager().openWindow(
                        windowInfo,
                        openType,
                        params
                );
            }
        }

        private Entity loadEntityInstance(EntityLoadInfo info) {
            DataService ds = ServiceLocator.getDataService();
            LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
            if (info.getViewName() != null)
                ctx.setView(info.getViewName());
            Entity entity = ds.load(ctx);
            return entity;
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

        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            return new com.vaadin.event.Action[] {
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
            }
        }

        protected String getMessagesPack() {
            return AppConfig.getInstance().getMessagesPack();
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
                            String redirectionUrl = connection.logout();
                            open(new ExternalResource(App.getInstance().getURL() + redirectionUrl));
                        }
                    },
                    null
            );
        }
    }
}
