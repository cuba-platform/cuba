/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.core.file.FileUploadDialog;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskWrapper;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.sys.AppTimers;
import com.haulmont.cuba.web.toolkit.Timer;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Tree;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

/**
 * Left panel containing application and search folders.
 *
 * @author krivopustov
 * @version $Id$
 */
public class FoldersPane extends VerticalLayout {

    private static final long serialVersionUID = 6666603397626574763L;

    private static Log log = LogFactory.getLog(FoldersPane.class);

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
    protected SplitPanel vertSplit;
    protected SplitPanel horSplit;

    protected WebConfig webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

    protected Messages messages = AppBeans.get(Messages.class);

    protected Metadata metadata = AppBeans.get(Metadata.class);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    protected UserSettingService userSettingService = AppBeans.get(UserSettingService.class);

    protected FoldersService foldersService = AppBeans.get(FoldersService.class);

    protected DataService dataService = AppBeans.get(DataService.class);

    protected UserSettingsTools userSettingsTools = AppBeans.get(UserSettingsTools.class);

    protected BackgroundTaskWrapper<Integer, List<AppFolder>> folderUpdateBackgroundTaskWrapper;

    public FoldersPane(MenuBar menuBar, AppWindow appWindow) {
        this.menuBar = menuBar;
        parentAppWindow = appWindow;

        setHeight(100, Sizeable.UNITS_PERCENTAGE);
        setStyleName("folderspane");
        folderUpdateBackgroundTaskWrapper = new BackgroundTaskWrapper(new AppFolderUpdateBackgroundTask(10));
    }

    public void init(Component parent) {
        if (parent instanceof SplitPanel) {
            horSplit = (SplitPanel) parent;
        }

        boolean visible;
        UserSettingsTools.FoldersState state = userSettingsTools.loadFoldersState();
        if (state == null) {
            visible = webConfig.getFoldersPaneVisibleByDefault() || webConfig.getUseLightHeader();
            horizontalSplitPos = webConfig.getFoldersPaneDefaultWidth();
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

            private static final long serialVersionUID = 6869815029204595973L;

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
                    appFoldersLabel = new Label(messages.getMainMessage("folders.appFoldersRoot"));
                    appFoldersLabel.setStyleName("folderspane-caption");
                } else {
                    appFoldersLabel = null;
                }

                int period = webConfig.getAppFoldersRefreshPeriodSec() * 1000;

                // find old timers
                AppTimers appTimers = App.getInstance().getTimers();
                Collection<Timer> timers = appTimers.getAll(parentAppWindow);
                for (Timer t : timers) {
                    if (t instanceof FoldersPane.FoldersPaneTimer)
                        t.stop();
                }

                timer = new FoldersPaneTimer(period, true);
                timer.addListener(createAppFolderUpdater());
                appTimers.add(timer, parentAppWindow);
                timer.start();
            }

            Component searchFoldersPane = createSearchFoldersPane();
            if (searchFoldersPane != null) {
                searchFoldersPane.setHeight("97%");
                searchFoldersPane.setWidth("96%");
                if (isNeedFoldersTitle()) {
                    searchFoldersLabel = new Label(messages.getMainMessage("folders.searchFoldersRoot"));
                    searchFoldersLabel.setStyleName("folderspane-caption");
                } else {
                    searchFoldersLabel = null;
                }
            }

