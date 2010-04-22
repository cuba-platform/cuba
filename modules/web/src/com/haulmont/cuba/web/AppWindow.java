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
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.NoSuchScreenException;
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
import com.haulmont.cuba.web.log.LogWindow;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main application window.
 * <p/>
 * Specific application should inherit from this class and create appropriate
 * instance in {@link com.haulmont.cuba.web.App#createAppWindow()} method
 */
public class AppWindow extends Window implements UserSubstitutionListener {

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

    protected MenuBar menuBar;
    protected TabSheet tabSheet;
    protected SplitPanel foldersSplit;

    protected Mode mode;

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
        middleLayout.setSizeFull();

        mainLayout = new VerticalLayout();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setSizeFull();

        if (Mode.TABBED.equals(mode)) {
            tabSheet = new TabSheet();
            tabSheet.setSizeFull();

            mainLayout.addComponent(tabSheet);
            mainLayout.setExpandRatio(tabSheet, 1);
        }

        foldersPane = createFoldersPane();

        if (foldersPane != null) {
            foldersSplit = new WebSplitPanel();
            foldersSplit.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
            foldersSplit.setSplitPosition(0, UNITS_PIXELS);
            foldersSplit.setLocked(true);

            foldersSplit.addComponent(foldersPane);
            foldersSplit.addComponent(mainLayout);

            middleLayout.addComponent(foldersSplit);
            middleLayout.setExpandRatio(foldersSplit, 1);

            foldersPane.init(foldersSplit);
        } else {
            middleLayout.addComponent(mainLayout);
            middleLayout.setExpandRatio(mainLayout, 1);
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

    /**
     * Can be overridden in descendant to init an app-specific layout
     */
    protected void postInitLayout() {
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

        return layout;
    }

    /**
     * Can be overridden in descendant to create an app-specific menu bar
     */
    protected MenuBar createMenuBar() {
        menuBar = new MenuBar();
        if (ConfigProvider.getConfig(GlobalConfig.class).getTestMode()) {
            App.getInstance().getWindowManager().setDebugId(menuBar, "appMenu");
        }

        final MenuConfig menuConfig = AppConfig.getInstance().getMenuConfig();
        List<MenuItem> rootItems = menuConfig.getRootItems();
        for (MenuItem menuItem : rootItems) {
            createMenuBarItem(menuBar, menuItem);
        }

        return menuBar;
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

        HorizontalLayout loginLayout = new HorizontalLayout();
        loginLayout.setMargin(false, true, false, false);
        loginLayout.setSpacing(true);
        Label userLabel = new Label(MessageProvider.getMessage(getMessagesPack(), "loggedInLabel"));
        userLabel.setStyleName("select-label");

        loginLayout.addComponent(userLabel);

        final NativeSelect substUserSelect = new NativeSelect();
        substUserSelect.setNullSelectionAllowed(false);
        substUserSelect.setImmediate(true);
        substUserSelect.setStyleName("select-label");

        fillSubstitutedUsers(substUserSelect);
        UserSession us = App.getInstance().getConnection().getSession();
        substUserSelect.select(us.getSubstitutedUser() == null ? us.getUser() : us.getSubstitutedUser());
        substUserSelect.addListener(new SubstitutedUserChangeListener(substUserSelect));

        loginLayout.addComponent(substUserSelect);


        Button logoutBtn = new Button(MessageProvider.getMessage(getMessagesPack(), "logoutBtn"),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 4885156177472913997L;

                    public void buttonClick(Button.ClickEvent event) {
                        if (foldersPane != null) {
                            foldersPane.savePosition();
                        }
                        connection.logout();
                        String url = ActiveDirectoryHelper.useActiveDirectory() ? "login" : "";
                        open(new ExternalResource(App.getInstance().getURL() + url));
                    }
                }
        );
        logoutBtn.setStyleName("white-border");
        logoutBtn.setIcon(new ThemeResource("images/exit.gif"));

        Button viewLogBtn = new Button(MessageProvider.getMessage(getMessagesPack(), "viewLogBtn"),
                new Button.ClickListener() {
                    private static final long serialVersionUID = -2017737447316558248L;

                    public void buttonClick(Button.ClickEvent event) {
                        LogWindow logWindow = new LogWindow();
                        addWindow(logWindow);
                    }
                }
        );
        viewLogBtn.setStyleName("white-border");
        viewLogBtn.setIcon(new ThemeResource("images/show-log.gif"));

        Button newWindowBtn = new Button(MessageProvider.getMessage(getMessagesPack(), "newWindowBtn"),
                new Button.ClickListener() {
                    private static final long serialVersionUID = -2017737447316558248L;

                    public void buttonClick(Button.ClickEvent event) {
                        String name = GlobalUtils.generateWebWindowName();
                        open(new ExternalResource(App.getInstance().getURL() + name), "_new");
                    }
                }
        );
        newWindowBtn.setStyleName("white-border");
        newWindowBtn.setIcon(new ThemeResource("images/clean.gif"));

        titleLayout.addComponent(loginLayout);
        titleLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_RIGHT);

        titleLayout.addComponent(logoutBtn);
        titleLayout.setComponentAlignment(logoutBtn, Alignment.MIDDLE_RIGHT);

        titleLayout.addComponent(viewLogBtn);
        titleLayout.setComponentAlignment(viewLogBtn, Alignment.MIDDLE_RIGHT);

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

    private void createMenuBarItem(MenuBar menuBar, MenuItem item) {
        if (!connection.isConnected()) return;

        final UserSession session = connection.getSession();
        if (item.isPermitted(session)) {
            MenuBar.MenuItem menuItem = menuBar.addItem(MenuConfig.getMenuItemCaption(item.getId()), createMenuBarCommand(item));

            createSubMenu(menuItem, item, session);
            if (!menuItem.hasChildren() && menuItem.getCommand() == null) {
                menuBar.removeItem(menuItem);
            }
        }
    }

    private void createSubMenu(MenuBar.MenuItem vItem, MenuItem item, UserSession session) {
        if (item.isPermitted(session) && !item.getChildren().isEmpty()) {
            for (MenuItem child : item.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    if (child.isPermitted(session)) {
                        vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), createMenuBarCommand(child));
                    }
                } else {
                    MenuBar.MenuItem menuItem = vItem.addItem(MenuConfig.getMenuItemCaption(child.getId()), null);
                    createSubMenu(menuItem, child, session);
                }
            }
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
                "and (us.substitutedUser.active = true or us.substitutedUser.active is null)");
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
            App app = App.getInstance();
            app.getWindowManager().closeAll();
            app.getConnection().substituteUser((User) substUserSelect.getValue());
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
            UserSession us = App.getInstance().getConnection().getSession();
            substUserSelect.select(us.getSubstitutedUser() == null ? us.getUser() : us.getSubstitutedUser());
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
                if (info == null)
                    params.put(element.attributeValue("name"), value);
                else
                    params.put(element.attributeValue("name"), loadEntityInstance(info));
            }
            params.put("caption", caption);

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
                        WindowManager.OpenType.NEW_TAB,
                        params
                );
            } else {
                App.getInstance().getWindowManager().openWindow(
                        windowInfo,
                        WindowManager.OpenType.NEW_TAB,
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
}
