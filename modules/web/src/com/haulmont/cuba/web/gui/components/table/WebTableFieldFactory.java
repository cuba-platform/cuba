/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.factories.AbstractFieldFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.web.gui.components.WebAbstractTable;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.ui.TableFieldFactory;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Map;

public class WebTableFieldFactory extends AbstractFieldFactory implements TableFieldFactory {
    protected WebAbstractTable<?, ?> webTable;
    protected Security security;
    protected MetadataTools metadataTools;

    public WebTableFieldFactory(WebAbstractTable<?, ?> webTable, Security security, MetadataTools metadataTools) {
        this.webTable = webTable;
        this.security = security;
        this.metadataTools = metadataTools;
    }

    @Override
    public com.vaadin.v7.ui.Field<?> createField(com.vaadin.v7.data.Container container,
                                                 Object itemId, Object propertyId, Component uiContext) {

        String fieldPropertyId = String.valueOf(propertyId);

        Table.Column columnConf = webTable.getColumnsInternal().get(propertyId);

        Item item = container.getItem(itemId);
        Entity entity = ((ItemWrapper) item).getItem();
        Datasource fieldDatasource = webTable.getItemDatasource(entity);

        com.haulmont.cuba.gui.components.Component columnComponent =
                createField(fieldDatasource, fieldPropertyId, columnConf.getXmlDescriptor());

        if (columnComponent instanceof Field) {
            Field cubaField = (Field) columnComponent;

            Map<Table.Column, String> requiredColumns = webTable.getRequiredColumnsInternal();
            if (requiredColumns != null && requiredColumns.containsKey(columnConf)) {
                cubaField.setRequired(true);
                cubaField.setRequiredMessage(requiredColumns.get(columnConf));
            }
        }

        if (!(columnComponent instanceof CheckBox)) {
            columnComponent.setWidthFull();
        }

        if (columnComponent instanceof com.haulmont.cuba.gui.components.Component.BelongToFrame) {
            com.haulmont.cuba.gui.components.Component.BelongToFrame belongToFrame = (com.haulmont.cuba.gui.components.Component.BelongToFrame) columnComponent;
            if (belongToFrame.getFrame() == null) {
                belongToFrame.setFrame(webTable.getFrame());
            }
        }

        applyPermissions(columnComponent);

        columnComponent.setParent(webTable);

        Component componentImpl = getComponentImplementation(columnComponent);
        if (componentImpl instanceof com.vaadin.v7.ui.Field) {
            return (com.vaadin.v7.ui.Field<?>) componentImpl;
        }

        return new EditableColumnFieldWrapper(componentImpl, columnComponent);
    }

    protected Component getComponentImplementation(com.haulmont.cuba.gui.components.Component columnComponent) {
        com.vaadin.ui.Component composition = WebComponentsHelper.getComposition(columnComponent);
        Component componentImpl = composition;
        if (composition instanceof com.vaadin.v7.ui.Field
                && ((com.vaadin.v7.ui.Field) composition).isRequired()) {
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(false);
            layout.setSpacing(false);
            layout.addComponent(composition);

            if (composition.getWidth() < 0) {
                layout.setWidthUndefined();
            }

            componentImpl = layout;
        }
        return componentImpl;
    }

    protected void applyPermissions(com.haulmont.cuba.gui.components.Component columnComponent) {
        if (columnComponent instanceof DatasourceComponent
                && columnComponent instanceof com.haulmont.cuba.gui.components.Component.Editable) {
            DatasourceComponent dsComponent = (DatasourceComponent) columnComponent;
            MetaPropertyPath propertyPath = dsComponent.getMetaPropertyPath();

            if (propertyPath != null) {
                MetaClass metaClass = dsComponent.getDatasource().getMetaClass();
                com.haulmont.cuba.gui.components.Component.Editable editable = (com.haulmont.cuba.gui.components.Component.Editable) dsComponent;

                editable.setEditable(editable.isEditable()
                        && security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString()));
            }
        }
    }

    @Override
    @Nullable
    protected CollectionDatasource getOptionsDatasource(Datasource fieldDatasource, String propertyId) {
        if (webTable.getDatasource() == null) {
            throw new IllegalStateException("Table datasource is null");
        }

        MetaClass metaClass = webTable.getDatasource().getMetaClass();
        MetaPropertyPath metaPropertyPath = metadataTools.resolveMetaPropertyPath(metaClass, propertyId);
        Table.Column columnConf = webTable.getColumnsInternal().get(metaPropertyPath);
        DsContext dsContext = webTable.getDatasource().getDsContext();

        String optDsName = columnConf.getXmlDescriptor() != null ?
                columnConf.getXmlDescriptor().attributeValue("optionsDatasource") : "";

        if (StringUtils.isBlank(optDsName)) {
            return null;
        } else {
            CollectionDatasource ds = (CollectionDatasource) dsContext.get(optDsName);
            if (ds == null) {
                throw new IllegalStateException(
                        String.format("Options datasource for table column '%s' not found: %s", propertyId, optDsName));
            }

            return ds;
        }
    }
}