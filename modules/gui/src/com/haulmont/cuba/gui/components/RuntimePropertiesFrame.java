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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.validators.DateValidator;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.components.validators.IntegerValidator;
import com.haulmont.cuba.gui.components.validators.LongValidator;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Universal frame for editing dynamic attributes of any {@link com.haulmont.cuba.core.entity.Categorized} implementations.
 */
public class RuntimePropertiesFrame extends AbstractFrame {

    public static final String NAME = "runtimeProperties";
    public static final String DEFAULT_FIELD_WIDTH = "100%";

    protected RuntimePropsDatasource rds;

    protected CollectionDatasource categoriesDs;

    protected boolean requiredControlEnabled = true;

    @Inject
    protected BoxLayout categoryFieldBox;

    @Inject
    protected LookupField categoryField;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected DynamicAttributesTools dynamicAttributesTools;

    @Inject
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;

    @Inject
    protected Security security;

    @WindowParam
    protected String rows;

    @WindowParam
    protected String cols;

    @WindowParam
    protected String fieldWidth;

    @WindowParam
    protected Boolean borderVisible;

    @WindowParam
    protected String fieldCaptionWidth;

    @WindowParam
    protected String[] fieldCaptionWidths;

    @Override
    public void init(Map<String, Object> params) {
        initDatasources(params);

        if (StringUtils.isEmpty(fieldWidth)) {
            fieldWidth = DEFAULT_FIELD_WIDTH;
        }

        initCategoryField();
        loadComponent(rds);
    }

    protected void initDatasources(Map<String, Object> params) {
        String dsId = (String) params.get("runtimeDs");
        if (dsId == null) {
            throw new DevelopmentException("runtimeProperties initialization error: runtimeDs is not provided");
        }
        rds = (RuntimePropsDatasource) getDsContext().get(dsId);
        if (rds == null) {
            throw new DevelopmentException(
                    String.format("runtimeProperties initialization error: runtimeDs '%s' does not exist", dsId));
        }

        String categoriesDsId = (String) params.get("categoriesDs");
        if (categoriesDsId == null) {
            throw new DevelopmentException("runtimeProperties initialization error: categoriesDs is not provided");
        }

        categoriesDs = (CollectionDatasource) getDsContext().get(categoriesDsId);
        if (categoriesDs == null) {
            throw new DevelopmentException(
                    String.format("runtimeProperties initialization error: categoriesDs '%s' does not exist",
                            categoriesDsId)
            );
        }
    }

    protected void initCategoryField() {
        categoryField.setDatasource(rds.getMainDs(), "category");
        categoryField.setOptionsDatasource(categoriesDs);
    }

    @SuppressWarnings("unchecked")
    protected void loadComponent(Datasource ds) {
        ds.addStateChangeListener(e -> {
            if (!Datasource.State.VALID.equals(e.getState())) {
                return;
            }
            createRuntimeFieldGroup(ds);
        });
    }

    protected FieldGroup createRuntimeFieldGroup(Datasource ds) {
        Component runtime = getComponent("runtime");
        if (runtime != null) {
            remove(runtime);
        }

        FieldGroup newRuntimeFieldGroup = uiComponents.create(FieldGroup.class);
        newRuntimeFieldGroup.setBorderVisible(Boolean.TRUE.equals(borderVisible));

        newRuntimeFieldGroup.setWidth("100%");
        newRuntimeFieldGroup.setId("runtime");

        newRuntimeFieldGroup.setFrame(getFrame());
        add(newRuntimeFieldGroup);

        for (FieldGroup.FieldConfig field : newRuntimeFieldGroup.getFields()) {
            newRuntimeFieldGroup.removeField(field);
        }

        List<FieldGroup.FieldConfig> fields = createFieldsForAttributes(newRuntimeFieldGroup);
        addFieldsToFieldGroup(newRuntimeFieldGroup, fields);

        if (!newRuntimeFieldGroup.getFields().isEmpty()) {
            newRuntimeFieldGroup.setDatasource(ds);
            newRuntimeFieldGroup.bind();
        }

        for (FieldGroup.FieldConfig fieldConfig : newRuntimeFieldGroup.getFields()) {
            loadValidators(newRuntimeFieldGroup, fieldConfig);
            loadRequired(newRuntimeFieldGroup, fieldConfig);
            loadEditable(newRuntimeFieldGroup, fieldConfig);
        }

        initFieldCaptionWidth(newRuntimeFieldGroup);

        return newRuntimeFieldGroup;
    }

    protected void initFieldCaptionWidth(FieldGroup newRuntimeFieldGroup) {
        if (fieldCaptionWidth != null) {
            if (fieldCaptionWidth.contains("%")) {
                throw new IllegalStateException("RuntimePropertiesFrame fieldCaptionWidth with '%' unit is unsupported");
            }

            int captionWidth = Integer.parseInt(fieldCaptionWidth.replace("px", ""));

            newRuntimeFieldGroup.setFieldCaptionWidth(captionWidth);
        }
        if (fieldCaptionWidths != null) {
            for (int i = 0; i < fieldCaptionWidths.length; i++) {
                if (fieldCaptionWidths[i].contains("%")) {
                    throw new IllegalStateException("RuntimePropertiesFrame fieldCaptionWidth with '%' unit is unsupported");
                }

                int captionWidth = Integer.parseInt(fieldCaptionWidths[i].replace("px", ""));

                newRuntimeFieldGroup.setFieldCaptionWidth(i, captionWidth);
            }
        }
    }

