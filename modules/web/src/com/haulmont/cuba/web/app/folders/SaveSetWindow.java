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

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.QueryParser;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.filter.UserSetHelper;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.AppUI;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

public class SaveSetWindow extends AbstractWindow {

    @Inject
    protected Metadata metadata;
    @Inject
    protected UserSessionSource sessionSource;

    @Inject
    protected LookupField<SearchFolder> foldersSelect;

    @WindowParam
    protected CubaFoldersPane foldersPane;

    @WindowParam(name = "items")
    protected Set ids;
    @WindowParam
    protected String componentPath;
    @WindowParam
    protected String componentId;
    @WindowParam
    protected String entityType;
    @WindowParam
    protected String entityClass;
    @WindowParam
    protected String query;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions().setWidthAuto();
    }

    @Subscribe("createNew")
    protected void onCreateNewClick(Button.ClickEvent event) {
        QueryParser parser = QueryTransformerFactory.createParser(query);
        String entityAlias = parser.getEntityAlias(entityType);
        String filterXml = UserSetHelper.generateSetFilter(ids, entityClass, componentId, entityAlias);

        SearchFolder folder = metadata.create(SearchFolder.class);
        folder.setUser(sessionSource.getUserSession().getUser());
        folder.setName("");
        folder.setFilterXml(filterXml);
        folder.setFilterComponentId(componentPath);
        folder.setEntityType(entityType);
        folder.setIsSet(true);

        Runnable commitHandler = () -> {
            foldersPane.saveFolder(folder);
            foldersPane.refreshFolders();
        };

        FolderEditWindow window = AppFolderEditWindow.create(false, false, folder, null, commitHandler);
        AppUI.getCurrent().addWindow(window);
        window.addCloseListener(e -> close(COMMIT_ACTION_ID));
    }

    @Subscribe("insertBtn")
    protected void onInsertBtnClick(Button.ClickEvent event) {
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