            if (appFoldersPane != null && searchFoldersPane != null) {
                vertSplit = new WebSplitPanel().getComponent();
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
                timer.stop();

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
        String s = userSettingService.loadSetting(foldersCollapse);
        List<UUID> idFolders = strToIds(s);
        for (AbstractSearchFolder folder : (Collection<AbstractSearchFolder>) tree.getItemIds()) {
            if (idFolders.contains(folder.getId())) {
                tree.collapseItem(folder);
            }
        }
        tree.addListener(new Tree.ExpandListener() {
            private static final long serialVersionUID = 5469841059576722790L;

            @Override
            public void nodeExpand(Tree.ExpandEvent event) {
                if (event.getItemId() instanceof AbstractSearchFolder) {
                    UUID uuid = ((AbstractSearchFolder) event.getItemId()).getId();
                    String str = userSettingService.loadSetting(foldersCollapse);
                    userSettingService.saveSetting(foldersCollapse, removeIdInStr(str, uuid));
                }
            }
        });
        tree.addListener(new Tree.CollapseListener() {
            private static final long serialVersionUID = 5575939977767407729L;

            @Override
            public void nodeCollapse(Tree.CollapseEvent event) {
                if (event.getItemId() instanceof AbstractSearchFolder) {
                    UUID uuid = ((AbstractSearchFolder) event.getItemId()).getId();
                    String str = userSettingService.loadSetting(foldersCollapse);
                    userSettingService.saveSetting(foldersCollapse, addIdInStr(str, uuid));
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
        StringBuilder sb = new StringBuilder();
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
            if (horSplit != null) {
                horizontalSplitPos = horSplit.getSplitPosition();
            }
            if (vertSplit != null) {
                verticalSplitPos = vertSplit.getSplitPosition();
            }
        }
        userSettingsTools.saveFoldersState(
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

        List<AppFolder> reloadedFolders = getReloadedFolders();
        updateFolders(reloadedFolders);
    }

    public void asyncReloadAppFolders() {
        if (appFoldersTree == null)
            return;
        folderUpdateBackgroundTaskWrapper.restart();
    }

    protected Collection<AppFolder> getRecursivelyChildAppFolders(AppFolder parentFolder) {
        Collection<AppFolder> result = new LinkedList<>();
        Collection<AppFolder> childFolders = (Collection<AppFolder>) appFoldersTree.getChildren(parentFolder);
        if (childFolders != null) {
            result.addAll(childFolders);
            for (AppFolder folder : childFolders)
                result.addAll(getRecursivelyChildAppFolders(folder));
        }
        return result;
    }

    protected void updateQuantityAndItemStyleAppFolder(AppFolder parentFolder, @Nullable List<AppFolder> reloadedFolders) {
        Collection<AppFolder> childFolders = getRecursivelyChildAppFolders(parentFolder);
        int sumOfChildQuantity = 0;
        Set<String> childFoldersStyleSet = new HashSet<>();
        for (AppFolder childFolder : childFolders) {
            if (reloadedFolders != null) {
                childFolder = reloadedFolders.get(reloadedFolders.indexOf(childFolder));
            }
            sumOfChildQuantity += !StringUtils.isBlank(childFolder.getQuantityScript()) ? childFolder.getQuantity() : 0;
            if (childFolder.getItemStyle() != null)
                childFoldersStyleSet.add(childFolder.getItemStyle());
        }
        parentFolder.setQuantity(sumOfChildQuantity);
        if (!childFoldersStyleSet.isEmpty()) {
            parentFolder.setItemStyle(StringUtils.join(childFoldersStyleSet, " "));
        } else
            parentFolder.setItemStyle("");
    }

    protected List<AppFolder> getReloadedFolders() {
        List<AppFolder> folders = new ArrayList(appFoldersTree.getItemIds());
        FoldersService service = AppBeans.get(FoldersService.NAME);
        folders = service.reloadAppFolders(folders);

        for (AppFolder folder : folders) {
            if (StringUtils.isBlank(folder.getQuantityScript()) && folder.getQuantity() != null)
                updateQuantityAndItemStyleAppFolder(folder, folders);
        }

        return folders;
    }

    protected void updateFolders(List<AppFolder> reloadedFolders) {
        List<AppFolder> folders = new ArrayList(appFoldersTree.getItemIds());
        for (AppFolder folder : reloadedFolders) {
            int index = reloadedFolders.indexOf(folder);
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
        List<AppFolder> appFolders = foldersService.loadAppFolders();
        if (appFolders.isEmpty())
            return null;

        appFoldersTree = new com.haulmont.cuba.web.toolkit.ui.Tree();
//        appFoldersTree.setDoubleClickMode(true);
        appFoldersTree.setItemStyleGenerator(new FolderTreeStyleProvider());
        appFoldersTree.addListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent event) {
                AppFolder folder = (AppFolder) event.getItemId();
                if (StringUtils.isBlank(folder.getQuantityScript())) {
                    folder.setQuantity(null);
                    folder.setItemStyle(null);
                    appFoldersTree.setItemCaption(folder, folder.getCaption());
                }
            }
        });
        appFoldersTree.addListener(new Tree.CollapseListener() {
            @Override
            public void nodeCollapse(Tree.CollapseEvent event) {
                AppFolder folder = (AppFolder) event.getItemId();
                if (StringUtils.isBlank(folder.getQuantityScript())) {
                    updateQuantityAndItemStyleAppFolder(folder, null);
                    appFoldersTree.setItemCaption(folder, folder.getCaption());
                }
            }
        });

        appFoldersRoot = messages.getMainMessage("folders.appFoldersRoot");
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

        List<SearchFolder> searchFolders = foldersService.loadSearchFolders();
        searchFoldersRoot = messages.getMainMessage("folders.searchFoldersRoot");
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
            if (webConfig.getShowFolderIcons()) {
                if (folder instanceof SearchFolder) {
                    if (BooleanUtils.isTrue(((SearchFolder) folder).getIsSet())) {
                        tree.setItemIcon(folder, new VersionedThemeResource("icons/set-small.png"));
                    } else {
                        tree.setItemIcon(folder, new VersionedThemeResource("icons/search-folder-small.png"));
                    }
                } else if (folder instanceof AppFolder) {
                    tree.setItemIcon(folder, new VersionedThemeResource("icons/app-folder-small.png"));
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
        return messages.getMainMessage(visible ? "folders.hideFolders" : "folders.showFolders");
    }

    protected Resource getMenuItemIcon() {
        if (visible) {
            return new VersionedThemeResource("icons/folders_pane_icon_active.png");
        } else {
            return new VersionedThemeResource("icons/folders_pane_icon.png");
        }
    }

    protected void openFolder(AbstractSearchFolder folder) {
        if (StringUtils.isBlank(folder.getFilterComponentId())) {
            log.warn("Unable to open folder: componentId is blank");
            return;
        }

        String[] strings = ValuePathHelper.parse(folder.getFilterComponentId());
        String screenId = strings[0];

        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo(screenId);

        Map<String, Object> params = new HashMap<String, Object>();

        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
        WindowParams.DISABLE_APPLY_SETTINGS.set(params, true);
        WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);

        if (!StringUtils.isBlank(folder.getTabName())) {
            WindowParams.DESCRIPTION.set(params, messages.getMainMessage(folder.getTabName()));
        } else {
            WindowParams.DESCRIPTION.set(params, messages.getMainMessage(folder.getName()));
        }

        WindowParams.FOLDER_ID.set(params, folder.getId());

        Window window = App.getInstance().getWindowManager().openWindow(windowInfo,
                WindowManager.OpenType.NEW_TAB, params);

        Filter filterComponent = null;

        if (strings.length > 1) {
            String filterComponentId = StringUtils.join(Arrays.copyOfRange(strings, 1, strings.length), '.');
            filterComponent = window.getComponent(filterComponentId);

            FilterEntity filterEntity = new FilterEntity();
            filterEntity.setFolder(folder);
            filterEntity.setComponentId(folder.getFilterComponentId());
            filterEntity.setName(folder.getLocName());
            filterEntity.setCode(folder.getName());

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
        Set<Entity> res = dataService.commit(commitContext);
        for (Entity entity : res) {
            if (entity.equals(folder))
                return (Folder) entity;
        }
        return null;
    }

    public void removeFolder(Folder folder) {
        CommitContext commitContext = new CommitContext(Collections.emptySet(), Collections.singleton(folder));
        dataService.commit(commitContext);
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

    protected boolean isItemExpandable(Object folder) {
        return folder instanceof AbstractSearchFolder &&
                StringUtils.isBlank(((AbstractSearchFolder) folder).getFilterComponentId());
    }

    protected class FolderTreeStyleProvider implements Tree.ItemStyleGenerator {
        private static final long serialVersionUID = 3346848644718707748L;

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
        private static final long serialVersionUID = -5975272418037777967L;

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
            } else if (isItemExpandable(folder)) {
                Component tree = event.getComponent();
                if (tree instanceof Tree && isItemExpandable(folder)) {
                    if (((Tree) tree).isExpanded(folder))
                        ((Tree) tree).collapseItem(folder);
                    else
                        ((Tree) tree).expandItem(folder);
                }
            }
        }
    }

    protected class AppFolderActionsHandler implements Action.Handler {

        private static final long serialVersionUID = -2312945707104156806L;

        private OpenAction openAction = new OpenAction();
        private CreateAction createAction = new CreateAction(true);
        private CopyAction copyAction = new CopyAction();
        private EditAction editAction = new EditAction();
        private RemoveAction removeAction = new RemoveAction();
        private ExportAction exportAction = new ExportAction();
        private ImportAction importAction = new ImportAction();

        @Override
        public Action[] getActions(Object target, Object sender) {
            if (target instanceof Folder) {
                if (userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.appFolder.global")) {
                    return new Action[] {openAction, createAction, copyAction,
                            editAction, removeAction, exportAction, importAction};
                } else {
                    return new Action[] {openAction};
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

        private static final long serialVersionUID = -4187914216755933883L;

        private OpenAction openAction = new OpenAction();
        private CopyAction copyAction = new CopyAction();
        private CreateAction createAction = new CreateAction(false);
        private EditAction editAction = new EditAction();
        private RemoveAction removeAction = new RemoveAction();
        private ExportAction exportAction = new ExportAction();
        private ImportAction importAction = new ImportAction();

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
            return userSessionSource.getUserSession().getUser().equals(folder.getUser());
        }

        private boolean isGlobalSearchFolderPermitted() {
            return (userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.searchFolder.global"));
        }

        private Action[] createAllActions() {
            return new Action[] {openAction, copyAction, createAction,
                    editAction, removeAction, exportAction, importAction};
        }

        private Action[] createWithoutOpenActions() {
            return new Action[] {createAction, editAction, removeAction};
        }

        private Action[] createOnlyCreateAction() {
            return new Action[] {createAction};
        }

        private Action[] createOpenCreateAction() {
            return new Action[] {openAction, createAction, copyAction};
        }

    }

    protected abstract class FolderAction extends Action {

        private static final long serialVersionUID = -4097329335200783939L;

        public FolderAction(String caption) {
            super(caption);
        }

        public abstract void perform(Folder folder);
    }

    protected class OpenAction extends FolderAction {

        private static final long serialVersionUID = 1000154780292112851L;

        public OpenAction() {
            super(messages.getMainMessage("folders.openFolderAction"));
        }

        @Override
        public void perform(Folder folder) {
            if (folder instanceof AbstractSearchFolder)
                openFolder((AbstractSearchFolder) folder);
        }
    }

    protected class CreateAction extends FolderAction {

        private static final long serialVersionUID = -7050374751418360734L;

        private boolean isAppFolder;

        public CreateAction(boolean isAppFolder) {
            super(messages.getMainMessage("folders.createFolderAction"));
            this.isAppFolder = isAppFolder;
        }

        @Override
        public void perform(final Folder folder) {
            final Folder newFolder = isAppFolder ? metadata.create(AppFolder.class) : metadata.create(SearchFolder.class);
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
                private static final long serialVersionUID = 5604371155722856067L;

                @Override
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    App.getInstance().getAppWindow().removeWindow(window);
                }
            });
            App.getInstance().getAppWindow().addWindow(window);
        }
    }

    protected class CopyAction extends FolderAction {
        private static final long serialVersionUID = -4472902118887902921L;

        public CopyAction() {
            super(messages.getMainMessage("folders.copyFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            final AbstractSearchFolder newFolder;
            newFolder = metadata.create(folder.getMetaClass());
            newFolder.copyFrom((AbstractSearchFolder) folder);
            new EditAction().perform(newFolder);
        }
    }

    protected class EditAction extends FolderAction {

        private static final long serialVersionUID = 297056776792080638L;

        public EditAction() {
            super(messages.getMainMessage("folders.editFolderAction"));
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
                private static final long serialVersionUID = 3267969862627920749L;

                @Override
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    App.getInstance().getAppWindow().removeWindow(window);
                }
            });
            App.getInstance().getAppWindow().addWindow(window);
        }
    }

    protected class RemoveAction extends FolderAction {

        private static final long serialVersionUID = 6861269931995018516L;

        public RemoveAction() {
            super(messages.getMainMessage("folders.removeFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            App.getInstance().getWindowManager().showOptionDialog(
                    messages.getMainMessage("dialogs.Confirmation"),
                    messages.getMainMessage("folders.removeFolderConfirmation"),
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

        private static final long serialVersionUID = -8455267774573271204L;

        public ExportAction() {
            super(messages.getMainMessage("folders.exportFolderAction"));
        }

        @Override
        public void perform(Folder folder) {
            try {
                byte[] data = foldersService.exportFolder(folder);
                new WebExportDisplay().show(new ByteArrayDataProvider(data), "Folders", ExportFormat.ZIP);
            } catch (IOException ignored) {
            }
        }
    }

    protected class ImportAction extends FolderAction {
        private static final long serialVersionUID = 5466565178242730937L;

        public ImportAction() {
            super(messages.getMainMessage("folders.importFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
            final FileUploadDialog dialog = App.getInstance().getWindowManager().
                    openWindow(windowConfig.getWindowInfo("fileUploadDialog"), WindowManager.OpenType.DIALOG);

            dialog.addListener(new Window.CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        try {
                            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.class);
                            byte[] data = FileUtils.readFileToByteArray(fileUploading.getFile(dialog.getFileId()));
                            fileUploading.deleteFile(dialog.getFileId());
                            foldersService.importFolder(folder, data);
                        } catch (Exception ex) {
                            dialog.showNotification(
                                    messages.getMainMessage("folders.importFailedNotification"),
                                    ex.getMessage(),
                                    IFrame.NotificationType.ERROR
                            );
                        }
                        refreshFolders();
                    }
                }
            });
        }
    }

    public class AppFolderUpdateBackgroundTask extends BackgroundTask<Integer, List<AppFolder>> {

        public AppFolderUpdateBackgroundTask(long timeoutSeconds) {
            super(timeoutSeconds);
        }

        @Override
        public List<AppFolder> run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
            return getReloadedFolders();
        }

        @Override
        public void done(List<AppFolder> reloadedFolders) {
            updateFolders(reloadedFolders);
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

    protected static class FoldersPaneTimer extends Timer {

        public FoldersPaneTimer(int delay, boolean repeating) {
            super(delay, repeating);
        }
    }
}