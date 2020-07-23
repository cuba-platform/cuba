/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.app.core.file.FileUploadDialog;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskWrapper;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.widgets.CubaTimer;
import com.haulmont.cuba.web.widgets.CubaTree;
import com.haulmont.cuba.web.widgets.CubaVerticalActionsLayout;
import com.haulmont.cuba.web.widgets.grid.CubaGridContextMenu;
import com.haulmont.cuba.web.widgets.grid.CubaSingleSelectionModel;
import com.haulmont.cuba.web.widgets.tree.EnhancedTreeDataProvider;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.Action;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.SerializableFunction;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.util.ReflectTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.haulmont.cuba.gui.components.Window.COMMIT_ACTION_ID;

/**
 * Left panel containing application and search folders.
 */
public class CubaFoldersPane extends VerticalLayout {

    private static final long serialVersionUID = 6666603397626574763L;
    public static final String C_FOLDERS_PANE = "c-folders-pane";

    protected static final Method REFRESH_FOLDERS_METHOD = ReflectTools.findMethod(
            RefreshFoldersListener.class, "refreshFolders", RefreshFoldersEvent.class);

    protected boolean visible;

    protected CubaTree<AppFolder> appFoldersTree;
    protected CubaTree<SearchFolder> searchFoldersTree;

    protected Label appFoldersLabel;
    protected Label searchFoldersLabel;

    protected FoldersPaneTimer timer;

    protected static final int DEFAULT_VERT_SPLIT_POS = 50;
    protected int verticalSplitPos;

    protected VerticalSplitPanel vertSplit;

    protected WebConfig webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected UserSettingService userSettingService = AppBeans.get(UserSettingService.NAME);

    protected FoldersService foldersService = AppBeans.get(FoldersService.NAME);

    protected DataService dataService = AppBeans.get(DataService.NAME);

    protected UserSettingsTools userSettingsTools = AppBeans.get(UserSettingsTools.NAME);

    protected Folders folders = AppBeans.get(Folders.NAME);

    protected EntityStates entityStates = AppBeans.get(EntityStates.class);

    protected BackgroundTaskWrapper<Integer, List<AppFolder>> folderUpdateBackgroundTaskWrapper;

    protected Frame frame;

    public CubaFoldersPane() {
        setSizeFull();
        setMargin(false);
        setSpacing(true);

        setStyleName(C_FOLDERS_PANE);
        //noinspection unchecked
        folderUpdateBackgroundTaskWrapper = new BackgroundTaskWrapper(new AppFolderUpdateBackgroundTask(10));
    }

    public void setVerticalSplitPosition(float verticalSplitPos) {
        if (vertSplit != null
                && vertSplit.getSplitPosition() != verticalSplitPos) {
            vertSplit.setSplitPosition(verticalSplitPos);
        }
    }

    public float getVerticalSplitPosition() {
        return vertSplit != null
                ? vertSplit.getSplitPosition()
                : DEFAULT_VERT_SPLIT_POS;
    }

    public void loadFolders() {
        UserSettingsTools.FoldersState state = userSettingsTools.loadFoldersState();
        if (state == null) {
            verticalSplitPos = DEFAULT_VERT_SPLIT_POS;
        } else {
            verticalSplitPos = state.verticalSplit;
        }

        showFolders(true);
    }

    protected void showFolders(boolean show) {
        if (show == visible)
            return;

        if (show) {
            Component appFoldersPane = createAppFoldersPane();
            if (appFoldersPane != null) {
                setupAppFoldersPane(appFoldersPane);
                setupUpdateTimer();
            }

            Component searchFoldersPane = createSearchFoldersPane();
            if (searchFoldersPane != null) {
                setupSearchFoldersPane(searchFoldersPane);
            }

            createFoldersPaneLayout(appFoldersPane, searchFoldersPane);
            adjustLayout();

            if (getParent() != null)
                getParent().markAsDirty();

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

            appFoldersTree = null;
            searchFoldersTree = null;
        }

        visible = show;
    }