    protected List<FieldGroup.FieldConfig> createFieldsForAttributes(FieldGroup newRuntimeFieldGroup) {
        @SuppressWarnings("unchecked")
        Collection<DynamicAttributesMetaProperty> metaProperties = rds.getPropertiesFilteredByCategory();
        List<FieldGroup.FieldConfig> fields = new ArrayList<>(metaProperties.size());

        for (DynamicAttributesMetaProperty property : metaProperties) {
            FieldGroup.FieldConfig field = newRuntimeFieldGroup.createField(property.getName());
            field.setProperty(property.getName());
            CategoryAttribute attribute = property.getAttribute();
            if (attribute != null) {
                field.setCaption(attribute.getLocaleName());
                if (StringUtils.isNoneBlank(attribute.getLocaleDescription())) {
                    field.setDescription(attribute.getLocaleDescription());
                }
                if (StringUtils.isNotBlank(attribute.getWidth())) {
                    field.setWidth(attribute.getWidth());
                } else {
                    field.setWidth(fieldWidth);
                }
            } else {
                field.setCaption(property.getName());
                field.setWidth(fieldWidth);
            }
            fields.add(field);
        }
        return fields;
    }

    protected void addFieldsToFieldGroup(FieldGroup newRuntimeFieldGroup, List<FieldGroup.FieldConfig> fields) {
        int rowsPerColumn;
        int propertiesCount = rds.getPropertiesFilteredByCategory().size();
        if (StringUtils.isNotBlank(cols)) {
            if (propertiesCount % Integer.parseInt(cols) == 0) {
                rowsPerColumn = propertiesCount / Integer.parseInt(cols);
            } else {
                rowsPerColumn = propertiesCount / Integer.parseInt(cols) + 1;
            }
        } else if (StringUtils.isNotBlank(rows)) {
            rowsPerColumn = Integer.parseInt(rows);
        } else {
            rowsPerColumn = propertiesCount;
        }

        int columnNo = 0;
        int fieldsCount = 0;
        for (FieldGroup.FieldConfig field : fields) {
            fieldsCount++;
            newRuntimeFieldGroup.addField(field, columnNo);
            if (fieldsCount % rowsPerColumn == 0) {
                columnNo++;
                newRuntimeFieldGroup.setColumns(columnNo + 1);
            }
        }
    }

    protected Consumer getValidator(MetaProperty property) {
        Consumer validator = null;
        if (property.getRange().isDatatype()) {
            Class type = property.getRange().asDatatype().getJavaClass();

            if (type.equals(Integer.class)) {
                validator = new IntegerValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (type.equals(Long.class)) {
                validator = new LongValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (type.equals(Double.class) || type.equals(BigDecimal.class)) {
                validator = new DoubleValidator(messages.getMainMessage("validation.invalidNumber"));

            } else if (type.equals(java.sql.Date.class)) {
                validator = new DateValidator(messages.getMainMessage("validation.invalidDate"));
            }
        }
        return validator;
    }

    protected void loadValidators(FieldGroup fieldGroup, FieldGroup.FieldConfig field) {
        MetaPropertyPath metaPropertyPath = rds.getMetaClass().getPropertyPath(field.getProperty());
        if (metaPropertyPath != null) {
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            Consumer validator = getValidator(metaProperty);

            if (validator != null) {
                field.addValidator(validator);
            }
        }
    }

    protected void loadRequired(FieldGroup fieldGroup, FieldGroup.FieldConfig field) {
        CategoryAttribute attribute = dynamicAttributes.getAttributeForMetaClass(rds.resolveCategorizedEntityClass(), field.getId());
        if (attribute != null) {
            String requiredMessage = messages.formatMainMessage(
                    "validation.required.defaultMsg",
                    attribute.getName()
            );
            field.setRequired(Boolean.TRUE.equals(attribute.getRequired()) && requiredControlEnabled);
            field.setRequiredMessage(requiredMessage);
        }
    }

    protected void loadEditable(FieldGroup fieldGroup, FieldGroup.FieldConfig field) {
        if (fieldGroup.isEditable()) {
            MetaClass metaClass = rds.resolveCategorizedEntityClass();
            MetaPropertyPath propertyPath = dynamicAttributesTools.getMetaPropertyPath(metaClass, field.getProperty());

            checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

            boolean editableFromPermissions = security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString());
            if (!editableFromPermissions) {
                field.setEditable(false);
            }
            boolean visibleFromPermissions = security.isEntityAttrReadPermitted(metaClass, propertyPath.toString());
            if (!visibleFromPermissions) {
                field.setVisible(false);
            }
        }
    }

    public void setCategoryFieldVisible(boolean visible) {
        categoryFieldBox.setVisible(visible);
    }

    public boolean isRequiredControlEnabled() {
        return requiredControlEnabled;
    }

    public void setRequiredControlEnabled(boolean requiredControlEnabled) {
        this.requiredControlEnabled = requiredControlEnabled;
        FieldGroup newRuntime = (FieldGroup) getComponent("runtime");
        if (newRuntime != null) {
            for (final FieldGroup.FieldConfig field : newRuntime.getFields()) {
                loadRequired(newRuntime, field);
            }
        }
    }

    public void setCategoryFieldEditable(boolean editable) {
        categoryField.setEditable(editable);
        FieldGroup newRuntime = (FieldGroup) getComponent("runtime");
        if (newRuntime != null) {
            newRuntime.setEditable(editable);
        }
    }
}