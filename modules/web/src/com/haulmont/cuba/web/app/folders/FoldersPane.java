/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 16:14:55
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.WindowParams;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.UserSettingHelper;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.*;

import com.haulmont.cuba.web.toolkit.Timer;

@SuppressWarnings("serial")
public class FoldersPane extends VerticalLayout {

    private static Log log = LogFactory.getLog(FoldersPane.class);

    protected String messagesPack;

    protected boolean visible;

    protected com.haulmont.cuba.web.toolkit.ui.Tree appFoldersTree;
    protected com.haulmont.cuba.web.toolkit.ui.Tree searchFoldersTree;

    protected MenuBar menuBar;
    protected MenuBar.MenuItem menuItem;

    protected Label appFoldersLabel;
    protected Label searchFoldersLabel;

    protected Object appFoldersRoot;
    protected Object searchFoldersRoot;
    private Timer timer;

    protected static final int DEFAULT_VERT_SPLIT_POS = 400;
    protected int horizontalSplitPos;
    protected int verticalSplitPos;

    protected AppWindow parentAppWindow;
    protected WebSplitPanel vertSplit;
    protected WebSplitPanel horSplit;

    public FoldersPane(MenuBar menuBar, AppWindow appWindow) {
        this.menuBar = menuBar;
        messagesPack = AppConfig.getMessagesPack();
        parentAppWindow = appWindow;

        setHeight(100, Sizeable.UNITS_PERCENTAGE);
        setStyleName("folderspane");
    }

    public void init(Component parent) {
        if (parent instanceof WebSplitPanel) {
            horSplit = (WebSplitPanel) parent;
        }

        boolean visible;
        UserSettingHelper.FoldersState state = UserSettingHelper.loadFoldersState();
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        if (state == null) {
            WebConfig config = ConfigProvider.getConfig(WebConfig.class);
            visible = config.getFoldersPaneVisibleByDefault() || webConfig.getUseLightHeader();
            horizontalSplitPos = config.getFoldersPaneDefaultWidth();
            verticalSplitPos = DEFAULT_VERT_SPLIT_POS;
        } else {
            visible = state.visible;
            horizontalSplitPos = state.horizontalSplit;
            verticalSplitPos = state.verticalSplit;
        }

        showFolders(visible);

        MenuBar.MenuItem firstItem = getFirstMenuItem(menuBar);

        if (!webConfig.getUseLightHeader()) {
            menuItem = menuBar.addItemBefore(getMenuItemCaption(),
                    getMenuItemIcon(),
                    createMenuBarCommand(),
                    firstItem);

            menuBar.setStyleName("folders-pane");
        }
    }