    protected void createFoldersPaneLayout(Component appFoldersPane, Component searchFoldersPane) {
        if (appFoldersPane != null && searchFoldersPane != null) {
            vertSplit = new VerticalSplitPanel();
            vertSplit.setSplitPosition(verticalSplitPos);

            CubaVerticalActionsLayout afLayout = createFoldersPaneLayout(appFoldersPane, appFoldersLabel);
            vertSplit.setFirstComponent(afLayout);

            CubaVerticalActionsLayout sfLayout = createFoldersPaneLayout(searchFoldersPane, searchFoldersLabel);
            vertSplit.setSecondComponent(sfLayout);

            addComponent(vertSplit);
        } else {
            if (appFoldersPane != null) {
                // we need to wrap the folders tree with a layout in order to provide margins
                CubaVerticalActionsLayout afLayout = createFoldersPaneLayout(appFoldersPane, appFoldersLabel);
                addComponent(afLayout);
            }

            if (searchFoldersPane != null) {
                // we need to wrap the folders tree with a layout in order to provide margins
                CubaVerticalActionsLayout sfLayout = createFoldersPaneLayout(searchFoldersPane, searchFoldersLabel);
                addComponent(sfLayout);
            }
        }
    }

    protected CubaVerticalActionsLayout createFoldersPaneLayout(Component foldersPane, Label foldersLabel) {
        CubaVerticalActionsLayout layout = new CubaVerticalActionsLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        if (foldersLabel != null)
            addFoldersLabel(layout, foldersLabel);
        layout.addComponent(foldersPane);
        layout.setExpandRatio(foldersPane, 1);

        layout.addShortcutListener(
                new ShortcutListenerDelegate("apply" + foldersPane.getCubaId(), ShortcutAction.KeyCode.ENTER, null)
                        .withHandler((sender, target) -> {
                            if (sender == layout) {
                                handleFoldersPaneShortcutAction(foldersPane);
                            }
                        }));

        return layout;
    }

    @SuppressWarnings("unchecked")
    protected void handleFoldersPaneShortcutAction(Component foldersPane) {
        AbstractSearchFolder folder = ((CubaTree<AbstractSearchFolder>) foldersPane).asSingleSelect().getValue();
        if (folder != null) {
            openFolder(folder);
        }
    }

    protected void setupSearchFoldersPane(Component searchFoldersPane) {
        searchFoldersPane.setHeight(100, Unit.PERCENTAGE);
        searchFoldersPane.setWidth(100, Unit.PERCENTAGE);
        if (isNeedFoldersTitle()) {
            searchFoldersLabel = new Label(messages.getMainMessage("folders.searchFoldersRoot"));
            searchFoldersLabel.setStyleName("c-folders-pane-caption");
        } else {
            searchFoldersLabel = null;
        }
    }

    protected void setupUpdateTimer() {
        if (webConfig.getFoldersPaneEnabled()) {
            int period = webConfig.getAppFoldersRefreshPeriodSec() * 1000;

            timer = new FoldersPaneTimer();
            timer.setRepeating(true);
            timer.setDelay(period);
            timer.addActionListener(createAppFolderUpdater());
            timer.start();

            if (this.isAttached()) {
                AppUI ui = AppUI.getCurrent();
                stopExistingFoldersPaneTimer(ui);
                ui.addTimer(timer);
            } else if (frame != null) {
                com.haulmont.cuba.gui.components.Window window = ComponentsHelper.getWindowImplementation(frame);
                if (window == null) {
                    throw new IllegalStateException("Null window for CubaFoldersPane");
                }
                AbstractComponent topLevelFrame = window.unwrapComposition(AbstractComponent.class);
                timer.extend(topLevelFrame);
            }
        }
    }

    protected void stopExistingFoldersPaneTimer(AppUI ui) {
        for (CubaTimer t : ui.getTimers()) {
            if (t instanceof FoldersPaneTimer) {
                t.stop();
            }
        }
    }

    protected void setupAppFoldersPane(Component appFoldersPane) {
        appFoldersPane.setHeight("100%");
        appFoldersPane.setWidth("100%");
        if (isNeedFoldersTitle()) {
            appFoldersLabel = new Label(messages.getMainMessage("folders.appFoldersRoot"));
            appFoldersLabel.setStyleName("c-folders-pane-caption");
        } else {
            appFoldersLabel = null;
        }
    }

