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

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Component.BelongToFrame;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.components.data.options.DatasourceOptions;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.components.factories.AbstractFieldFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.gui.components.WebAbstractTable;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.TableFieldFactory;

import javax.annotation.Nullable;
import java.util.Map;

public class WebTableFieldFactory<E extends Entity> extends AbstractFieldFactory implements TableFieldFactory {
    protected WebAbstractTable<?, E> webTable;
    protected Security security;
    protected MetadataTools metadataTools;

    public WebTableFieldFactory(WebAbstractTable<?, E> webTable, Security security, MetadataTools metadataTools) {
        this.webTable = webTable;
        this.security = security;
        this.metadataTools = metadataTools;
    }

    @SuppressWarnings("unchecked")
    @Override
    public com.vaadin.v7.ui.Field<?> createField(com.vaadin.v7.data.Container container,
                                                 Object itemId, Object propertyId, Component uiContext) {

        String fieldPropertyId = String.valueOf(propertyId);

        Table.Column columnConf = webTable.getColumnsInternal().get(propertyId);

        TableDataContainer tableDataContainer = (TableDataContainer) container;
        Entity entity  = (Entity) tableDataContainer.getInternalItem(itemId);
        InstanceContainer instanceContainer = webTable.getInstanceContainer((E) entity);

        com.haulmont.cuba.gui.components.Component columnComponent =
                createField(new ContainerValueSource(instanceContainer, fieldPropertyId),
                        fieldPropertyId, columnConf.getXmlDescriptor());

        if (columnComponent instanceof Field) {
            Field cubaField = (Field) columnComponent;

            Map<Table.Column, String> requiredColumns = webTable.getRequiredColumnsInternal();
            if (requiredColumns != null && requiredColumns.containsKey(columnConf)) {
                cubaField.setRequired(true);
                cubaField.setRequiredMessage(requiredColumns.get(columnConf));
            }
        }

        if (!(columnComponent instanceof CheckBox)) { // todo get rid of concrete CheckBox class !
            columnComponent.setWidthFull();
        }

        if (columnComponent instanceof BelongToFrame) {
            BelongToFrame belongToFrame = (BelongToFrame) columnComponent;
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
        com.vaadin.ui.Component composition = columnComponent.unwrapComposition(com.vaadin.ui.Component.class);
        Component componentImpl = composition;
        if (composition instanceof com.vaadin.v7.ui.Field
                && ((com.vaadin.v7.ui.Field) composition).isRequired()) {
            VerticalLayout layout = new VerticalLayout(); // vaadin8 replace with CssLayout
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
        if (columnComponent instanceof HasValueSource
                && columnComponent instanceof com.haulmont.cuba.gui.components.Component.Editable) {
            HasValueSource component = (HasValueSource) columnComponent;
            MetaPropertyPath propertyPath = ((EntityValueSource) component.getValueSource()).getMetaPropertyPath();

            if (propertyPath != null) {
                MetaClass metaClass = ((EntityValueSource) component.getValueSource()).getEntityMetaClass();
                com.haulmont.cuba.gui.components.Component.Editable editable =
                        (com.haulmont.cuba.gui.components.Component.Editable) component;

                editable.setEditable(editable.isEditable()
                        && security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    protected Options getOptions(EntityValueSource valueSource, String property) {
        MetaClass metaClass = valueSource.getEntityMetaClass();
        MetaPropertyPath metaPropertyPath = metadataTools.resolveMetaPropertyPath(metaClass, property);
        Table.Column columnConf = webTable.getColumnsInternal().get(metaPropertyPath);

        CollectionContainer collectionContainer = findOptionsContainer(columnConf);
        if (collectionContainer != null) {
            return new ContainerOptions(collectionContainer);
        }

        CollectionDatasource ds = findOptionsDatasource(columnConf, property);
        if (ds != null) {
            return new DatasourceOptions(ds);
        }

        return null;
    }

    @Nullable
    protected CollectionContainer findOptionsContainer(Table.Column columnConf) {
        String optDcName = columnConf.getXmlDescriptor() != null ?
                columnConf.getXmlDescriptor().attributeValue("optionsContainer") : null;

        if (Strings.isNullOrEmpty(optDcName)) {
            return null;
        } else {
            ScreenData screenData = UiControllerUtils.getScreenData(webTable.getFrame().getFrameOwner());
            InstanceContainer container = screenData.getContainer(optDcName);

            if (container instanceof CollectionContainer) {
                return (CollectionContainer) container;
            }

            throw new IllegalStateException(
                    String.format("'%s' is not an instance of CollectionContainer", optDcName));
        }
    }

    @Nullable
    protected CollectionDatasource findOptionsDatasource(Table.Column columnConf, String propertyId) {
        String optDsName = columnConf.getXmlDescriptor() != null ?
                columnConf.getXmlDescriptor().attributeValue("optionsDatasource") : "";

        if (Strings.isNullOrEmpty(optDsName)) {
            return null;
        } else {
            if (webTable.getDatasource() == null) {
                throw new IllegalStateException("Table datasource is null");
            }

            DsContext dsContext = webTable.getDatasource().getDsContext();
            CollectionDatasource ds = (CollectionDatasource) dsContext.get(optDsName);
            if (ds == null) {
                throw new IllegalStateException(
                        String.format("Options datasource for table column '%s' not found: %s", propertyId, optDsName));
            }

            return ds;
        }
    }
}