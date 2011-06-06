/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.categories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import org.apache.commons.lang.BooleanUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class CategoryEditor extends AbstractEditor {
    private Table table;
    private DataService dataService;
    private Datasource categoryDs;
    private Category category;
    private CheckBox cb;
    private ComponentsFactory factory = AppConfig.getFactory();
    private Locale locale = UserSessionProvider.getLocale();

    public CategoryEditor(IFrame frame) {
        super(frame);
    }

    protected void init(Map<String, Object> params) {
        table = getComponent("categoryAttrsTable");
        dataService = getDsContext().getDataService();
        table.addAction(new CategoryAttributeCreateAction());
        table.addAction(new CategoryAttributeEditAction());
        table.addAction(new RemoveAction(table,false));
        table.addAction(new RefreshAction(table));
        categoryDs=getDsContext().get("categoryDs");
        cb = getComponent("isDefault");

    }

    public void setItem(Entity item) {
        super.setItem(item);
        category = (Category) getItem();
        generateEntityTypeField();
        initDataTypeColumn();
        initDefaultValueColumn();
        initCb();
    }

    private void initDataTypeColumn() {

        MetaPropertyPath dataType = table.getDatasource().getMetaClass().getPropertyPath("dataType");

        table.removeGeneratedColumn(dataType);
        table.addGeneratedColumn(dataType.toString(), new Table.ColumnGenerator() {
            public Component generateCell(Table table, Object itemId) {
                Label dataTypeLabel = factory.createComponent(Label.NAME);
                String labelContent;
                CategoryAttribute attribute = (CategoryAttribute) table.getDatasource().getItem(itemId);
                if (BooleanUtils.isTrue(attribute.getIsEntity())) {
                    try {
                        Class clazz = Class.forName(attribute.getDataType());
                        MetaClass metaClass = MetadataProvider.getSession().getClass(clazz);
                        labelContent = MessageUtils.getEntityCaption(metaClass);
                    } catch (ClassNotFoundException ex) {
                        labelContent = "classNotFound";
                    }
                } else {
                    labelContent = getMessage(attribute.getDataType());
                }
                //dataTypeLabel.setCaption(labelContent);

                dataTypeLabel.setValue(labelContent);
                return dataTypeLabel;
            }
        });
    }
    public void initDefaultValueColumn(){

        MetaPropertyPath defaultValue = table.getDatasource().getMetaClass().getPropertyPath("defaultValue");
        table.removeGeneratedColumn(defaultValue);
        table.addGeneratedColumn(defaultValue.toString(), new Table.ColumnGenerator() {
            public Component generateCell(Table table, Object itemId) {
                Label defaultValueLabel = factory.createComponent(Label.NAME);
                String labelContent;
                CategoryAttribute attribute = (CategoryAttribute) table.getDatasource().getItem(itemId);
                if (BooleanUtils.isTrue(attribute.getIsEntity())) {
                    try {
                        Class clazz = Class.forName(attribute.getDataType());
                        LoadContext entitiesContext = new LoadContext(clazz);
                        String entityClassName = MetadataProvider.getSession().getClass(clazz).getName();
                        if (attribute.getDefaultEntityId() != null) {
                            LoadContext.Query query = entitiesContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
                            query.addParameter("e", attribute.getDefaultEntityId());
                            entitiesContext.setView("_local");
                            Entity entity = dataService.load(entitiesContext);
                            labelContent = InstanceUtils.getInstanceName(entity);
                        } else labelContent = "";
                    } catch (ClassNotFoundException ex) {
                        labelContent = "entityNotFound";
                    }
                } else if (RuntimePropsDatasource.PropertyType.BOOLEAN.equals(RuntimePropsDatasource.PropertyType.valueOf(attribute.getDataType()))
                        && (attribute.getDefaultValue() != null)) {
                    labelContent = getMessage(attribute.getDefaultValue());

                } else {
                    labelContent = attribute.getDefaultValue();
                }
                defaultValueLabel.setValue(labelContent);
                return defaultValueLabel;
            }
        });
    }

    private void generateEntityTypeField(){

        boolean hasValue = (category.getEntityType() == null) ? (false) : (true);

        LookupField categoryEntityTypeField = getComponent("entityType");
        Map<String,Object> options = new HashMap<String,Object>();
        MetaClass entityType = null;
        for (MetaClass metaClass : MetadataHelper.getAllPersistentMetaClasses()) {
            if (CategorizedEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
                options.put(MessageUtils.getEntityCaption(metaClass), metaClass);
                if (hasValue && metaClass.getName().equals(category.getEntityType())) {
                    entityType = metaClass;
                }
            }
        }
        categoryEntityTypeField.setOptionsMap(options);
        categoryEntityTypeField.setValue(entityType);
        categoryEntityTypeField.addListener(new ValueListener(){
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                category.setEntityType(((MetaClass)value).getName());
            }
        });
    }

    private void initCb() {
        cb.setValue(BooleanUtils.isTrue(category.getIsDefault()));
        cb.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (Boolean.TRUE.equals(value)) {
                    LoadContext categoriesContext = new LoadContext(category.getClass());
                    LoadContext.Query query = categoriesContext.setQueryString("select c from sys$Category c where c.entityType= :entityType and not c.id=:id");
                    categoriesContext.setView("category.defaultEdit");
                    query.addParameter("entityType", category.getEntityType());
                    query.addParameter("id", category.getId());
                    List<Category> categories = dataService.loadList(categoriesContext);
                    for(Category cat : categories){
                        cat.setIsDefault(false);
                    }
                    CommitContext commitContext = new CommitContext(categories);
                    dataService.commit(commitContext);
                    category.setIsDefault(true);
                }
                else if(Boolean.FALSE.equals(value)){
                    category.setIsDefault(false);
                }
            }


        });
    }

    protected class CategoryAttributeEditAction extends AbstractAction {

        protected CategoryAttributeEditAction() {
            super("edit");
        }

        public String getCaption() {
            return getMessage("categoryAttrsTable.edit");
        }

        @Override
        public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
            if (!table.getSelected().isEmpty()) {
                AttributeEditor editor = openEditor(
                        "sys$CategoryAttribute.edit",
                        (CategoryAttribute) table.getSelected().iterator().next(),
                        WindowManager.OpenType.DIALOG,
                        table.getDatasource());
                editor.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        table.getDatasource().refresh();
                    }
                });
            }
        }
    }

    protected class CategoryAttributeCreateAction extends AbstractAction {

        protected CategoryAttributeCreateAction() {
            super("create");
        }

        public String getCaption() {
            return getMessage("categoryAttrsTable.create");
        }

        @Override
        public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
            final CategoryAttribute attribute = EntityFactory.create(CategoryAttribute.class);
            attribute.setCategory((Category) categoryDs.getItem());
            AttributeEditor editor = openEditor(
                    "sys$CategoryAttribute.edit",
                    attribute,
                    WindowManager.OpenType.DIALOG,
                    table.getDatasource());
            editor.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    table.getDatasource().refresh();
                }
            });
        }
    }

}