    protected <T extends AbstractSearchFolder> void collapseItemInTree(CubaTree<T> tree, final String foldersCollapse) {
        String s = userSettingService.loadSetting(foldersCollapse);
        List<UUID> idFolders = strToIds(s);

        List<T> foldersToCollapse = tree.getItems()
                .filter(folder ->
                        idFolders.contains(folder.getId()))
                .collect(Collectors.toList());

        tree.collapse(foldersToCollapse);

        tree.addExpandListener(event -> {
            if (event.getExpandedItem() != null) {
                UUID uuid = event.getExpandedItem().getId();
                String str = userSettingService.loadSetting(foldersCollapse);
                userSettingService.saveSetting(foldersCollapse, removeIdInStr(str, uuid));
            }
        });

        tree.addCollapseListener(event -> {
            if (event.getCollapsedItem() != null) {
                UUID uuid = event.getCollapsedItem().getId();
                String str = userSettingService.loadSetting(foldersCollapse);
                userSettingService.saveSetting(foldersCollapse, addIdInStr(str, uuid));
            }
        });
    }

    @Nullable
    protected String addIdInStr(@Nullable String inputStr, @Nullable UUID uuid) {
        if (uuid == null) {
            return inputStr;
        }

        String str = uuid.toString();
        if (inputStr == null) {
            return str;
        } else if (!inputStr.contains(str)) {
            return inputStr + ":" + str;
        } else {
            return inputStr;
        }
    }

    @Nullable
    protected String removeIdInStr(@Nullable String inputStr, @Nullable UUID uuid) {
        if (inputStr != null && uuid != null) {
            List<UUID> uuids = strToIds(inputStr);
            uuids.remove(uuid);
            inputStr = idsToStr(uuids);
        }

        return inputStr;
    }

    protected List<UUID> strToIds(@Nullable String inputStr) {
        ArrayList<UUID> uuids = new ArrayList<>();
        if (inputStr != null) {
            String[] args = StringUtils.split(inputStr, ':');
            for (String str : args) {
                uuids.add(UUID.fromString(str));
            }
        }
        return uuids;
    }

    @Nullable
    protected String idsToStr(List<UUID> uuids) {
        if (uuids.isEmpty()) {
            return null;
        }

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
        l.setMargin(new MarginInfo(false, true, false, true));
        l.addComponent(label);
        l.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(l);
        return l;
    }

    public void savePosition() {
        if (visible) {
            if (vertSplit != null)
                verticalSplitPos = (int) vertSplit.getSplitPosition();
        }
        userSettingsTools.saveFoldersState(visible, -1, verticalSplitPos);
    }

    protected Consumer<CubaTimer> createAppFolderUpdater() {
        return new AppFoldersUpdater();
    }

    public void refreshFolders() {
        if (visible) {
            showFolders(false);
            showFolders(true);

            fireEvent(new RefreshFoldersEvent(this));
        }
    }

    public void reloadAppFolders() {
        if (appFoldersTree == null)
            return;

        List<AppFolder> reloadedFolders = getReloadedFolders();
        reloadParentFolders(reloadedFolders);
        updateFolders(reloadedFolders);
    }

    public void asyncReloadAppFolders() {
        if (appFoldersTree == null)
            return;
        folderUpdateBackgroundTaskWrapper.restart();
    }

    protected void reloadParentFolders(List<AppFolder> reloadedFolders) {
        for (AppFolder folder : reloadedFolders) {
            if (StringUtils.isBlank(folder.getQuantityScript())) {
                if (appFoldersTree.isExpanded(folder)) {
                    folder.setQuantity(null);
                    folder.setItemStyle("");
                } else {
                    reloadSingleParentFolder(folder, reloadedFolders);
                }
            }
        }
    }

    protected Collection<AppFolder> getChildFolders(AppFolder parentFolder) {
        Collection<AppFolder> result = new LinkedList<>();
        Collection<AppFolder> childFolders = appFoldersTree.getChildren(parentFolder);
        if (childFolders != null) {
            result.addAll(childFolders);
            for (AppFolder folder : childFolders)
                result.addAll(getChildFolders(folder));
        }
        return result;
    }

