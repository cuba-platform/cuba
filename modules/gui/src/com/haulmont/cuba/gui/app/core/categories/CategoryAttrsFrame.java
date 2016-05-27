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

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionPropertyDatasourceImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CategoryAttrsFrame extends AbstractFrame {

    @Inject
    protected Metadata metadata;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    private Table<CategoryAttribute> categoryAttrsTable;

    @Inject
    protected Datasource categoryDs;

    @Inject
    protected CollectionPropertyDatasourceImpl<CategoryAttribute, UUID> categoryAttrsDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        categoryAttrsTable.addAction(new CategoryAttributeCreateAction());
        categoryAttrsTable.addAction(new CategoryAttributeEditAction());
        categoryAttrsTable.addAction(new RemoveAction(categoryAttrsTable, false));
        categoryAttrsTable.addAction(new RefreshAction(categoryAttrsTable));

        categoryAttrsDs.addStateChangeListener(e -> {
            if (e.getState() != Datasource.State.VALID) {
                return;
            }
            initDataTypeColumn();
            initDefaultValueColumn();
        });

        initMoveButtons();
    }

    protected void initMoveButtons() {
        AbstractAction moveUpAction = new ItemTrackingAction("moveUp") {
            @Override
            public void actionPerform(Component component) {
                Set<CategoryAttribute> selected = categoryAttrsTable.getSelected();
                if (selected.isEmpty())
                    return;

                CategoryAttribute currentAttr = selected.iterator().next();
                UUID prevId = categoryAttrsDs.prevItemId(currentAttr.getId());
                if (prevId == null)
                    return;

                Integer tmp = currentAttr.getOrderNo();
                CategoryAttribute prevAttr = categoryAttrsDs.getItem(prevId);
                currentAttr.setOrderNo(prevAttr.getOrderNo());
                prevAttr.setOrderNo(tmp);

                sortTableByOrderNo();
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        ((Button) getComponentNN("moveUp")).setAction(moveUpAction);

        AbstractAction moveDownAction = new ItemTrackingAction("moveDown") {
            @Override
            public void actionPerform(Component component) {
                Set<CategoryAttribute> selected = categoryAttrsTable.getSelected();
                if (selected.isEmpty())
                    return;

                CategoryAttribute currentAttr = selected.iterator().next();
                UUID nextId = categoryAttrsDs.nextItemId(currentAttr.getId());
                if (nextId == null)
                    return;

                Integer tmp = currentAttr.getOrderNo();
                CategoryAttribute nextAttr = categoryAttrsDs.getItem(nextId);
                currentAttr.setOrderNo(nextAttr.getOrderNo());
                nextAttr.setOrderNo(tmp);

                sortTableByOrderNo();
            }

            @Override
            public String getCaption() {
                return "";
            }
        };
        ((Button) getComponentNN("moveDown")).setAction(moveDownAction);

        categoryAttrsTable.addAction(moveUpAction);
        categoryAttrsTable.addAction(moveDownAction);
    }

    protected void sortTableByOrderNo() {
        categoryAttrsTable.sortBy(categoryAttrsDs.getMetaClass().getPropertyPath("orderNo"), true);
    }

    protected void initDataTypeColumn() {
        categoryAttrsTable.removeGeneratedColumn("dataType");
        categoryAttrsTable.addGeneratedColumn("dataType", new Table.ColumnGenerator<CategoryAttribute>() {
            @Override
            public Component generateCell(CategoryAttribute attribute) {
                Label dataTypeLabel = factory.createComponent(Label.class);
                String labelContent;
                if (BooleanUtils.isTrue(attribute.getIsEntity())) {
                    Class clazz = attribute.getJavaClassForEntity();

                    if (clazz != null) {
                        MetaClass metaClass = metadata.getSession().getClass(clazz);
                        labelContent = messageTools.getEntityCaption(metaClass);
                    } else {
                        labelContent = "classNotFound";
                    }
                } else {
                    labelContent = getMessage(attribute.getDataType().name());
                }

                dataTypeLabel.setValue(labelContent);
                return dataTypeLabel;
            }
        });
    }

    protected void initDefaultValueColumn() {
        categoryAttrsTable.addGeneratedColumn("defaultValue", new Table.ColumnGenerator<CategoryAttribute>() {
            @Override
            public Component generateCell(CategoryAttribute attribute) {
                String defaultValue = "";

                if (BooleanUtils.isNotTrue(attribute.getIsEntity())) {
                    PropertyType dataType = attribute.getDataType();
                    switch (dataType) {
                        case DATE:
                            Date date = attribute.getDefaultDate();
                            if (date != null) {
                                String dateTimeFormat = Datatypes.getFormatStringsNN(userSessionSource.getLocale()).getDateTimeFormat();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
                                defaultValue = simpleDateFormat.format(date);
                            } else if (BooleanUtils.isTrue(attribute.getDefaultDateIsCurrent())) {
                                defaultValue = getMessage("currentDate");
                            }
                            break;
                        case BOOLEAN:
                            Boolean b = attribute.getDefaultBoolean();
                            if (b != null)
                                defaultValue = BooleanUtils.isTrue(b) ? getMessage("msgTrue") : getMessage("msgFalse");
                            break;
                        default:
                            if (attribute.getDefaultValue() != null)
                                defaultValue = attribute.getDefaultValue().toString();
                    }
                } else {
                    Class clazz = attribute.getJavaClassForEntity();
                    if (clazz != null) {
                        LoadContext entitiesContext = new LoadContext(clazz);
                        String entityClassName = metadata.getClassNN(clazz).getName();
                        if (attribute.getDefaultEntityId() != null) {
                            LoadContext.Query query = entitiesContext.setQueryString("select a from " + entityClassName + " a where a.id =:e");
                            query.setParameter("e", attribute.getDefaultEntityId());
                            entitiesContext.setView("_local");
                            Entity entity = dataSupplier.load(entitiesContext);
                            defaultValue = InstanceUtils.getInstanceName(entity);
                        } else defaultValue = "";
                    } else {
                        defaultValue = getMessage("entityNotFound");
                    }
                }

                Label defaultValueLabel = factory.createComponent(Label.class);
                defaultValueLabel.setValue(defaultValue);
                return defaultValueLabel;
            }
        });
    }

    protected void assignNextOrderNo(CategoryAttribute attr) {
        UUID lastId = categoryAttrsDs.lastItemId();
        if (lastId == null)
            attr.setOrderNo(1);
        else {
            CategoryAttribute lastItem = categoryAttrsDs.getItemNN(lastId);
            if (lastItem.getOrderNo() == null)
                attr.setOrderNo(1);
            else
                attr.setOrderNo(categoryAttrsDs.getItemNN(lastId).getOrderNo() + 1);

        }
    }

    protected class CategoryAttributeEditAction extends ItemTrackingAction {

        protected CategoryAttributeEditAction() {
            super("edit");
        }

        @Override
        public String getCaption() {
            return getMessage("categoryAttrsTable.edit");
        }

        @Override
        public void actionPerform(Component component) {
            Set<CategoryAttribute> selected = categoryAttrsTable.getSelected();
            if (!selected.isEmpty()) {
                AttributeEditor editor = (AttributeEditor) openEditor(
                        "sys$CategoryAttribute.edit",
                        selected.iterator().next(),
                        WindowManager.OpenType.DIALOG,
                        categoryAttrsTable.getDatasource());
                editor.addCloseListener(actionId -> {
                    categoryAttrsTable.getDatasource().refresh();
                    categoryAttrsTable.requestFocus();
                    // restore selection from ds
                    categoryAttrsTable.setSelected(categoryAttrsDs.getItem());
                });
            }
        }
    }

    protected class CategoryAttributeCreateAction extends AbstractAction {

        protected CategoryAttributeCreateAction() {
            super("create");
        }

        @Override
        public String getCaption() {
            return getMessage("categoryAttrsTable.create");
        }

        @Override
        public void actionPerform(Component component) {
            final CategoryAttribute attribute = metadata.create(CategoryAttribute.class);
            attribute.setCategory((Category) categoryDs.getItem());
            assignNextOrderNo(attribute);
            AttributeEditor editor = (AttributeEditor) openEditor(
                    "sys$CategoryAttribute.edit",
                    attribute,
                    WindowManager.OpenType.DIALOG,
                    categoryAttrsTable.getDatasource());
            editor.addCloseListener(actionId -> {
                categoryAttrsTable.getDatasource().refresh();
                categoryAttrsTable.requestFocus();
                // restore selection from ds
                categoryAttrsTable.setSelected(categoryAttrsDs.getItem());
            });
        }
    }
}