/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
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
    protected Table categoryTable;

    @Inject
    protected CollectionDatasource categoriesDs;

    @Override
    public void init(Map<String, Object> params) {
        categoriesDs = getDsContext().get("categoriesDs");
        categoryTable = getComponent("categoryTable");
        categoryTable.addAction(new CreateAction());
        categoryTable.addAction(new EditAction());
        categoryTable.addAction(new RemoveAction(categoryTable));

        categoryTable.removeGeneratedColumn("entityType");

        categoryTable.addGeneratedColumn("entityType", new Table.ColumnGenerator<Category>() {
            @Override
            public Component generateCell(Category entity) {
                Label dataTypeLabel = AppConfig.getFactory().createComponent(Label.NAME);
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
            CategoryEditor editor = openEditor("sys$Category.edit", category, WindowManager.OpenType.THIS_TAB);
            editor.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    categoriesDs.refresh();
                    categoryTable.requestFocus();
                }
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
                CategoryEditor editor = openEditor("sys$Category.edit", category, WindowManager.OpenType.THIS_TAB);
                editor.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        categoriesDs.refresh();
                        categoryTable.requestFocus();
                    }
                });
            }
        }
    }
}