    protected void reloadSingleParentFolder(AppFolder parentFolder, @Nullable List<AppFolder> reloadedFolders) {
        Collection<AppFolder> childFolders = getChildFolders(parentFolder);
        int sumOfChildQuantity = 0;
        Set<String> childFoldersStyleSet = new HashSet<>();
        for (AppFolder childFolder : childFolders) {
            if (reloadedFolders != null) {
                childFolder = reloadedFolders.get(reloadedFolders.indexOf(childFolder));
            }
            sumOfChildQuantity += !StringUtils.isBlank(childFolder.getQuantityScript())
                    && childFolder.getQuantity() != null ? childFolder.getQuantity() : 0;
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
        List<AppFolder> folders = appFoldersTree.getItems()
                .collect(Collectors.toList());
        FoldersService service = AppBeans.get(FoldersService.NAME);
        return service.reloadAppFolders(folders);
    }

    protected void updateFolders(List<AppFolder> reloadedFolders) {
        List<AppFolder> folders = appFoldersTree.getItems()
                .collect(Collectors.toList());
        for (AppFolder folder : reloadedFolders) {
            int index = reloadedFolders.indexOf(folder);
            AppFolder f = folders.get(index);
            if (f != null) {
                f.setItemStyle(folder.getItemStyle());
                f.setQuantity(folder.getQuantity());
            }
        }
        appFoldersTree.repaint();
    }

    protected void adjustLayout() {
    }

    protected Component createAppFoldersPane() {
        if (!webConfig.getFoldersPaneEnabled())
            return null;
        List<AppFolder> appFolders = foldersService.loadAppFolders();
        if (appFolders.isEmpty())
            return null;
        appFoldersTree = new CubaTree<>();
        appFoldersTree.setCubaId("appFoldersTree");
        appFoldersTree.setDataProvider(createTreeDataProvider());
        appFoldersTree.setGridSelectionModel(new CubaSingleSelectionModel<>());
        appFoldersTree.setStyleGenerator(new FolderTreeStyleProvider<>());

        appFoldersTree.addExpandListener(event -> {
            AppFolder folder = event.getExpandedItem();
            if (StringUtils.isBlank(folder.getQuantityScript())) {
                folder.setQuantity(null);
                folder.setItemStyle(null);
                appFoldersTree.repaint();
            }
        });
        appFoldersTree.addCollapseListener(event -> {
            AppFolder folder = event.getCollapsedItem();
            if (StringUtils.isBlank(folder.getQuantityScript())) {
                reloadSingleParentFolder(folder, null);
                appFoldersTree.repaint();
            }
        });

        fillTree(appFoldersTree, appFolders);
        appFoldersTree.addItemClickListener(new FolderClickListener<>());
        appFoldersTree.setItemCaptionGenerator(this::getFolderTreeItemCaption);

        initAppFoldersContextMenu();

        appFoldersTree.expand(appFoldersTree.getItems().collect(Collectors.toList()));

        return appFoldersTree;
    }

    protected void initAppFoldersContextMenu() {
        new AppFolderGridContextMenu<>(appFoldersTree.getCompositionRoot());
    }

    public void setIconGenerator(IconGenerator<AbstractSearchFolder> iconGenerator) {
        if (appFoldersTree != null) {
            appFoldersTree.setItemIconGenerator(iconGenerator::apply);
        }
        if (searchFoldersTree != null) {
            searchFoldersTree.setItemIconGenerator(iconGenerator::apply);
        }
    }

    protected Component createSearchFoldersPane() {
        if (!webConfig.getFoldersPaneEnabled())
            return null;
        searchFoldersTree = new CubaTree<>();
        searchFoldersTree.setCubaId("searchFoldersTree");
        searchFoldersTree.setDataProvider(createTreeDataProvider());
        searchFoldersTree.setGridSelectionModel(new CubaSingleSelectionModel<>());
        searchFoldersTree.setStyleGenerator(new FolderTreeStyleProvider<>());

        List<SearchFolder> searchFolders = foldersService.loadSearchFolders();

        searchFoldersTree.addItemClickListener(new FolderClickListener<>());
        searchFoldersTree.setItemCaptionGenerator(this::getFolderTreeItemCaption);

        initSearchFoldersContextMenu();

        if (!searchFolders.isEmpty()) {
            fillTree(searchFoldersTree, searchFolders);
        }

        searchFoldersTree.expand(searchFoldersTree.getItems().collect(Collectors.toList()));

        return searchFoldersTree;
    }

    protected void initSearchFoldersContextMenu() {
        new SearchFolderGridContextMenu<>(searchFoldersTree.getCompositionRoot());
    }

    private <T extends Folder> FoldersDataProvider<T> createTreeDataProvider() {
        TreeData<T> treeData = new TreeData<>();
        return new FoldersDataProvider<>(treeData);
    }

    private String getFolderTreeItemCaption(Folder folder) {
        return folder.getCaption();
    }

    protected <T extends Folder> void fillTree(CubaTree<T> tree, List<T> folders) {
        folders.sort(Comparator.comparingInt(this::folderDepth));
        for (T folder : folders) {
            //noinspection unchecked
            T parent = (T) getFolderParent(folder);
            if (tree.getTreeData().contains(parent)) {
                tree.getTreeData().addItem(parent, folder);
            } else {
                tree.getTreeData().addItem(null, folder);
            }
        }
    }

    protected int folderDepth(Folder folder) {
        int depth = 0;
        while (folder != null) {
            depth++;
            folder = getFolderParent(folder);
        }
        return depth;
    }

    /**
     * Returns a given folder's parent only if it's not marked as deleted,
     * otherwise there will be an exception, because a folder returns a not null parent,
     * but there is no such item in tree as it's not loaded as separate folder as it's marked as deleted.
     *
     * @param folder a folder to obtain a parent
     * @return a parent folder
     */
    protected Folder getFolderParent(Folder folder) {
        if (!entityStates.isLoaded(folder, "parent")) {
            return null;
        } else {
            Folder parent = folder.getParent();
            return parent == null
                    ? null
                    : PersistenceHelper.isDeleted(parent)
                    ? null
                    : parent;
        }
    }

    protected void openFolder(AbstractSearchFolder folder) {
        folders.openFolder(folder);
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

    public Tree getSearchFoldersTree() {
        return searchFoldersTree;
    }

    public Tree getAppFoldersTree() {
        return appFoldersTree;
    }

    public Collection<SearchFolder> getSearchFolders() {
        return searchFoldersTree != null
                ? searchFoldersTree.getItems().collect(Collectors.toList())
                : Collections.emptyList();
    }

    protected boolean getItemClickable(Folder folder) {
        return folder instanceof AbstractSearchFolder
                && !StringUtils.isBlank(((AbstractSearchFolder) folder).getFilterComponentId());
    }

    protected boolean isItemExpandable(Folder folder) {
        return folder instanceof AbstractSearchFolder &&
                StringUtils.isBlank(((AbstractSearchFolder) folder).getFilterComponentId());
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Frame getFrame() {
        return frame;
    }

    public void addRefreshFoldersListener(RefreshFoldersListener listener) {
        addListener(RefreshFoldersEvent.class, listener, REFRESH_FOLDERS_METHOD);
    }

    protected class FolderTreeStyleProvider<T extends AbstractSearchFolder> implements StyleGenerator<T> {
        @Override
        public String apply(T folder) {
            if (folder != null) {
                String style;
                // clickable tree item
                if (getItemClickable(folder)) {
                    style = "c-clickable-folder";
                } else {
                    style = "c-nonclickable-folder";
                }

                // handle custom styles
                if (StringUtils.isNotBlank(folder.getItemStyle())) {
                    style += " " + folder.getItemStyle();
                }

                return style;
            }
            return null;
        }
    }

    protected class FolderClickListener<T extends AbstractSearchFolder> implements Tree.ItemClickListener<T> {
        @Override
        public void itemClick(Tree.ItemClick<T> event) {
            if (event.getMouseEventDetails().getButton() == MouseEventDetails.MouseButton.LEFT) {
                T folder = event.getItem();
                if (getItemClickable(folder)) {
                    openFolder(folder);
                } else if (isItemExpandable(folder)) {
                    //noinspection unchecked
                    Tree<AbstractSearchFolder> tree = (Tree<AbstractSearchFolder>) event.getSource();
                    if (tree.isExpanded(folder)) {
                        tree.collapse(folder);
                    } else {
                        tree.expand(folder);
                    }
                }
            }
        }
    }

    protected static abstract class FolderAction extends Action {

        public FolderAction(String caption) {
            super(caption);
        }

        public abstract void perform(Folder folder);
    }

    protected class OpenAction extends FolderAction {

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
                    () -> {
                        saveFolder(newFolder);
                        refreshFolders();
                    });
            AppUI.getCurrent().addWindow(window);
        }
    }

    protected class CopyAction extends FolderAction {

        public CopyAction() {
            super(messages.getMainMessage("folders.copyFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            AbstractSearchFolder newFolder = (AbstractSearchFolder) metadata.create(folder.getMetaClass());
            newFolder.copyFrom((AbstractSearchFolder) folder);
            new EditAction().perform(newFolder);
        }
    }

    protected class EditAction extends FolderAction {

        public EditAction() {
            super(messages.getMainMessage("folders.editFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            final FolderEditWindow window;
            if (folder instanceof SearchFolder) {
                window = AppFolderEditWindow.create(false, false, folder, null, () -> {
                    saveFolder(folder);
                    refreshFolders();
                });
            } else {
                if (folder instanceof AppFolder) {
                    window = AppFolderEditWindow.create(true, false, folder, null, () -> {
                        saveFolder(folder);
                        refreshFolders();
                    });
                } else {
                    return;
                }
            }
            AppUI.getCurrent().addWindow(window);
        }
    }

    protected class RemoveAction extends FolderAction {

        public RemoveAction() {
            super(messages.getMainMessage("folders.removeFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            App.getInstance().getWindowManager().showOptionDialog(
                    messages.getMainMessage("dialogs.Confirmation"),
                    messages.getMainMessage("folders.removeFolderConfirmation"),
                    Frame.MessageType.CONFIRMATION,
                    new com.haulmont.cuba.gui.components.Action[]{
                            new DialogAction(Type.YES).withHandler(event -> {
                                removeFolder(folder);
                                refreshFolders();
                            }),
                            new DialogAction(Type.NO, Status.PRIMARY)
                    }
            );
        }
    }

    protected class ExportAction extends FolderAction {

        public ExportAction() {
            super(messages.getMainMessage("folders.exportFolderAction"));
        }

        @Override
        public void perform(Folder folder) {
            try {
                byte[] data = foldersService.exportFolder(folder);
                AppBeans.get(WebExportDisplay.class).show(new ByteArrayDataProvider(data), "Folders", ExportFormat.ZIP);
            } catch (IOException ignored) {
            }
        }
    }

    protected class ImportAction extends FolderAction {

        public ImportAction() {
            super(messages.getMainMessage("folders.importFolderAction"));
        }

        @Override
        public void perform(final Folder folder) {
            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

            WindowManager wm = App.getInstance().getWindowManager();
            WindowInfo windowInfo = windowConfig.getWindowInfo("fileUploadDialog");
            FileUploadDialog dialog = (FileUploadDialog) wm.openWindow(windowInfo, OpenType.DIALOG);

            dialog.addCloseListener(actionId -> {
                if (COMMIT_ACTION_ID.equals(actionId)) {
                    try {
                        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
                        byte[] data = FileUtils.readFileToByteArray(fileUploading.getFile(dialog.getFileId()));
                        fileUploading.deleteFile(dialog.getFileId());
                        foldersService.importFolder(folder, data);
                    } catch (AccessDeniedException ex) {
                        throw ex;
                    } catch (Exception ex) {
                        dialog.showNotification(
                                messages.getMainMessage("folders.importFailedNotification"),
                                ex.getMessage(),
                                Frame.NotificationType.ERROR
                        );
                    }
                    refreshFolders();
                }
            });
        }
    }

    public class AppFolderUpdateBackgroundTask extends BackgroundTask<Integer, List<AppFolder>> {

        public AppFolderUpdateBackgroundTask(long timeoutSeconds) {
            super(timeoutSeconds);
        }

        @Override
        public List<AppFolder> run(TaskLifeCycle<Integer> taskLifeCycle) {
            return getReloadedFolders();
        }

        @Override
        public void done(List<AppFolder> reloadedFolders) {
            reloadParentFolders(reloadedFolders);
            updateFolders(reloadedFolders);
        }
    }

    public class AppFoldersUpdater implements Consumer<CubaTimer> {
        @Override
        public void accept(CubaTimer cubaTimer) {
            reloadAppFolders();
        }
    }

    // used for instance of to detect folders pane timer
    protected static class FoldersPaneTimer extends CubaTimer {
    }

    protected static class FoldersDataProvider<T extends Folder> extends TreeDataProvider<T>
            implements EnhancedTreeDataProvider<T> {

        public FoldersDataProvider(TreeData<T> treeData) {
            super(treeData);
        }

        @Override
        public Stream<T> getItems() {
            return getChildrenRecursively(null);
        }

        protected Stream<T> getChildrenRecursively(T parent) {
            Supplier<Stream<T>> children = () -> getChildren(parent);
            Stream<T> items = children.get().flatMap((SerializableFunction<T, Stream<T>>)
                    this::getChildrenRecursively);

            return Stream.concat(children.get(), items);
        }

        protected Stream<T> getChildren(T item) {
            return fetchChildren(new HierarchicalQuery<>(null, item));
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getParent(T item) {
            return (T) item.getParent();
        }
    }

    protected class FolderGridContextMenu<T extends Folder> extends CubaGridContextMenu<T> {

        protected OpenAction openAction = createOpenAction();
        protected CreateAction createAction = createCreateAction();
        protected CopyAction copyAction = createCopyAction();
        protected EditAction editAction = createEditAction();
        protected RemoveAction removeAction = createRemoveAction();
        protected ExportAction exportAction = createExportAction();
        protected ImportAction importAction = createImportAction();

        public FolderGridContextMenu(Grid<T> parentComponent) {
            super(parentComponent);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Grid<T> getParent() {
            return (Grid<T>) super.getParent();
        }

        protected OpenAction createOpenAction() {
            return new OpenAction();
        }

        protected CreateAction createCreateAction() {
            return new CreateAction(true);
        }

        protected CopyAction createCopyAction() {
            return new CopyAction();
        }

        protected EditAction createEditAction() {
            return new EditAction();
        }

        protected RemoveAction createRemoveAction() {
            return new RemoveAction();
        }

        protected ExportAction createExportAction() {
            return new ExportAction();
        }

        protected ImportAction createImportAction() {
            return new ImportAction();
        }

        protected Command createCommand(FolderAction action) {
            return new CommandFolderActionAdapter(action, this::getSelectedFolder);
        }

        protected T getSelectedFolder() {
            return getParent().asSingleSelect().getValue();
        }

        public FolderGridContextMenu<T> addOpenAction() {
            addItem(openAction.getCaption(), createCommand(openAction));
            return this;
        }

        public FolderGridContextMenu<T> addCreateAction() {
            addItem(createAction.getCaption(), createCommand(createAction));
            return this;
        }

        public FolderGridContextMenu<T> addCopyAction() {
            addItem(copyAction.getCaption(), createCommand(copyAction));
            return this;
        }

        public FolderGridContextMenu<T> addEditAction() {
            addItem(editAction.getCaption(), createCommand(editAction));
            return this;
        }

        public FolderGridContextMenu<T> addRemoveAction() {
            addItem(removeAction.getCaption(), createCommand(removeAction));
            return this;
        }

        public FolderGridContextMenu<T> addExportAction() {
            addItem(exportAction.getCaption(), createCommand(exportAction));
            return this;
        }

        public FolderGridContextMenu<T> addImportAction() {
            addItem(importAction.getCaption(), createCommand(importAction));
            return this;
        }

        protected void createAllActions() {
            addOpenAction();
            addCopyAction();
            addEditAction();
            addCreateAction();
            addRemoveAction();
            addExportAction();
            addImportAction();
        }
    }

    protected class  AppFolderGridContextMenu<T extends AppFolder> extends FolderGridContextMenu<T> {

        public AppFolderGridContextMenu(Grid<T> parentComponent) {
            super(parentComponent);
        }

        @Override
        protected void onContextClick(ContextClickEvent event) {
            removeItems();

            if (event instanceof Grid.GridContextClickEvent
                    && ((Grid.GridContextClickEvent) event).getItem() != null) {
                //noinspection unchecked
                T item = ((Grid.GridContextClickEvent<T>) event).getItem();
                // Context Click doesn't trigger selection, so we do it programmatically.
                getParent().select(item);

                if (isGlobalAppFolderPermitted()) {
                    createAllActions();
                } else {
                    addOpenAction();
                }
            } else if (isGlobalAppFolderPermitted()) {
                addImportAction();
            }

            super.onContextClick(event);
        }

        protected boolean isGlobalAppFolderPermitted() {
            return userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.appFolder.global");
        }
    }

    protected class SearchFolderGridContextMenu<T extends SearchFolder> extends FolderGridContextMenu<T> {

        public SearchFolderGridContextMenu(Grid<T> grid) {
            super(grid);
        }

        @Override
        protected CreateAction createCreateAction() {
            return new CreateAction(false);
        }

        @Override
        protected void onContextClick(ContextClickEvent event) {
            removeItems();

            if (event instanceof Grid.GridContextClickEvent
                    && ((Grid.GridContextClickEvent) event).getItem() != null) {
                //noinspection unchecked
                T item = ((Grid.GridContextClickEvent<T>) event).getItem();
                // Context Click doesn't trigger selection, so we do it programmatically.
                getParent().select(item);

                if (isGlobalFolder(item)) {
                    if (isFilterFolder(item)) {
                        if (isGlobalSearchFolderPermitted()) {
                            createAllActions();
                        } else {
                            createOpenCreateActions();
                        }
                    } else {
                        if (isGlobalSearchFolderPermitted()) {
                            createWithoutOpenActions();
                        } else {
                            createOnlyCreateAction();
                        }
                    }
                } else {
                    if (isFilterFolder(item)) {
                        if (isOwner(item)) {
                            createAllActions();
                        } else {
                            createOpenCreateActions();
                        }
                    } else {
                        if (isOwner(item)) {
                            createWithoutOpenActions();
                        } else {
                            createOnlyCreateAction();
                        }
                    }
                }
            } else {
                createImportCreateActions();
            }

            super.onContextClick(event);
        }

        protected boolean isGlobalFolder(SearchFolder folder) {
            return (folder.getUser() == null);
        }

        protected boolean isFilterFolder(SearchFolder folder) {
            return (folder.getFilterComponentId() != null);
        }

        protected boolean isOwner(SearchFolder folder) {
            return userSessionSource.getUserSession().getCurrentOrSubstitutedUser().equals(folder.getUser());
        }

        protected boolean isGlobalSearchFolderPermitted() {
            return userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.searchFolder.global");
        }

        protected void createOnlyCreateAction() {
            addCreateAction();
        }

        protected void createOpenCreateActions() {
            addOpenAction();
            addCreateAction();
            addCopyAction();
        }

        protected void createWithoutOpenActions() {
            addCreateAction();
            addEditAction();
            addRemoveAction();
        }

        protected void createImportCreateActions() {
            addCreateAction();
            addImportAction();
        }
    }

    protected static class CommandFolderActionAdapter implements Command {

        protected final FolderAction action;
        protected final Supplier<Folder> selectedFolderProvider;

        public CommandFolderActionAdapter(FolderAction action,
                                          Supplier<Folder> selectedFolderProvider) {
            this.action = action;
            this.selectedFolderProvider = selectedFolderProvider;
        }

        @Override
        public void menuSelected(MenuItem selectedItem) {
            Folder folder = selectedFolderProvider.get();
            action.perform(folder);
        }
    }

    public static class RefreshFoldersEvent extends EventObject {
        public RefreshFoldersEvent(CubaFoldersPane source) {
            super(source);
        }

        @Override
        public CubaFoldersPane getSource() {
            return (CubaFoldersPane) super.getSource();
        }
    }

    public interface RefreshFoldersListener extends Serializable {

        void refreshFolders(RefreshFoldersEvent event);
    }
}