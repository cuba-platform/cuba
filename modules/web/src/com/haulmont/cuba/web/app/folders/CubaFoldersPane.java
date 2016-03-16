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
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.file.FileUploadDialog;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.config.WindowConfig;
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
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaTimer;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static com.haulmont.cuba.gui.components.Window.COMMIT_ACTION_ID;

/**
 * Left panel containing application and search folders.
 *
 */
public class CubaFoldersPane extends VerticalLayout {

    private static final long serialVersionUID = 6666603397626574763L;

    protected boolean visible;

    protected Tree appFoldersTree;
    protected Tree searchFoldersTree;

    protected Label appFoldersLabel;
    protected Label searchFoldersLabel;

    protected Object appFoldersRoot;
    protected Object searchFoldersRoot;
    protected FoldersPaneTimer timer;

    protected static final int DEFAULT_VERT_SPLIT_POS = 50;
    protected int verticalSplitPos;

    protected VerticalSplitPanel vertSplit;

    protected WebConfig webConfig = AppBeans.<Configuration>get(Configuration.NAME).getConfig(WebConfig.class);

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected UserSettingService userSettingService = AppBeans.get(UserSettingService.NAME);

    protected FoldersService foldersService = AppBeans.get(FoldersService.NAME);

    protected DataService dataService = AppBeans.get(DataService.NAME);

    protected UserSettingsTools userSettingsTools = AppBeans.get(UserSettingsTools.NAME);

    protected Folders folders = AppBeans.get(Folders.NAME);

    protected BackgroundTaskWrapper<Integer, List<AppFolder>> folderUpdateBackgroundTaskWrapper;

    public CubaFoldersPane() {
        setSizeFull();
        setMargin(false);
        setSpacing(true);

        setStyleName("cuba-folders-pane");
        //noinspection unchecked
        folderUpdateBackgroundTaskWrapper = new BackgroundTaskWrapper(new AppFolderUpdateBackgroundTask(10));
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
    }

    protected void setupSearchFoldersPane(Component searchFoldersPane) {
        searchFoldersPane.setHeight("100%");
        searchFoldersPane.setWidth("100%");
        if (isNeedFoldersTitle()) {
            searchFoldersLabel = new Label(messages.getMainMessage("folders.searchFoldersRoot"));
            searchFoldersLabel.setStyleName("cuba-folders-pane-caption");
        } else {
            searchFoldersLabel = null;
        }
    }

    protected void setupUpdateTimer() {
        int period = webConfig.getAppFoldersRefreshPeriodSec() * 1000;

        AppWindow appWindow = App.getInstance().getAppWindow();
        for (CubaTimer t : appWindow.getTimers()) {
            if (t instanceof FoldersPaneTimer) {
                t.stop();
            }
        }

        timer = new FoldersPaneTimer();
        timer.setRepeating(true);
        timer.setDelay(period);
        timer.addActionListener(createAppFolderUpdater());
        timer.start();

        appWindow.addTimer(timer);
    }

    protected void setupAppFoldersPane(Component appFoldersPane) {
        appFoldersPane.setHeight("100%");
        appFoldersPane.setWidth("100%");
        if (isNeedFoldersTitle()) {
            appFoldersLabel = new Label(messages.getMainMessage("folders.appFoldersRoot"));
            appFoldersLabel.setStyleName("cuba-folders-pane-caption");
        } else {
            appFoldersLabel = null;
        }
    }

