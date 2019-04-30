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

package com.haulmont.cuba.gui.components.factories;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.components.data.options.DatasourceOptions;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataComponents;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;
import javax.inject.Inject;

@org.springframework.stereotype.Component(DataGridEditorComponentGenerationStrategy.NAME)
public class DataGridEditorComponentGenerationStrategy extends AbstractComponentGenerationStrategy implements Ordered {
    public static final String NAME = "cuba_DataGridEditorMetaComponentStrategy";

    protected DataComponents dataComponents;

    @Inject
    public void setDataComponents(DataComponents dataComponents) {
        this.dataComponents = dataComponents;
    }

    @Inject
    public DataGridEditorComponentGenerationStrategy(Messages messages, DynamicAttributesTools dynamicAttributesTools) {
        super(messages, dynamicAttributesTools);
    }

    @Inject
    public void setUiComponents(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }

    @Nullable
    @Override
    public Component createComponent(ComponentGenerationContext context) {
        if (context.getComponentClass() == null
                || !DataGrid.class.isAssignableFrom(context.getComponentClass())) {
            return null;
        }

        return createComponentInternal(context);
    }

    @Override
    protected Component createStringField(ComponentGenerationContext context, MetaPropertyPath mpp) {
        TextField component = uiComponents.create(TextField.class);
        setValueSource(component, context);
        return component;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Field createEntityField(ComponentGenerationContext context, MetaPropertyPath mpp) {
        Options options = context.getOptions();

        Lookup lookupAnnotation;
        if ((lookupAnnotation = mpp.getMetaProperty().getAnnotatedElement().getAnnotation(Lookup.class)) != null
                && lookupAnnotation.type() == LookupType.DROPDOWN) {
            MetaClass metaClass = mpp.getMetaProperty().getRange().asClass();
            CollectionContainer<Entity> container = dataComponents.createCollectionContainer(metaClass.getJavaClass());
            CollectionLoader<Entity> loader = dataComponents.createCollectionLoader();
            loader.setQuery("select e from " + metaClass.getName() + " e");
            loader.setView(View.MINIMAL);
            loader.setContainer(container);
            loader.load();
            options = new ContainerOptions(container);
        }

        if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
            DynamicAttributesMetaProperty metaProperty = (DynamicAttributesMetaProperty) mpp.getMetaProperty();
            CategoryAttribute attribute = metaProperty.getAttribute();
            if (Boolean.TRUE.equals(attribute.getLookup())) {
                DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
                CollectionDatasource optionsDatasource = dynamicAttributesGuiTools
                        .createOptionsDatasourceForLookup(metaProperty.getRange().asClass(),
                                attribute.getJoinClause(), attribute.getWhereClause());
                options = new DatasourceOptions(optionsDatasource);
            }
        }

        PickerField pickerField;
        if (options == null) {
            pickerField = uiComponents.create(PickerField.class);
            setValueSource(pickerField, context);
            pickerField.addLookupAction();
            if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
                DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
                DynamicAttributesMetaProperty dynamicAttributesMetaProperty =
                        (DynamicAttributesMetaProperty) mpp.getMetaProperty();
                dynamicAttributesGuiTools.initEntityPickerField(pickerField,
                        dynamicAttributesMetaProperty.getAttribute());
            }
            PickerField.LookupAction lookupAction =
                    (PickerField.LookupAction) pickerField.getActionNN(PickerField.LookupAction.NAME);
            // Opening lookup screen in another mode will close editor
            lookupAction.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
            // In case of adding special logic for lookup screen opened from DataGrid editor
            lookupAction.setLookupScreenParams(ParamsMap.of("dataGridEditor", true));
            boolean actionsByMetaAnnotations = ComponentsHelper.createActionsByMetaAnnotations(pickerField);
            if (!actionsByMetaAnnotations) {
                pickerField.addClearAction();
            }
        } else {
            LookupPickerField lookupPickerField = uiComponents.create(LookupPickerField.class);
            setValueSource(lookupPickerField, context);
            lookupPickerField.setOptions(options);

            pickerField = lookupPickerField;

            ComponentsHelper.createActionsByMetaAnnotations(pickerField);
        }

        return pickerField;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 30;
    }
}