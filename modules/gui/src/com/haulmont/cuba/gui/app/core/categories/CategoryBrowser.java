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

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.client.sys.cache.DynamicAttributesCacheStrategy;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCacheService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang3.BooleanUtils;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CategoryBrowser extends AbstractLookup {

    @Inject
    protected Metadata metadata;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Table<Category> categoryTable;

    @Inject
    protected CollectionDatasource<Category, UUID> categoriesDs;

    @Inject
    protected DynamicAttributesCacheService dynamicAttributesCacheService;

    @Inject
    protected PermissionConfig permissionConfig;

    @Inject
    protected ClientCacheManager clientCacheManager;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected Table<CategoryAttribute> attributesTable;

    @Inject
    protected CollectionDatasource<CategoryAttribute, UUID> attributesDs;

    @Override
    public void init(Map<String, Object> params) {
        categoryTable.addAction(new CreateAction());
        categoryTable.addAction(new EditAction());
        categoryTable.addAction(new RemoveAction(categoryTable));
        categoryTable.addAction(new BaseAction("applyChanges")
                .withCaption(getMessage("categoryTable.applyChanges"))
                .withHandler(actionPerformedEvent -> {
                    dynamicAttributesCacheService.loadCache();
                    clientCacheManager.refreshCached(DynamicAttributesCacheStrategy.NAME);
                    permissionConfig.clearConfigCache();

                    showNotification(getMessage("notification.changesApplied"));
                }));

        categoryTable.removeGeneratedColumn("entityType");

        categoryTable.addGeneratedColumn("entityType", entity -> {
            Label dataTypeLabel = componentsFactory.createComponent(Label.class);
            MetaClass meta = metadata.getSession().getClassNN(entity.getEntityType());
            dataTypeLabel.setValue(messageTools.getEntityCaption(meta));
            return dataTypeLabel;
        });

        initAttrDataTypeColumn();
    }

    protected class CreateAction extends AbstractAction {

        public CreateAction() {
            super("create", Status.PRIMARY);
        }

        @Override
        public String getCaption() {
            return getMessage("categoryTable.create");
        }

        @Override
        public void actionPerform(Component component) {
            Category category = metadata.create(Category.class);
            Editor editor = openEditor("sys$Category.edit", category, OpenType.THIS_TAB);
            editor.addCloseListener(actionId -> {
                categoriesDs.refresh();
                categoryTable.requestFocus();
            });
        }
    }

    protected class EditAction extends ItemTrackingAction {

        public EditAction() {
            super("edit");
        }

        @Override
        public String getCaption() {
            return getMessage("categoryTable.edit");
        }

        @Override
        public void actionPerform(Component component) {
            Set<Category> selected = categoryTable.getSelected();
            if (!selected.isEmpty()) {
                Category category = selected.iterator().next();
                Editor editor = openEditor("sys$Category.edit", category, OpenType.THIS_TAB);
                editor.addCloseListener(actionId -> {
                    categoriesDs.refresh();
                    categoryTable.requestFocus();
                });
            }
        }
    }

    protected void initAttrDataTypeColumn() {
        attributesTable.removeGeneratedColumn("dataType");
        attributesTable.addGeneratedColumn("dataType", attribute -> {
            String labelContent;
            if (BooleanUtils.isTrue(attribute.getIsEntity())) {
                Class clazz = attribute.getJavaClassForEntity();

                if (clazz != null) {
                    MetaClass metaClass = metadata.getSession().getClass(clazz);
                    labelContent = messageTools.getEntityCaption(metaClass);
                } else {
                    labelContent = "";
                }
            } else {
                labelContent = getMessage(attribute.getDataType().name());
            }

            return new Table.PlainTextCell(labelContent);
        });
    }
}