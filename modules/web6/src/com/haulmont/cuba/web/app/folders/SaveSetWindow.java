/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.UserSetHelper;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;

import java.util.Map;
import java.util.Set;

/**
 * @author devyatkin
 * @version $Id$
 */
public class SaveSetWindow extends AbstractWindow {

    private Set ids;
    private String componentPath;
    private String componentId;
    private String entityType;
    private String entityClass;
    private FoldersPane foldersPane;
    private LookupField foldersSelect;
    private String query;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        ids = (Set) params.get("items");
        componentPath = (String) params.get("componentPath");
        componentId = (String) params.get("componentId");
        entityType = (String) params.get("entityType");
        foldersPane = (FoldersPane) params.get("foldersPane");
        entityClass = (String) params.get("entityClass");
        query = (String) params.get("query");
        foldersSelect = getComponent("folderSelect");

        Button createBtn = getComponent("createNew");
        Button insertBtn = getComponent("insertBtn");

        insertBtn.setAction(new InsertAction());
        createBtn.setAction(new CreateSetAction());
    }

    private class InsertAction extends AbstractAction {
        protected InsertAction() {
            super("InsertAction");
        }
        public void actionPerform(Component component) {
            SearchFolder folder = foldersSelect.getValue();
            if (folder==null){
                showNotification(getMessage("saveSetWindow.notSelected"),NotificationType.TRAY);
                return; }
            String filterXml = folder.getFilterXml();
                folder.setFilterXml(UserSetHelper.addEntities(filterXml, ids));
                foldersPane.saveFolder(folder);
                foldersPane.refreshFolders();
                close(COMMIT_ACTION_ID,true);
        }
    }

    private class CreateSetAction extends AbstractAction {

        protected CreateSetAction() {
            super("CreateSetAction");
        }

        @Override
        public void actionPerform(Component component) {
            QueryParser parser = QueryTransformerFactory.createParser(query);
            String entityAlias = parser.getEntityAlias(entityType);
            String filterXml = UserSetHelper.generateSetFilter(ids, entityClass, componentId, entityAlias);
            Metadata metadata = AppBeans.get(Metadata.NAME);
            final SearchFolder folder = metadata.create(SearchFolder.class);
            UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
            folder.setUser(sessionSource.getUserSession().getUser());
            folder.setName("");
            folder.setFilterXml(filterXml);
            folder.setFilterComponentId(componentPath);
            folder.setEntityType(entityType);
            folder.setIsSet(true);

            Runnable commitHandler = new Runnable() {
                public void run() {
                    foldersPane.saveFolder(folder);
                    foldersPane.refreshFolders();
                }
            };

            final FolderEditWindow window = AppFolderEditWindow.create(false, false, folder, null, commitHandler);
            window.addListener(new com.vaadin.ui.Window.CloseListener() {
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    App.getInstance().getAppWindow().removeWindow(window);
                }
            });
            App.getInstance().getAppWindow().addWindow(window);
            window.addListener(new com.vaadin.ui.Window.CloseListener(){
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    close(COMMIT_ACTION_ID);
                }
            });
        }
    }
}