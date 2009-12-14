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
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.toolkit.Timer;
import com.haulmont.cuba.web.app.UserSettingHelper;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Component;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.Action;

import java.util.*;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class FoldersPane extends VerticalLayout {

    private Log log = LogFactory.getLog(FoldersPane.class);

    protected String messagesPack;
    protected FoldersService service;

    protected boolean visible;

    protected Tree appFoldersTree;
    protected Tree searchFoldersTree;

    protected MenuBar.MenuItem menuItem;

    protected Object appFoldersRoot;
    protected Object searchFoldersRoot;
    private Timer timer;

    public FoldersPane(MenuBar menuBar) {
        messagesPack = AppConfig.getInstance().getMessagesPack();
        service = ServiceLocator.lookup(FoldersService.JNDI_NAME);

        setHeight(100, Sizeable.UNITS_PERCENTAGE);

        showFolders(UserSettingHelper.loadFoldersVisibleState());

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
            setWidth(200, Sizeable.UNITS_PIXELS);
            setMargin(true, false, true, true);
            setSpacing(true);

            Component appFoldersPane = createAppFoldersPane();
            if (appFoldersPane != null) {
                addComponent(appFoldersPane);

                int period = ConfigProvider.getConfig(WebConfig.class).getAppFoldersRefreshPeriodSec() * 1000;
                timer = new Timer(period, true);
                timer.addListener(new AppFoldersUpdater());
                App.getInstance().addTimer(timer);
            }

            Component searchFoldersPane = createSearchFoldersPane();
            if (searchFoldersPane != null)
                addComponent(searchFoldersPane);

            adjustLayout();

            if (getParent() != null)
                getParent().requestRepaint();

        } else {
            if (timer != null)
                timer.stopTimer();

            removeAllComponents();
            setWidth(0, Sizeable.UNITS_PIXELS);
            setMargin(false);

            appFoldersTree = null;
            searchFoldersTree = null;
        }

        visible = show;
        UserSettingHelper.saveFoldersVisibleState(visible);        
    }

    public void refreshFolders() {
        if (visible) {
            showFolders(false);
            showFolders(true);
        }
    }

    protected void adjustLayout() {
        if (appFoldersTree != null)
            setExpandRatio(appFoldersTree, 1);

        if (searchFoldersTree != null)
            setExpandRatio(searchFoldersTree, 1);
    }

    protected Component createAppFoldersPane() {
        List<AppFolder> appFolders = service.loadAppFolders();
        if (appFolders.isEmpty())
            return null;

        appFoldersTree = new Tree();
        appFoldersTree.setSizeFull();

        appFoldersRoot = MessageProvider.getMessage(messagesPack, "folders.appFoldersRoot");
        fillTree(appFoldersTree, appFolders, appFoldersRoot);
        appFoldersTree.addListener(new FolderClickListener());
        appFoldersTree.addActionHandler(new AppFolderActionsHandler());

        for (Object itemId : appFoldersTree.rootItemIds()) {
            appFoldersTree.expandItemsRecursively(itemId);
        }
        return appFoldersTree;
    }

    protected Component createSearchFoldersPane() {
        searchFoldersTree = new Tree();
        searchFoldersTree.setSizeFull();

        List<SearchFolder> searchFolders = service.loadSearchFolders();
        searchFoldersRoot = MessageProvider.getMessage(messagesPack, "folders.searchFoldersRoot");
        searchFoldersTree.addItem(searchFoldersRoot);
        searchFoldersTree.addListener(new FolderClickListener());
        searchFoldersTree.addActionHandler(new SearchFolderActionsHandler());
        if (!searchFolders.isEmpty()) {
            fillTree(searchFoldersTree, searchFolders, searchFoldersRoot);
        }

        for (Object itemId : searchFoldersTree.rootItemIds()) {
            searchFoldersTree.expandItemsRecursively(itemId);
        }
        return searchFoldersTree;
    }

    protected void fillTree(Tree tree, List<? extends Folder> folders, Object rootItemId) {
        tree.addItem(rootItemId);
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
        Window window = App.getInstance().getWindowManager().openWindow(windowInfo, WindowManager.OpenType.NEW_TAB);

        if (strings.length > 1) {
            String filterComponentId = StringUtils.join(Arrays.copyOfRange(strings, 1, strings.length), '.');
            Filter filterComponent = window.getComponent(filterComponentId);

            FilterEntity filterEntity = new FilterEntity();
            filterEntity.setIsTemporary(true);
            filterEntity.setComponentId(folder.getFilterComponentId());
            filterEntity.setName(folder.getName());
            filterEntity.setXml(folder.getFilterXml());

            filterComponent.setFilterEntity(filterEntity);
        }
    }

    public void saveFolder(Folder folder) {
        CommitContext commitContext = new CommitContext(Collections.singleton(folder));
        ServiceLocator.getDataService().commit(commitContext);
    }

    protected void removeFolder(Folder folder) {
        CommitContext commitContext = new CommitContext(Collections.emptySet(), Collections.singleton(folder));
        ServiceLocator.getDataService().commit(commitContext);
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

        public void itemClick(ItemClickEvent event) {
            if (!event.isDoubleClick())
                return;

            if (event.getItemId() instanceof AbstractSearchFolder
                    && !StringUtils.isBlank(((AbstractSearchFolder) event.getItemId()).getFilterComponentId()))
            {
                openFolder((AbstractSearchFolder) event.getItemId());
            }
        }
    }

    protected class AppFolderActionsHandler implements Action.Handler {

        public Action[] getActions(Object target, Object sender) {
            if (target instanceof Folder)
                return new Action[] { new OpenAction() };
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
                    return new Action[] { new CreateAction(), new EditAction(), new RemoveAction() };
                else
                    return new Action[] { new OpenAction(), new CreateAction(), new EditAction(), new RemoveAction() };
            } else
                return new Action[] { new CreateAction() };
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
                    new com.haulmont.cuba.gui.components.Action[] {
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

    protected class AppFoldersUpdater implements Timer.Listener {

        public void onTimer(Timer timer) {
            if (appFoldersTree == null)
                return;

            synchronized (FoldersPane.this) {
                appFoldersTree.removeAllItems();

                List<AppFolder> appFolders = service.loadAppFolders();
                if (!appFolders.isEmpty()) {
                    fillTree(appFoldersTree, appFolders, appFoldersRoot);
                    for (Object itemId : appFoldersTree.rootItemIds()) {
                        appFoldersTree.expandItemsRecursively(itemId);
                    }
                }

                appFoldersTree.requestRepaint();
            }
        }

        public void onStopTimer(Timer timer) {
        }
    }
}
