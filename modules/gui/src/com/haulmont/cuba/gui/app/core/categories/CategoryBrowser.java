/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.client.sys.cache.DynamicAttributesCacheStrategy;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCacheService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CategoryBrowser extends AbstractLookup {

    @Inject
    protected Metadata metadata;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Table<Category> categoryTable;

    @Inject
    protected CollectionDatasource categoriesDs;

    @Inject
    protected DynamicAttributesCacheService dynamicAttributesCacheService;

    @Inject
    protected PermissionConfig permissionConfig;

    @Inject
    protected ClientCacheManager clientCacheManager;

    @Override
    public void init(Map<String, Object> params) {
        categoriesDs = (CollectionDatasource) getDsContext().get("categoriesDs");
        categoryTable.addAction(new CreateAction());
        categoryTable.addAction(new EditAction());
        categoryTable.addAction(new RemoveAction(categoryTable));
        categoryTable.addAction(new AbstractAction("applyChanges") {
            @Override
            public void actionPerform(Component component) {
                dynamicAttributesCacheService.loadCache();
                clientCacheManager.refreshCached(DynamicAttributesCacheStrategy.NAME);
                permissionConfig.clearConfigCache();
                showNotification(getMessage("notification.changesApplied"), NotificationType.HUMANIZED);
            }
        });

        categoryTable.removeGeneratedColumn("entityType");

        categoryTable.addGeneratedColumn("entityType", new Table.ColumnGenerator<Category>() {
            @Override
            public Component generateCell(Category entity) {
                Label dataTypeLabel = AppConfig.getFactory().createComponent(Label.class);
                MetaClass meta = metadata.getSession().getClassNN(entity.getEntityType());
                dataTypeLabel.setValue(messageTools.getEntityCaption(meta));
                return dataTypeLabel;
            }
        });
    }


    protected class CreateAction extends AbstractAction {

        public CreateAction() {
            super("create");
        }

        @Override
        public String getCaption() {
            return getMessage("categoryTable.create");
        }

        @Override
        public void actionPerform(Component component) {
            Category category = metadata.create(Category.class);
            Editor editor = openEditor("sys$Category.edit", category, WindowManager.OpenType.THIS_TAB);
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
                Editor editor = openEditor("sys$Category.edit", category, WindowManager.OpenType.THIS_TAB);
                editor.addCloseListener(actionId -> {
                    categoriesDs.refresh();
                    categoryTable.requestFocus();
                });
            }
        }
    }
}