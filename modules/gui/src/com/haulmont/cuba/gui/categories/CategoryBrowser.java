/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.categories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;


import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CategoryBrowser extends AbstractLookup{
    private Table table;
    private CollectionDatasource categoriesDs;

    @Override
    public void init(Map<String, Object> params) {
        categoriesDs=getDsContext().get("categoriesDs");
        table = getComponent("categoryTable");
        table.addAction(new CreateAction());
        table.addAction(new EditAction());
        table.addAction(new RemoveAction(table));

        table.removeGeneratedColumn("entityType");

        table.addGeneratedColumn("entityType",new Table.ColumnGenerator(){
            @Override
            public Component generateCell(Table table, Object itemId) {
                Label dataTypeLabel = AppConfig.getFactory().createComponent(Label.NAME);
                Category category = (Category) table.getDatasource().getItem(itemId);
                MetaClass meta = MetadataProvider.getSession().getClass(category.getEntityType());
                dataTypeLabel.setValue(MessageUtils.getEntityCaption(meta));
                return dataTypeLabel;
            }
        });
    }


    protected class CreateAction extends AbstractAction {

        protected CreateAction() {
            super("create");
        }

        public String getCaption() {
            return getMessage("categoryTable.create");
        }

        @Override
        public void actionPerform(Component component) {
            Category category = MetadataProvider.create(Category.class);
            CategoryEditor editor = openEditor("sys$Category.edit", category, WindowManager.OpenType.THIS_TAB);
            editor.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    categoriesDs.refresh();
                }
            });
        }
    }

    protected class EditAction extends AbstractAction {

        protected EditAction() {
            super("edit");
        }

        public String getCaption() {
            return getMessage("categoryTable.edit");
        }

        @Override
        public void actionPerform(Component component) {
            if (!table.getSelected().isEmpty()) {
                Category category = (Category) table.getSelected().iterator().next();
                CategoryEditor editor = openEditor("sys$Category.edit", category, WindowManager.OpenType.THIS_TAB);
                editor.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        categoriesDs.refresh();
                    }
                });
            }
        }
    }


}