    protected MenuBar.Command createMenuBarCommand() {
        return new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                showFolders(!visible);
                selectedItem.setText(getMenuItemCaption());
                selectedItem.setIcon(getMenuItemIcon());
            }
        };
    }

    private synchronized void showFolders(boolean show) {
        if (show == visible)
            return;

        if (show) {
            if (horSplit != null) {
                horSplit.setSplitPosition(horizontalSplitPos, Sizeable.UNITS_PIXELS);
                horSplit.setLocked(false);
            } else {
                setWidth(horizontalSplitPos, Sizeable.UNITS_PIXELS);
            }

            setSizeFull();
            setMargin(false);
            setSpacing(true);

            Component appFoldersPane = createAppFoldersPane();
            if (appFoldersPane != null) {
                appFoldersPane.setHeight("97%");
                appFoldersPane.setWidth("96%");
                if (isNeedFoldersTitle()) {
                    appFoldersLabel = new Label(MessageProvider.getMessage(messagesPack, "folders.appFoldersRoot"));
                    appFoldersLabel.setStyleName("folderspane-caption");
                } else {
                    appFoldersLabel = null;
                }

                int period = ConfigProvider.getConfig(WebConfig.class).getAppFoldersRefreshPeriodSec() * 1000;
                timer = new Timer(period, true);
                timer.addListener(createAppFolderUpdater());
                App.getInstance().getTimers().add(timer, parentAppWindow);
            }

            Component searchFoldersPane = createSearchFoldersPane();
            if (searchFoldersPane != null) {
                searchFoldersPane.setHeight("97%");
                searchFoldersPane.setWidth("96%");
                if (isNeedFoldersTitle()) {
                    searchFoldersLabel = new Label(MessageProvider.getMessage(messagesPack, "folders.searchFoldersRoot"));
                    searchFoldersLabel.setStyleName("folderspane-caption");
                } else {
                    searchFoldersLabel = null;
                }
            }

            if (appFoldersPane != null && searchFoldersPane != null) {
                vertSplit = new WebSplitPanel();
                vertSplit.setSplitPosition(verticalSplitPos, Sizeable.UNITS_PIXELS);

                VerticalLayout afLayout = new VerticalLayout();
                afLayout.setSpacing(true);
                afLayout.setSizeFull();
                if (appFoldersLabel != null)
                    addFoldersLabel(afLayout, appFoldersLabel);
                afLayout.addComponent(appFoldersPane);
                afLayout.setExpandRatio(appFoldersPane, 1);
                vertSplit.setFirstComponent(afLayout);

                VerticalLayout sfLayout = new VerticalLayout();
                sfLayout.setSpacing(true);
                sfLayout.setSizeFull();
                if (searchFoldersLabel != null)
                    addFoldersLabel(sfLayout, searchFoldersLabel);
                sfLayout.addComponent(searchFoldersPane);
                sfLayout.setExpandRatio(searchFoldersPane, 1);
                vertSplit.setSecondComponent(sfLayout);

                addComponent(vertSplit);
            } else {
                if (appFoldersPane != null) {
                    if (appFoldersLabel != null)
                        addFoldersLabel(this, appFoldersLabel);
                    addComponent(appFoldersPane);
                    setExpandRatio(appFoldersPane, 1);
                }
                if (searchFoldersPane != null) {
                    if (searchFoldersLabel != null)
                        addFoldersLabel(this, searchFoldersLabel);
                    addComponent(searchFoldersPane);
                    setExpandRatio(searchFoldersPane, 1);
                }
            }
            adjustLayout();

            if (getParent() != null)
                getParent().requestRepaint();

            if (appFoldersTree != null) {
                collapseItemInTree(appFoldersTree, "appFoldersCollapse");
            }
            if (searchFoldersTree != null) {
                collapseItemInTree(searchFoldersTree, "searchFoldersCollapse");
            }

        } else {
            if (timer != null)
                timer.stopTimer();

            removeAllComponents();
            setMargin(false);

            savePosition();

            if (horSplit != null) {
                horSplit.setSplitPosition(0, Sizeable.UNITS_PIXELS);
                horSplit.setLocked(true);
            } else {
                setWidth(0, Sizeable.UNITS_PIXELS);
            }

            appFoldersTree = null;
            searchFoldersTree = null;
        }

        visible = show;
    }

    protected void collapseItemInTree(Tree tree, final String foldersCollapse) {
        final UserSettingService uss = ServiceLocator.lookup(UserSettingService.NAME);
        String s = uss.loadSetting(foldersCollapse);
        List<UUID> idFolders = strToIds(s);
        for (AbstractSearchFolder folder : (Collection<AbstractSearchFolder>) tree.getItemIds()) {
            if (idFolders.contains(folder.getId())) {
                tree.collapseItem(folder);
            }
        }
        tree.addListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent event) {
                if (event.getItemId() instanceof AbstractSearchFolder) {
                    UUID uuid = ((AbstractSearchFolder) event.getItemId()).getId();
                    String str = uss.loadSetting(foldersCollapse);
                    uss.saveSetting(foldersCollapse, removeIdInStr(str, uuid));
                }
            }
        });
        tree.addListener(new Tree.CollapseListener() {
            @Override
            public void nodeCollapse(Tree.CollapseEvent event) {
                if (event.getItemId() instanceof AbstractSearchFolder) {
                    UUID uuid = ((AbstractSearchFolder) event.getItemId()).getId();
                    String str = uss.loadSetting(foldersCollapse);
                    uss.saveSetting(foldersCollapse, addIdInStr(str, uuid));
                }
            }
        });
    }

    protected String addIdInStr(String inputStr, UUID uuid) {
        if (inputStr == null)
            inputStr = "";
        String str = uuid != null ? uuid.toString() : "";
        if (!inputStr.contains(str)) {
            if (inputStr.length() == 0)
                return str;
            else
                return inputStr + ":" + str;
        } else
            return inputStr;
    }

    protected String removeIdInStr(String inputStr, UUID uuid) {
        if (inputStr == null) inputStr = "";
        List<UUID> uuids = strToIds(inputStr);
        if (uuid != null)
            uuids.remove(uuid);
        return idsToStr(uuids);
    }

    protected List<UUID> strToIds(String inputStr) {
        if (inputStr == null) inputStr = "";
        String[] args = StringUtils.split(inputStr, ':');
        ArrayList<UUID> uuids = new ArrayList<UUID>();
        for (String str : args) {
            uuids.add(UUID.fromString(str));
        }
        return uuids;
    }

    protected String idsToStr(List<UUID> uuids) {
        if (uuids == null) return "";
        StringBuffer sb = new StringBuffer();
        for (UUID uuid : uuids) {
            sb.append(":").append(uuid.toString());
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    protected Component addFoldersLabel(AbstractLayout layout, Label label) {
        HorizontalLayout l = new HorizontalLayout();
        l.setMargin(false, true, false, true);
        l.addComponent(label);
        layout.addComponent(l);
        return l;
    }

    public void savePosition() {
        if (visible) {
            if (horSplit != null)
                horizontalSplitPos = horSplit.getSplitPosition();
            if (vertSplit != null)
                verticalSplitPos = vertSplit.getSplitPosition();
        }
        UserSettingHelper.saveFoldersState(
                visible,
                horizontalSplitPos,
                verticalSplitPos
        );
    }

    protected Timer.Listener createAppFolderUpdater() {
        return new AppFoldersUpdater();
    }

    public void refreshFolders() {
        if (visible) {
            showFolders(false);
            showFolders(true);
        }
    }

    public void reloadAppFolders() {
        if (appFoldersTree == null)
            return;

        List<AppFolder> folders = new ArrayList(appFoldersTree.getItemIds());
        FoldersService service = ServiceLocator.lookup(FoldersService.NAME);
        List<AppFolder> updateFolders = service.reloadAppFolders(folders);
        for (AppFolder folder : updateFolders) {
            int index = updateFolders.indexOf(folder);
            AppFolder f = folders.get(index);
            if (f != null) {
                f.setItemStyle(folder.getItemStyle());
                f.setQuantity(folder.getQuantity());
            }
            appFoldersTree.setItemCaption(folder, folder.getCaption());
        }
    }

    protected void adjustLayout() {
    }

    protected Component createAppFoldersPane() {
        FoldersService service = ServiceLocator.lookup(FoldersService.NAME);
        List<AppFolder> appFolders = service.loadAppFolders();
        if (appFolders.isEmpty())
            return null;

        appFoldersTree = new com.haulmont.cuba.web.toolkit.ui.Tree();
//        appFoldersTree.setDoubleClickMode(true);
        appFoldersTree.setItemStyleGenerator(new FolderTreeStyleProvider());

        appFoldersRoot = MessageProvider.getMessage(messagesPack, "folders.appFoldersRoot");
        fillTree(appFoldersTree, appFolders, isNeedRootAppFolder() ? appFoldersRoot : null);
        appFoldersTree.addListener(new FolderClickListener());
        appFoldersTree.addActionHandler(new AppFolderActionsHandler());

        for (Object itemId : appFoldersTree.rootItemIds()) {
            appFoldersTree.expandItemsRecursively(itemId);
        }

        return appFoldersTree;
    }

    protected Component createSearchFoldersPane() {
        searchFoldersTree = new com.haulmont.cuba.web.toolkit.ui.Tree();
//        searchFoldersTree.setDoubleClickMode(true);
        searchFoldersTree.setItemStyleGenerator(new FolderTreeStyleProvider());

        FoldersService service = ServiceLocator.lookup(FoldersService.NAME);
        List<SearchFolder> searchFolders = service.loadSearchFolders();
        searchFoldersRoot = MessageProvider.getMessage(messagesPack, "folders.searchFoldersRoot");
        searchFoldersTree.addListener(new FolderClickListener());
        searchFoldersTree.addActionHandler(new SearchFolderActionsHandler());
        if (!searchFolders.isEmpty()) {
            fillTree(searchFoldersTree, searchFolders, isNeedRootSearchFolder() ? searchFoldersRoot : null);
        }

        for (Object itemId : searchFoldersTree.rootItemIds()) {
            searchFoldersTree.expandItemsRecursively(itemId);
        }
        return searchFoldersTree;
    }

    protected void fillTree(Tree tree, List<? extends Folder> folders, Object rootItemId) {
        if (rootItemId != null) {
            tree.addItem(rootItemId);
        }
        for (Folder folder : folders) {
            tree.addItem(folder);
            tree.setItemCaption(folder, folder.getCaption());
            if (ConfigProvider.getConfig(WebConfig.class).getShowFolderIcons()) {
                if (folder instanceof SearchFolder) {
                    if (BooleanUtils.isTrue(((SearchFolder) folder).getIsSet())) {
                        tree.setItemIcon(folder, new ThemeResource("icons/set-small.png"));
                    } else {
                        tree.setItemIcon(folder, new ThemeResource("icons/search-folder-small.png"));
                    }
                } else if (folder instanceof AppFolder) {
                    tree.setItemIcon(folder, new ThemeResource("icons/app-folder-small.png"));
                }
            }
        }
        for (Folder folder : folders) {
            if (folder.getParent() == null) {
                tree.setParent(folder, rootItemId);
            } else {
                if (tree.getItem(folder.getParent()) != null)
                    tree.setParent(folder, folder.getParent());
                else
                    tree.setParent(folder, rootItemId);
            }
        }
        for (Folder folder : folders) {
            if (!tree.hasChildren(folder)) {
                tree.setChildrenAllowed(folder, false);
            }
        }
    }

    protected String getMenuItemCaption() {
        return "";
    }

    protected String getDefaultMenuItemCaption() {
        return MessageProvider.getMessage(messagesPack, visible ? "folders.hideFolders" : "folders.showFolders");
    }

    protected Resource getMenuItemIcon() {
        if (visible) {
            return new ThemeResource("icons/folders_pane_icon_active.png");
        } else {
            return new ThemeResource("icons/folders_pane_icon.png");
        }
    }

    protected void openFolder(AbstractSearchFolder folder) {
        if (StringUtils.isBlank(folder.getFilterComponentId())) {
            log.warn("Unable to open folder: componentId is blank");
            return;
        }

        String[] strings = ValuePathHelper.parse(folder.getFilterComponentId());
        String screenId = strings[0];

        WindowInfo windowInfo = AppContext.getBean(WindowConfig.class).getWindowInfo(screenId);

        Map<String, Object> params = new HashMap<String, Object>();

        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
        WindowParams.DISABLE_APPLY_SETTINGS.set(params, true);
        WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);

        if (!StringUtils.isBlank(folder.getTabName())) {
            params.put("description", MessageProvider.getMessage(messagesPack, folder.getTabName()));
        } else {
            params.put("description", MessageProvider.getMessage(messagesPack, folder.getName()));
        }

        params.put("folderId", folder.getId());

        Window window = App.getInstance().getWindowManager().openWindow(windowInfo,
                WindowManager.OpenType.NEW_TAB, params);

        Filter filterComponent = null;

        if (strings.length > 1) {
            String filterComponentId = StringUtils.join(Arrays.copyOfRange(strings, 1, strings.length), '.');
            filterComponent = window.getComponent(filterComponentId);

            FilterEntity filterEntity = new FilterEntity();
            filterEntity.setFolder(folder);
            filterEntity.setComponentId(folder.getFilterComponentId());
            if (folder instanceof AppFolder) {
                filterEntity.setName(((AppFolder) folder).getLocName());
                filterEntity.setCode(folder.getName());
            } else {
                filterEntity.setName(folder.getName());
                filterEntity.setCode(folder.getName());
            }
            filterEntity.setXml(folder.getFilterXml());
            filterEntity.setApplyDefault(BooleanUtils.isNotFalse(folder.getApplyDefault()));
            if (folder instanceof SearchFolder) {
                filterEntity.setIsSet(((SearchFolder) folder).getIsSet());
            }
            filterComponent.setFilterEntity(filterEntity);
        }

        window.applySettings(new SettingsImpl(window.getId()));

        if (filterComponent != null && folder instanceof SearchFolder) {
            final SearchFolder searchFolder = (SearchFolder) folder;
            if (searchFolder.getPresentation() != null) {
                ((com.haulmont.cuba.gui.components.Component.HasPresentations) filterComponent.getApplyTo())
                        .applyPresentation(searchFolder.getPresentation().getId());
            }
        }

        ((DsContextImplementation) window.getDsContext()).resumeSuspended();
    }

    protected boolean isNeedRootAppFolder() {
        return false;
    }

    protected boolean isNeedRootSearchFolder() {
        return false;
    }

    protected boolean isNeedFoldersTitle() {
        return true;
    }

    public Folder saveFolder(Folder folder) {
        CommitContext commitContext = new CommitContext(Collections.singleton(folder));
        Set<Entity> res = ServiceLocator.getDataService().commit(commitContext);
        for (Entity entity : res) {
            if (entity.equals(folder))
                return (Folder) entity;
        }
        return null;
    }

    public void removeFolder(Folder folder) {
        CommitContext commitContext = new CommitContext(Collections.emptySet(), Collections.singleton(folder));
        ServiceLocator.getDataService().commit(commitContext);
    }

    public com.haulmont.cuba.web.toolkit.ui.Tree getSearchFoldersTree() {
        return searchFoldersTree;
    }

    public com.haulmont.cuba.web.toolkit.ui.Tree getAppFoldersTree() {
        return appFoldersTree;
    }

    public Collection<SearchFolder> getSearchFolders() {
        if (searchFoldersTree == null)
            return Collections.emptyList();
        else {
            List result = new ArrayList(searchFoldersTree.getItemIds());
            result.remove(searchFoldersRoot);
            return result;
        }
    }

    protected boolean getItemClickable(Folder folder) {
        return folder instanceof AbstractSearchFolder
                        && !StringUtils.isBlank(((AbstractSearchFolder) folder).getFilterComponentId());
    }

    protected class FolderTreeStyleProvider implements Tree.ItemStyleGenerator {
        @Override
        public String getStyle(Object itemId) {
            Folder folder = ((Folder) itemId);
            if (folder != null) {
                String style = "";
                // clickable tree item
                if (getItemClickable(folder))
                    style = "clickable-folder";
                else
                    style = "nonclickable-folder";
                // handle custom styles
                if (StringUtils.isNotBlank(folder.getItemStyle())) {
                    if (style.equals(""))
                        style = folder.getItemStyle();
                    else
                        style += " " + folder.getItemStyle();
                }

                return style;
            }
            return "";
        }
    }

    protected class FolderClickListener implements ItemClickEvent.ItemClickListener {
        @Override
        public void itemClick(ItemClickEvent event) {
            Folder folder = (Folder) event.getItemId();
            if (getItemClickable(folder)) {
                if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
                    if (appFoldersTree != null && appFoldersTree.containsId(event.getItemId()))
                        appFoldersTree.select(event.getItemId());
                    else if (searchFoldersTree != null && searchFoldersTree.containsId(event.getItemId()))
                        searchFoldersTree.select(event.getItemId());
                } else {
                    openFolder((AbstractSearchFolder) event.getItemId());
                }
            }
        }
    }

    protected class AppFolderActionsHandler implements Action.Handler {
        @Override
        public Action[] getActions(Object target, Object sender) {
            if (target instanceof Folder) {
                if (UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.appFolder.global")) {
                    return new Action[]{new OpenAction(), new CreateAction(true), new CopyAction(),
                            new EditAction(), new RemoveAction(), new ExportAction(), new ImportAction()};
                } else {
                    return new Action[]{new OpenAction()};
                }

            } else {
                return null;
            }
        }

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if (target instanceof Folder)
                ((FolderAction) action).perform((Folder) target);
            else
                ((FolderAction) action).perform(null);
        }
    }

    protected class SearchFolderActionsHandler extends AppFolderActionsHandler {

        @Override
        public Action[] getActions(Object target, Object sender) {
            if (target instanceof SearchFolder) {
                if (isGlobalFolder((SearchFolder) target)) {
                    if (isFilterFolder((SearchFolder) target)) {
                        if (isGlobalSearchFolderPermitted()) {
                            return createAllActions();
                        } else {
                            return createOpenCreateAction();
                        }
                    } else {
                        if (isGlobalSearchFolderPermitted()) {
                            return createWithoutOpenActions();
                        } else {
                            return createOnlyCreateAction();
                        }
                    }
                } else {
                    if (isFilterFolder((SearchFolder) target)) {
                        if (isOwner((SearchFolder) target)) {
                            return createAllActions();
                        } else {
                            return createOpenCreateAction();
                        }
                    } else {
                        if (isOwner((SearchFolder) target)) {
                            return createWithoutOpenActions();
                        } else {
                            return createOnlyCreateAction();
                        }
                    }
                }
            } else {
                return createOnlyCreateAction();
            }
        }

        private boolean isGlobalFolder(SearchFolder folder) {
            return (folder.getUser() == null);
        }

        private boolean isFilterFolder(SearchFolder folder) {
            return (folder.getFilterComponentId() != null);
        }

        private boolean isOwner(SearchFolder folder) {
            return UserSessionProvider.getUserSession().getUser().equals(folder.getUser());
        }

        private boolean isGlobalSearchFolderPermitted() {
            return (UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.searchFolder.global"));
        }

        private Action[] createAllActions() {
            return new Action[]{new OpenAction(), new CopyAction(), new CreateAction(false),
                    new EditAction(), new RemoveAction(), new ExportAction(), new ImportAction()};
        }

        private Action[] createWithoutOpenActions() {
            return new Action[]{new CreateAction(false), new EditAction(), new RemoveAction()};
        }

        private Action[] createOnlyCreateAction() {
            return new Action[]{new CreateAction(false)};
        }

        private Action[] createOpenCreateAction() {
            return new Action[]{new OpenAction(), new CreateAction(false), new CopyAction()};
        }

    }

    protected abstract class FolderAction extends Action {

        public FolderAction(String caption) {
            super(caption);
        }

        public abstract void perform(Folder folder);
    }

    protected class OpenAction extends FolderAction {

        public OpenAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.openFolderAction"));
        }

        @Override
        public void perform(Folder folder) {
            if (folder instanceof AbstractSearchFolder)
                openFolder((AbstractSearchFolder) folder);
        }
    }

    protected class CreateAction extends FolderAction {

        private boolean isAppFolder;

        public CreateAction(boolean isAppFolder) {
            super(MessageProvider.getMessage(messagesPack, "folders.createFolderAction"));
            this.isAppFolder = isAppFolder;
        }

        @Override
        public void perform(final Folder folder) {
            final Folder newFolder = isAppFolder ? MetadataProvider.create(AppFolder.class) : (new SearchFolder());
            newFolder.setName("");
            newFolder.setTabName("");
            newFolder.setParent(folder);
            final FolderEditWindow window = AppFolderEditWindow.create(isAppFolder, true, newFolder, null,
                    new Runnable() {
                        @Override
                        public void run() {
                            saveFolder(newFolder);
                            refreshFolders();
                        }
                    });

            window.addListener(new com.vaadin.ui.Window.CloseListener() {
                @Override
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    App.getInstance().getAppWindow().removeWindow(window);
                }
            });
            App.getInstance().getAppWindow().addWindow(window);
        }
    }

    protected class CopyAction extends FolderAction {
        public CopyAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.copyFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            final AbstractSearchFolder newFolder;
            newFolder = MetadataProvider.create(folder.getMetaClass());
            newFolder.copyFrom((AbstractSearchFolder) folder);
            new EditAction().perform(newFolder);
        }
    }

    protected class EditAction extends FolderAction {

        public EditAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.editFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            final FolderEditWindow window;
            if (folder instanceof SearchFolder) {
                window = new FolderEditWindow(false, folder, null, new Runnable() {
                    @Override
                    public void run() {
                        saveFolder(folder);
                        refreshFolders();
                    }
                });
            } else {
                if (folder instanceof AppFolder) {
                    window = AppFolderEditWindow.create(true, false, folder, null, new Runnable() {
                        @Override
                        public void run() {
                            saveFolder(folder);
                            refreshFolders();
                        }
                    });
                } else
                    return;
            }
            window.addListener(new com.vaadin.ui.Window.CloseListener() {
                @Override
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    App.getInstance().getAppWindow().removeWindow(window);
                }
            });
            App.getInstance().getAppWindow().addWindow(window);
        }
    }

    protected class RemoveAction extends FolderAction {

        public RemoveAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.removeFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            App.getInstance().getWindowManager().showOptionDialog(
                    MessageProvider.getMessage(messagesPack, "dialogs.Confirmation"),
                    MessageProvider.getMessage(messagesPack, "folders.removeFolderConfirmation"),
                    IFrame.MessageType.CONFIRMATION,
                    new com.haulmont.cuba.gui.components.Action[]{
                            new DialogAction(DialogAction.Type.YES) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    removeFolder(folder);
                                    refreshFolders();
                                }
                            },
                            new DialogAction(DialogAction.Type.NO)
                    }
            );
        }
    }

    protected class ExportAction extends FolderAction {

        public ExportAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.exportFolderAction"));
        }

        @Override
        public void perform(Folder folder) {
            FoldersService service = ServiceLocator.lookup(FoldersService.NAME);
            try {
                new WebExportDisplay().show(new ByteArrayDataProvider(
                        service.exportFolder(folder)), "Folders", ExportFormat.ZIP);
            } catch (IOException ignored) {
            }
        }
    }

    private class ImportAction extends FolderAction {
        protected ImportAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.importFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            // TODO Get rid of ReportImportDialog here
//            WindowConfig windowConfig = AppContext.getBean(WindowConfig.class);
//            final ReportImportDialog importDialog = App.getInstance().getWindowManager().
//                    openWindow(windowConfig.getWindowInfo("report$Report.fileUploadDialog"),
//                            WindowManager.OpenType.DIALOG);
//
//            importDialog.addListener(new Window.CloseListener() {
//                @Override
//                public void windowClosed(String actionId) {
//                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
//                        try {
//                            FoldersService service = ServiceLocator.lookup(FoldersService.NAME);
//                            service.importFolder(folder, importDialog.getBytes());
//                        } catch (Exception ex) {
//                            importDialog.showNotification(
//                                    importDialog.getMessage("notification.importFailed"),
//                                    ex.getMessage(),
//                                    IFrame.NotificationType.ERROR
//                            );
//                        }
//                        refreshFolders();
//                    }
//                }
//            });
        }
    }

    public class AppFoldersUpdater implements Timer.Listener {

        @Override
        public void onTimer(Timer timer) {
            reloadAppFolders();
        }

        @Override
        public void onStopTimer(Timer timer) {
        }
    }

    protected static MenuBar.MenuItem getFirstMenuItem(MenuBar menuBar) {
        return menuBar.getItems().isEmpty() ? null : menuBar.getItems().get(0);
    }
}