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

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.UserSetHelper;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.AppUI;

import java.util.Map;
import java.util.Set;

/**
 */
public class SaveSetWindow extends AbstractWindow {

    private Set ids;
    private String componentPath;
    private String componentId;
    private String entityType;
    private String entityClass;
    private CubaFoldersPane foldersPane;
    private LookupField foldersSelect;
    private String query;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidthAuto();

        ids = (Set) params.get("items");
        componentPath = (String) params.get("componentPath");
        componentId = (String) params.get("componentId");
        entityType = (String) params.get("entityType");
        foldersPane = (CubaFoldersPane) params.get("foldersPane");
        entityClass = (String) params.get("entityClass");
        query = (String) params.get("query");
        foldersSelect = (LookupField) getComponent("folderSelect");

        Button createBtn = (Button) getComponentNN("createNew");
        Button insertBtn = (Button) getComponentNN("insertBtn");

        insertBtn.setAction(new InsertAction());
        createBtn.setAction(new CreateSetAction());
    }

    private class InsertAction extends AbstractAction {
        protected InsertAction() {
            super("InsertAction");
        }

        @Override
        public void actionPerform(Component component) {
            SearchFolder folder = foldersSelect.getValue();
            if (folder == null) {
                showNotification(getMessage("saveSetWindow.notSelected"), NotificationType.TRAY);
                return;
            }
            String filterXml = folder.getFilterXml();
            folder.setFilterXml(UserSetHelper.addEntities(filterXml, ids));
            foldersPane.saveFolder(folder);
            foldersPane.refreshFolders();
            close(COMMIT_ACTION_ID, true);
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
                @Override
                public void run() {
                    foldersPane.saveFolder(folder);
                    foldersPane.refreshFolders();
                }
            };

            final FolderEditWindow window = AppFolderEditWindow.create(false, false, folder, null, commitHandler);
            AppUI.getCurrent().addWindow(window);
            window.addCloseListener(new com.vaadin.ui.Window.CloseListener() {
                @Override
                public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                    close(COMMIT_ACTION_ID);
                }
            });
        }
    }
}