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
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.UserSettingHelper;
import com.haulmont.cuba.web.gui.components.WebSplitPanel;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.List;

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

    protected static final int DEFAULT_PANE_WIDTH = 200;
    protected static final int DEFAULT_VERT_SPLIT_POS = 400;
    protected int horizontalSplitPos;
    protected int verticalSplitPos;

    protected AppWindow parentAppWindow;
    protected WebSplitPanel vertSplit;
    protected WebSplitPanel horSplit;

    public FoldersPane(MenuBar menuBar, AppWindow appWindow) {
        this.menuBar = menuBar;
        messagesPack = AppConfig.getInstance().getMessagesPack();
        parentAppWindow = appWindow;

        setHeight(100, Sizeable.UNITS_PERCENTAGE);
        setStyleName("folderspane");
    }

    public void init(Component parent) {
        if (parent instanceof WebSplitPanel) {
            horSplit = (WebSplitPanel) parent;
        }

        boolean nowVisible;
        UserSettingHelper.FoldersState state = UserSettingHelper.loadFoldersState();
        if (state == null) {
            nowVisible = false;
            String paneWidthStr = AppContext.getProperty("cuba.foldersPane.width");
            horizontalSplitPos = paneWidthStr == null ? DEFAULT_PANE_WIDTH : Integer.parseInt(paneWidthStr);
            verticalSplitPos = DEFAULT_VERT_SPLIT_POS;
        } else {
            nowVisible = state.visible;
            horizontalSplitPos = state.horizontalSplit;
            verticalSplitPos = state.verticalSplit;
        }

        showFolders(nowVisible);

        MenuBar.MenuItem firstItem = menuBar.getItems().isEmpty() ? null : menuBar.getItems().get(0);
        menuItem = menuBar.addItemBefore(getMenuItemCaption(),
                null,
                new MenuBar.Command() {
                    public void menuSelected(MenuBar.MenuItem selectedItem) {
                        showFolders(!visible);
                        menuItem.setText(getMenuItemCaption());
                    }
                },
                firstItem);
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
                appFoldersPane.setHeight("100%");
                appFoldersPane.setWidth("100%");
                if (isNeedFoldersTitle()) {
                    appFoldersLabel = new Label(MessageProvider.getMessage(messagesPack, "folders.appFoldersRoot"));
                    appFoldersLabel.setStyleName("folderspane-caption");
                } else {
                    appFoldersLabel = null;
                }

                int period = ConfigProvider.getConfig(WebConfig.class).getAppFoldersRefreshPeriodSec() * 1000;
                timer = new Timer(period, true);
                timer.addListener(createAppFolderUpdater());
                App.getInstance().addTimer(timer, parentAppWindow);
            }

            Component searchFoldersPane = createSearchFoldersPane();
            if (searchFoldersPane != null) {
                searchFoldersPane.setHeight("100%");
                searchFoldersPane.setWidth("100%");
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
                afLayout.setSizeFull();
                if (appFoldersLabel != null)
                    addFoldersLabel(afLayout, appFoldersLabel);
                afLayout.addComponent(appFoldersPane);
                afLayout.setExpandRatio(appFoldersPane, 1);
                vertSplit.setFirstComponent(afLayout);

                VerticalLayout sfLayout = new VerticalLayout();
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

    private void addFoldersLabel(AbstractLayout layout, Label label) {
        HorizontalLayout l = new HorizontalLayout();
        l.setMargin(false, true, false, true);
        l.addComponent(label);
        layout.addComponent(l);
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

        List<AppFolder> folders = new ArrayList<AppFolder>(appFoldersTree.getItemIds());
        FoldersService service = ServiceLocator.lookup(FoldersService.NAME);
        service.reloadAppFolders(folders);
        for (AppFolder folder : folders) {
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
        appFoldersTree.setDoubleClickMode(true);

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
        searchFoldersTree.setDoubleClickMode(true);

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
        return MessageProvider.getMessage(messagesPack, visible ? "folders.hideFolders" : "folders.showFolders");
    }

    protected void openFolder(AbstractSearchFolder folder) {
        if (StringUtils.isBlank(folder.getFilterComponentId())) {
            log.warn("Unable to open folder: componentId is blank");
            return;
        }

        String[] strings = ValuePathHelper.parse(folder.getFilterComponentId());
        String screenId = strings[0];

        WindowInfo windowInfo = AppConfig.getInstance().getWindowConfig().getWindowInfo(screenId);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("disableAutoRefresh", true);
        params.put("description", MessageProvider.getMessage(messagesPack, folder.getName()));

        Window window = App.getInstance().getWindowManager().openWindow(windowInfo,
                WindowManager.OpenType.NEW_TAB, params);

        if (strings.length > 1) {
            String filterComponentId = StringUtils.join(Arrays.copyOfRange(strings, 1, strings.length), '.');
            Filter filterComponent = window.getComponent(filterComponentId);

            FilterEntity filterEntity = new FilterEntity();
            filterEntity.setIsTemporary(true);
            filterEntity.setComponentId(folder.getFilterComponentId());
            if (folder instanceof AppFolder)
                filterEntity.setName(((AppFolder) folder).getLocName());
            else
                filterEntity.setName(folder.getName());
            filterEntity.setXml(folder.getFilterXml());

            filterComponent.setFilterEntity(filterEntity);
        }
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

    public void saveFolder(Folder folder) {
        CommitContext commitContext = new CommitContext(Collections.singleton(folder));
        ServiceLocator.getDataService().commit(commitContext);
    }

    protected void removeFolder(Folder folder) {
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
            List result = new ArrayList<SearchFolder>(searchFoldersTree.getItemIds());
            result.remove(searchFoldersRoot);
            return result;
        }
    }

    protected class FolderClickListener implements ItemClickEvent.ItemClickListener {
        public FolderClickListener() {
        }

        public void itemClick(ItemClickEvent event) {
            if (event.getItemId() instanceof AbstractSearchFolder
                    && !StringUtils.isBlank(((AbstractSearchFolder) event.getItemId()).getFilterComponentId())) {
                if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
                    if (appFoldersTree.containsId(event.getItemId()))
                        appFoldersTree.select(event.getItemId());
                    else if (searchFoldersTree.containsId(event.getItemId()))
                        searchFoldersTree.select(event.getItemId());
                } else {
                    openFolder((AbstractSearchFolder) event.getItemId());
                }
            }
        }
    }

    protected class AppFolderActionsHandler implements Action.Handler {
        public AppFolderActionsHandler() {
        }

        public Action[] getActions(Object target, Object sender) {
            if (target instanceof Folder)
                return new Action[]{new OpenAction()};
            else
                return null;
        }

        public void handleAction(Action action, Object sender, Object target) {
            if (target instanceof Folder)
                ((FolderAction) action).perform((Folder) target);
            else
                ((FolderAction) action).perform(null);
        }
    }

    protected class SearchFolderActionsHandler extends AppFolderActionsHandler {

        public Action[] getActions(Object target, Object sender) {
            if (target instanceof SearchFolder) {
                if (StringUtils.isBlank(((SearchFolder) target).getFilterComponentId()))
                    return new Action[]{new CreateAction(), new EditAction(), new RemoveAction()};
                else
                    return new Action[]{new OpenAction(), new CreateAction(), new EditAction(), new RemoveAction()};
            } else
                return new Action[]{new CreateAction()};
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

        public void perform(Folder folder) {
            if (folder instanceof AbstractSearchFolder)
                openFolder((AbstractSearchFolder) folder);
        }
    }

    protected class CreateAction extends FolderAction {

        public CreateAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.createFolderAction"));
        }

        public void perform(final Folder folder) {
            final SearchFolder newFolder = new SearchFolder();
            newFolder.setName("");
            newFolder.setParent(folder);
            newFolder.setUser(UserSessionClient.getUserSession().getUser());
            final FolderEditWindow window = new FolderEditWindow(true, newFolder, new Runnable() {
                public void run() {
                    saveFolder(newFolder);
                    refreshFolders();
                }
            });
            window.addListener(new com.vaadin.ui.Window.CloseListener() {
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    App.getInstance().getAppWindow().removeWindow(window);
                }
            });
            App.getInstance().getAppWindow().addWindow(window);
        }
    }

    protected class EditAction extends FolderAction {

        public EditAction() {
            super(MessageProvider.getMessage(messagesPack, "folders.editFolderAction"));
        }

        public void perform(final Folder folder) {
            final FolderEditWindow window = new FolderEditWindow(false, folder, new Runnable() {
                public void run() {
                    saveFolder(folder);
                    refreshFolders();
                }
            });
            window.addListener(new com.vaadin.ui.Window.CloseListener() {
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

    public class AppFoldersUpdater implements Timer.Listener {

        public void onTimer(Timer timer) {
            reloadAppFolders();
        }

        public void onStopTimer(Timer timer) {
        }
    }
}