    protected void collapseItemInTree(Tree tree, final String foldersCollapse) {
        String s = userSettingService.loadSetting(foldersCollapse);
        List<UUID> idFolders = strToIds(s);
        //noinspection unchecked, RedundantCast
        for (AbstractSearchFolder folder : (Collection<AbstractSearchFolder>) tree.getItemIds()) {
            if (idFolders.contains(folder.getId())) {
                tree.collapseItem(folder);
            }
        }
        tree.addExpandListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent event) {
                if (event.getItemId() instanceof AbstractSearchFolder) {
                    UUID uuid = ((AbstractSearchFolder) event.getItemId()).getId();
                    String str = userSettingService.loadSetting(foldersCollapse);
                    userSettingService.saveSetting(foldersCollapse, removeIdInStr(str, uuid));
                }
            }
        });
        tree.addCollapseListener(new Tree.CollapseListener() {
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
        ArrayList<UUID> uuids = new ArrayList<>();
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
        l.setMargin(new MarginInfo(false, true, false, true));
        l.addComponent(label);
        l.setWidth("100%");
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

    protected CubaTimer.ActionListener createAppFolderUpdater() {
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
        //noinspection unchecked
        Collection<AppFolder> childFolders = (Collection<AppFolder>) appFoldersTree.getChildren(parentFolder);
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
        @SuppressWarnings("unchecked")
        List<AppFolder> folders = new ArrayList(appFoldersTree.getItemIds());
        FoldersService service = AppBeans.get(FoldersService.NAME);
        return service.reloadAppFolders(folders);
    }

    protected void updateFolders(List<AppFolder> reloadedFolders) {
        @SuppressWarnings("unchecked")
        List<AppFolder> folders = new ArrayList(appFoldersTree.getItemIds());
        for (AppFolder folder : reloadedFolders) {
            int index = reloadedFolders.indexOf(folder);
            AppFolder f = folders.get(index);
            if (f != null) {
                f.setItemStyle(folder.getItemStyle());
                f.setQuantity(folder.getQuantity());
            }
            setFolderTreeItemCaption(appFoldersTree, folder);
        }
    }

    protected void adjustLayout() {
    }

    protected Component createAppFoldersPane() {
        List<AppFolder> appFolders = foldersService.loadAppFolders();
        if (appFolders.isEmpty())
            return null;

        appFoldersTree = new CubaTree();
        appFoldersTree.setCubaId("appFoldersTree");
        appFoldersTree.setSelectable(false);
        appFoldersTree.setItemStyleGenerator(new FolderTreeStyleProvider());
        appFoldersTree.addExpandListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent event) {
                AppFolder folder = (AppFolder) event.getItemId();
                if (StringUtils.isBlank(folder.getQuantityScript())) {
                    folder.setQuantity(null);
                    folder.setItemStyle(null);
                    setFolderTreeItemCaption(appFoldersTree, folder);
                }
            }
        });
        appFoldersTree.addCollapseListener(new Tree.CollapseListener() {
            @Override
            public void nodeCollapse(Tree.CollapseEvent event) {
                AppFolder folder = (AppFolder) event.getItemId();
                if (StringUtils.isBlank(folder.getQuantityScript())) {
                    reloadSingleParentFolder(folder, null);
                    setFolderTreeItemCaption(appFoldersTree, folder);
                }
            }
        });

        appFoldersRoot = messages.getMainMessage("folders.appFoldersRoot");
        fillTree(appFoldersTree, appFolders, isNeedRootAppFolder() ? appFoldersRoot : null);
        appFoldersTree.addItemClickListener(new FolderClickListener());
        appFoldersTree.addActionHandler(new AppFolderActionsHandler());

        for (Object itemId : appFoldersTree.rootItemIds()) {
            appFoldersTree.expandItemsRecursively(itemId);
        }

        return appFoldersTree;
    }

    protected <T extends Folder> void setFolderTreeItemCaption(Tree tree, T folder) {
        tree.setItemCaption(folder, folder.getCaption());
    }

    protected Component createSearchFoldersPane() {
        searchFoldersTree = new CubaTree();
        searchFoldersTree.setCubaId("searchFoldersTree");
        searchFoldersTree.setSelectable(false);
        searchFoldersTree.setItemStyleGenerator(new FolderTreeStyleProvider());

        List<SearchFolder> searchFolders = foldersService.loadSearchFolders();
        searchFoldersRoot = messages.getMainMessage("folders.searchFoldersRoot");
        searchFoldersTree.addItemClickListener(new FolderClickListener());
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
            setFolderTreeItemCaption(tree, folder);
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

    protected void openFolder(AbstractSearchFolder folder) {
        folders.openFolder(folder);
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

    public Tree getSearchFoldersTree() {
        return searchFoldersTree;
    }

    public Tree getAppFoldersTree() {
        return appFoldersTree;
    }

    public Collection<SearchFolder> getSearchFolders() {
        if (searchFoldersTree == null) {
            return Collections.emptyList();
        } else {
            @SuppressWarnings("unchecked")
            List result = new ArrayList(searchFoldersTree.getItemIds());
            result.remove(searchFoldersRoot);
            //noinspection unchecked
            return result;
        }
    }

    protected boolean getItemClickable(Folder folder) {
        return folder instanceof AbstractSearchFolder
                        && !StringUtils.isBlank(((AbstractSearchFolder) folder).getFilterComponentId());
    }

    protected boolean isItemExpandable(Folder folder) {
        return folder instanceof AbstractSearchFolder &&
                StringUtils.isBlank(((AbstractSearchFolder) folder).getFilterComponentId());
    }

    protected class FolderTreeStyleProvider implements Tree.ItemStyleGenerator {
        @Override
        public String getStyle(Tree source, Object itemId) {
            Folder folder = ((Folder) itemId);
            if (folder != null) {
                String style;
                // clickable tree item
                if (getItemClickable(folder))
                    style = "cuba-clickable-folder";
                else
                    style = "cuba-nonclickable-folder";
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
            if (event.getButton() == MouseEventDetails.MouseButton.LEFT) {
                Folder folder = (Folder) event.getItemId();
                if (getItemClickable(folder)) {
                    openFolder((AbstractSearchFolder) event.getItemId());
                } else if (isItemExpandable(folder)) {
                    Component tree = event.getComponent();
                    if (tree instanceof Tree) {
                        if (((Tree) tree).isExpanded(folder))
                            ((Tree) tree).collapseItem(folder);
                        else
                            ((Tree) tree).expandItem(folder);
                    }
                }
            }
        }
    }

    protected class AppFolderActionsHandler implements Action.Handler {
        protected OpenAction openAction = new OpenAction();
        protected CreateAction createAction = new CreateAction(true);
        protected CopyAction copyAction = new CopyAction();
        protected EditAction editAction = new EditAction();
        protected RemoveAction removeAction = new RemoveAction();
        protected ExportAction exportAction = new ExportAction();
        protected ImportAction importAction = new ImportAction();

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

        protected OpenAction openAction = new OpenAction();
        protected CopyAction copyAction = new CopyAction();
        protected CreateAction createAction = new CreateAction(false);
        protected EditAction editAction = new EditAction();
        protected RemoveAction removeAction = new RemoveAction();
        protected ExportAction exportAction = new ExportAction();
        protected ImportAction importAction = new ImportAction();

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

        protected boolean isGlobalFolder(SearchFolder folder) {
            return (folder.getUser() == null);
        }

        protected boolean isFilterFolder(SearchFolder folder) {
            return (folder.getFilterComponentId() != null);
        }

        protected boolean isOwner(SearchFolder folder) {
            return userSessionSource.getUserSession().getUser().equals(folder.getUser());
        }

        protected boolean isGlobalSearchFolderPermitted() {
            return (userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.searchFolder.global"));
        }

        protected Action[] createAllActions() {
            return new Action[] {openAction, copyAction, createAction,
                    editAction, removeAction, exportAction, importAction};
        }

        protected Action[] createWithoutOpenActions() {
            return new Action[] {createAction, editAction, removeAction};
        }

        protected Action[] createOnlyCreateAction() {
            return new Action[] {createAction};
        }

        protected Action[] createOpenCreateAction() {
            return new Action[] {openAction, createAction, copyAction};
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
                    new Runnable() {
                        @Override
                        public void run() {
                            saveFolder(newFolder);
                            refreshFolders();
                        }
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
                            new DialogAction(Type.YES) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    removeFolder(folder);
                                    refreshFolders();
                                }
                            },
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
                new WebExportDisplay().show(new ByteArrayDataProvider(data), "Folders", ExportFormat.ZIP);
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

            final FileUploadDialog dialog = (FileUploadDialog) App.getInstance().getWindowManager().
                    openWindow(windowConfig.getWindowInfo("fileUploadDialog"), WindowManager.OpenType.DIALOG);

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
        public List<AppFolder> run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
            return getReloadedFolders();
        }

        @Override
        public void done(List<AppFolder> reloadedFolders) {
            reloadParentFolders(reloadedFolders);
            updateFolders(reloadedFolders);
        }
    }

    public class AppFoldersUpdater implements CubaTimer.ActionListener {
        @Override
        public void timerAction(CubaTimer timer) {
            reloadAppFolders();
        }
    }

    // used for instance of to detect folders pane timer
    protected static class FoldersPaneTimer extends CubaTimer {
    }
}