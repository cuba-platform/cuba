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

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.EntitySqlGenerationService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.Role;

import javax.inject.Inject;
import java.util.Map;

/**
 */
public class SystemInfoWindow extends AbstractWindow {

    public interface Companion {
        void initInfoTable(Table infoTable);
    }

    @Inject
    protected EntityParamsDatasource paramsDs;

    @Inject
    protected Table infoTable;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected TextArea scriptArea;

    @Inject
    protected EntitySqlGenerationService sqlGenerationService;

    @Inject
    protected ClientConfig clientConfig;

    @WindowParam(name = "item")
    protected Entity item;

    @Inject
    protected Button insert;

    @Inject
    protected Button select;

    @Inject
    protected Button update;

    @Inject
    protected BoxLayout buttonsHbox;

    @Inject
    protected Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        paramsDs.setInstance(item);
        paramsDs.setInstanceMetaClass((MetaClass) params.get("metaClass"));

        paramsDs.refresh();

        Companion companion = getCompanion();
        if (companion != null) {
            companion.initInfoTable(infoTable);
        }

        infoTable.removeAllActions();

        if (!clientConfig.getSystemInfoScriptsEnabled()
                || item == null
                || !metadata.getTools().isPersistent(item.getMetaClass())) {
            buttonsHbox.setVisible(false);
        }
    }

    public void generateInsert() {
        scriptArea.setEditable(true);
        if (item instanceof Role) {
            View localView = metadata.getViewRepository().getView(Role.class, View.LOCAL);
            View roleView = new View(localView, Role.class, "role-export-view", true)
                    .addProperty("permissions", metadata.getViewRepository().getView(Permission.class, View.LOCAL));
            item = getDsContext().getDataSupplier().reload(item, roleView);

            StringBuilder result = new StringBuilder();
            result.append(sqlGenerationService.generateInsertScript(item)).append("\n");
            for (Permission permission : ((Role) item).getPermissions()) {
                result.append(sqlGenerationService.generateInsertScript(permission)).append("\n");
            }

            scriptArea.setValue(result.toString());
        } else {
            scriptArea.setValue(sqlGenerationService.generateInsertScript(item));
        }

        scriptArea.setVisible(true);
        scriptArea.setEditable(false);
    }

    public void generateUpdate() {
        scriptArea.setEditable(true);
        scriptArea.setValue(sqlGenerationService.generateUpdateScript(item));
        scriptArea.setVisible(true);
        scriptArea.setEditable(false);
    }

    public void generateSelect() {
        scriptArea.setEditable(true);
        scriptArea.setValue(sqlGenerationService.generateSelectScript(item));
        scriptArea.setVisible(true);
        scriptArea.setEditable(false);
    }
}