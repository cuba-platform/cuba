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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
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
import com.haulmont.cuba.web.log.LogWindow;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.dom4j.Element;

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
        setTheme("saneco");
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

        layout.addComponent(mainLayout);
        layout.setExpandRatio(mainLayout, 1);

        return layout;
    }

    /**
     * Can be overridden in descendant to create an app-specific caption
     */
    protected String getAppCaption() {
        return MessageProvider.getMessage(getClass(), "application.caption");
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
        layout.setMargin(false);
        layout.setSpacing(false);
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

    /**
     * Can be overridden in descendant to create an app-specific title layout
     */
    protected Layout createTitleLayout() {
        HorizontalLayout titleLayout = new HorizontalLayout();

        titleLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        titleLayout.setHeight(20, Sizeable.UNITS_PIXELS);

        titleLayout.setMargin(false);
        titleLayout.setSpacing(false);

        Label logoLabel = new Label(MessageProvider.getMessage(getClass(), "logoLabel"));
        logoLabel.setStyleName("logo");//new style for logo

        HorizontalLayout loginLayout = new HorizontalLayout();

        Label userLabel = new Label(MessageProvider.getMessage(getClass(), "loggedInLabel"));
        userLabel.setStyleName("logo");

        loginLayout.addComponent(userLabel);

        final NativeSelect substUserSelect = new NativeSelect();
        substUserSelect.setNullSelectionAllowed(false);
        substUserSelect.setImmediate(true);
        substUserSelect.setStyleName("logo");

        fillSubstitutedUsers(substUserSelect);
        substUserSelect.select(App.getInstance().getConnection().getSession().getUser());
        substUserSelect.addListener(new SubstitutedUserChangeListener(substUserSelect));

        loginLayout.addComponent(substUserSelect);

        Button logoutBtn = new NativeButton(MessageProvider.getMessage(getClass(), "logoutBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        connection.logout();
                        String url = ActiveDirectoryHelper.useActiveDirectory() ? "login" : "";
                        open(new ExternalResource(App.getInstance().getURL() + url));
                    }
                }
        );
        logoutBtn.setStyleName("title");

        Button viewLogBtn = new NativeButton(MessageProvider.getMessage(getClass(), "viewLogBtn"),
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        LogWindow logWindow = new LogWindow();
                        addWindow(logWindow);
                    }
                }
        );
        viewLogBtn.setStyleName("title");

        titleLayout.addComponent(logoLabel);
        titleLayout.setExpandRatio(logoLabel, 1);
        titleLayout.setComponentAlignment(logoLabel, Alignment.MIDDLE_LEFT);

        titleLayout.addComponent(loginLayout);

        titleLayout.addComponent(logoutBtn);
        titleLayout.addComponent(viewLogBtn);

        return titleLayout;
    }

    private void createMenuBarItem(MenuBar menuBar, MenuItem item) {
        if (!connection.isConnected()) return;

        final UserSession session = connection.getSession();
        if (item.isPermitted(session)) {
            MenuBar.MenuItem menuItem = menuBar.addItem(MenuConfig.getMenuItemCaption(item.getId()), createMenuBarCommand(item));

            createSubMenu(menuItem, item, session);
//            if (!menuItem.hasChildren()) {
//                menuBar.removeItem(menuItem);
//            }
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

        return new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String caption = MenuConfig.getMenuItemCaption(item.getId());

                Map<String, Object> params = new HashMap<String, Object>();
                Element descriptor = item.getDescriptor();
                for (Element element : Dom4j.elements(descriptor, "param")) {
                    params.put(element.attributeValue("name"), element.attributeValue("value"));
                }
                params.put("caption", caption);

                final String id = windowInfo.getId();
                if (id.endsWith(".create") || id.endsWith(".edit")) {
                    final String[] strings = id.split("[.]");
                    if (strings.length != 2) throw new UnsupportedOperationException();

                    final String metaClassName = strings[0];
                    final MetaClass metaClass = MetadataProvider.getSession().getClass(metaClassName);
                    if (metaClass == null)
                        throw new IllegalStateException(String.format("Can't find metaClass %s", metaClassName));

                    Entity newItem;
                    try {
                        newItem = metaClass.createInstance();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }

                    App.getInstance().getWindowManager().openEditor(
                            windowInfo,
                            newItem,
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
        };
    }

    protected void fillSubstitutedUsers(AbstractSelect select) {
        UserSession userSession = App.getInstance().getConnection().getSession();

        select.addItem(userSession.getUser());
        select.setItemCaption(userSession.getUser(), getSubstitutedUserCaption(userSession.getUser()));

        LoadContext ctx = new LoadContext(UserSubstitution.class);
        LoadContext.Query query = ctx.setQueryString("select us from sec$UserSubstitution us " +
                "where us.user.id = :userId and (us.endDate is null or us.endDate > :currentDate)");
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
            substUserSelect.select(App.getInstance().getConnection().getSession().getUser());
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
                App.getInstance().getWindowManager().showOptionDialog(
                        MessageProvider.getMessage(AppWindow.class, "substUserSelectDialog.title"),
                        MessageProvider.formatMessage(AppWindow.class, "substUserSelectDialog.msg", newUser.toString()),
                        IFrame.MessageType.WARNING,
                        new Action[]{new ChangeSubstUserAction(substUserSelect), new DoNotChangeSubstUserAction(substUserSelect)}
                );
            }

        }
    }
}
