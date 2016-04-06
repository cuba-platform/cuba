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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 */
public class FieldGroupLoader extends AbstractComponentLoader<FieldGroup> {

    protected DynamicAttributes dynamicAttributes = AppBeans.get(DynamicAttributes.NAME);
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.NAME, DynamicAttributesGuiTools.class);
    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME, MetadataTools.class);

    protected List<FieldGroup.FieldConfig> loadDynamicAttributeFields(Datasource ds) {
        if (ds != null && metadataTools.isPersistent(ds.getMetaClass())) {
            Set<CategoryAttribute> attributesToShow = dynamicAttributesGuiTools.getAttributesToShowOnTheScreen(ds.getMetaClass(),
                    getFrameId(), resultComponent.getId());
            if (CollectionUtils.isNotEmpty(attributesToShow)) {
                List<FieldGroup.FieldConfig> fields = new ArrayList<>();
                        ds.setLoadDynamicAttributes(true);
                for (CategoryAttribute attribute : attributesToShow) {
                    MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(ds.getMetaClass(), attribute);
                    FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(
                            DynamicAttributesUtils.encodeAttributeCode(attribute.getCode()));
                    field.setMetaPropertyPath(metaPropertyPath);
                    field.setType(metaPropertyPath.getRangeJavaClass());
                    field.setCaption(attribute.getName());
                    field.setDatasource(ds);
                    field.setRequired(attribute.getRequired());
                    field.setRequiredError(messages.formatMessage(
                            messages.getMainMessagePack(),
                            "validation.required.defaultMsg",
                            attribute.getName()));
                    fields.add(field);
                }
                return fields;
            }
            dynamicAttributesGuiTools.listenDynamicAttributesChanges(ds);
        }
        return Collections.emptyList();
    }

    protected void loadFieldCaptionWidth(FieldGroup resultComponent, Element element) {
        String fieldCaptionWidth = element.attributeValue("fieldCaptionWidth");
        if (StringUtils.isNotEmpty(fieldCaptionWidth)) {
            if (fieldCaptionWidth.startsWith(MessageTools.MARK)) {
                fieldCaptionWidth = loadResourceString(fieldCaptionWidth);
            }
            if (fieldCaptionWidth.endsWith("px")) {
                fieldCaptionWidth = fieldCaptionWidth.substring(0, fieldCaptionWidth.indexOf("px"));
            }

            resultComponent.setFieldCaptionWidth(Integer.parseInt(fieldCaptionWidth));
        }
    }

    protected Datasource loadDatasource(Element element) {
        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            Datasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getFullFrameId());
            }
            return ds;
        }
        return null;
    }

    protected List<FieldGroup.FieldConfig> loadFields(FieldGroup resultComponent, Element element, Datasource ds, boolean addDynamicAttributes) {
        @SuppressWarnings("unchecked")
        List<Element> fieldElements = element.elements("field");
        if (!fieldElements.isEmpty()) {
            List<FieldGroup.FieldConfig> fields = loadFields(resultComponent, fieldElements, ds);
            if (addDynamicAttributes) {
                fields.addAll(loadDynamicAttributeFields(ds));
            }
            return fields;
        }
        return addDynamicAttributes ? loadDynamicAttributeFields(ds) : Collections.emptyList();
    }

    protected List<FieldGroup.FieldConfig> loadFields(FieldGroup resultComponent, List<Element> elements, Datasource ds) {
        List<FieldGroup.FieldConfig> fields = new ArrayList<>(elements.size());
        List<String> ids = new ArrayList<>();
        for (Element fieldElement : elements) {
            FieldGroup.FieldConfig field = loadField(fieldElement, ds);
            if (ids.contains(field.getId())) {
                Map<String, Object> params = new HashMap<>();
                String fieldGroupId = resultComponent.getId();
                if (StringUtils.isNotEmpty(fieldGroupId)) {
                    params.put("FieldGroup ID", fieldGroupId);
                }

                throw new GuiDevelopmentException(String.format("FieldGroup column contains duplicate fields '%s'.", field.getId()), context.getFullFrameId(), params);
            }
            fields.add(field);
            ids.add(field.getId());
        }
        return fields;
    }

    protected FieldGroup.FieldConfig loadField(Element element, Datasource ds) {
        String id = element.attributeValue("id");

        Datasource datasource = loadDatasource(element);
        if (datasource != null) {
            ds = datasource;
        }

        boolean customField = false;
        String custom = element.attributeValue("custom");
        if (StringUtils.isNotEmpty(custom)) {
            customField = Boolean.parseBoolean(custom);
        }

        if (!customField && ds == null) {
            throw new GuiDevelopmentException(String.format("Datasource is not defined for FieldGroup field '%s'. " +
                    "Only custom fields can have no datasource.", id), context.getFullFrameId());
        }

        MetaPropertyPath metaPropertyPath = null;

        if (ds != null) {
            MetaClass metaClass = ds.getMetaClass();
            metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                    .resolveMetaPropertyPath(ds.getMetaClass(), id);
            if (metaPropertyPath == null) {
                if (!customField) {
                    throw new GuiDevelopmentException(String.format("Property '%s' is not found in entity '%s'",
                            id, metaClass.getName()), context.getFullFrameId());
                }
            }
        }

        FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(id);
        field.setMetaPropertyPath(metaPropertyPath);
        if (datasource != null) {
            field.setDatasource(datasource);
        }

        String propertyName = metaPropertyPath != null ? metaPropertyPath.getMetaProperty().getName() : null;
        if (metaPropertyPath != null && DynamicAttributesUtils.isDynamicAttribute(metaPropertyPath.getMetaProperty())) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaPropertyPath.getMetaProperty());
            field.setCaption(categoryAttribute != null ? categoryAttribute.getName() : propertyName);
        } else {
            loadCaption(field, element);
        }
        loadDescription(field, element);

        field.setXmlDescriptor(element);
        if (field.getMetaPropertyPath() != null) {
            field.setType(field.getMetaPropertyPath().getRangeJavaClass());
        }

        field.setFormatter(loadFormatter(element));

        String width = loadThemeString(element.attributeValue("width"));
        if (!StringUtils.isEmpty(width)) {
            field.setWidth(width);
        }

        field.setCustom(customField);

        String required = element.attributeValue("required");
        if (StringUtils.isNotEmpty(required)) {
            field.setRequired(Boolean.parseBoolean(required));
        }

        String requiredMsg = element.attributeValue("requiredMessage");
        if (requiredMsg != null) {
            requiredMsg = loadResourceString(requiredMsg);
            field.setRequiredError(requiredMsg);
        }

        return field;
    }

    protected void loadValidators(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element descriptor = field.getXmlDescriptor();
        @SuppressWarnings("unchecked")
        List<Element> validatorElements = (descriptor == null) ? null : descriptor.elements("validator");
        if (validatorElements != null) {
            if (!validatorElements.isEmpty()) {
                for (Element validatorElement : validatorElements) {
                    Field.Validator validator = loadValidator(validatorElement);
                    if (validator != null) {
                        resultComponent.addValidator(field, validator);
                    }
                }
            }
        } else {
            Datasource ds;
            if (field.getDatasource() == null) {
                ds = resultComponent.getDatasource();
            } else {
                ds = field.getDatasource();
            }

            if (ds != null) {
                MetaPropertyPath metaPropertyPath = field.getMetaPropertyPath();
                if (metaPropertyPath != null) {
                    MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
                    Field.Validator validator = null;
                    if (descriptor == null) {
                        validator = getDefaultValidator(metaProperty);
                    } else if (!"timeField".equals(descriptor.attributeValue("field"))) {
                        validator = getDefaultValidator(metaProperty); //In this case we no need to use validator
                    }

                    if (validator != null) {
                        resultComponent.addValidator(field, validator);
                    }
                }
            }
        }
    }

    protected void loadRequired(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        if (!field.isCustom()) {
            boolean isMandatory = false;
            MetaClass metaClass = getMetaClass(resultComponent, field);
            if (metaClass != null) {
                MetaPropertyPath propertyPath = field.getMetaPropertyPath();

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                MetaProperty metaProperty = propertyPath.getMetaProperty();
                isMandatory = metaProperty.isMandatory();
            }

            Element element = field.getXmlDescriptor();

            String required = element.attributeValue("required");
            if (StringUtils.isNotEmpty(required)) {
                isMandatory = Boolean.parseBoolean(required);
            }

            String requiredMsg = element.attributeValue("requiredMessage");
            if (StringUtils.isEmpty(requiredMsg) && metaClass != null) {
                MetaPropertyPath propertyPath = field.getMetaPropertyPath();

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                MetaProperty metaProperty = propertyPath.getMetaProperty();
                requiredMsg = messageTools.getDefaultRequiredMessage(metaProperty);
            }

            String requiredError = loadResourceString(requiredMsg);
            resultComponent.setRequired(field, isMandatory, requiredError);
        } else {
            Element element = field.getXmlDescriptor();
            if (element == null) {
                return;
            }

            String required = element.attributeValue("required");
            if (StringUtils.isNotEmpty(required)) {
                resultComponent.setRequired(field, Boolean.parseBoolean(required));

                String requiredMessage = element.attributeValue("requiredMessage");
                if (!Strings.isNullOrEmpty(requiredMessage)) {
                    resultComponent.setRequiredMessage(field, loadResourceString(requiredMessage));
                }
            }
        }
    }

    @Override
    protected void loadEditable(Component component, Element element) {
        FieldGroup fieldGroup = (FieldGroup) component;
        if (fieldGroup.getDatasource() != null) {
            MetaClass metaClass = fieldGroup.getDatasource().getMetaClass();
            boolean editable = (security.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                    || security.isEntityOpPermitted(metaClass, EntityOp.UPDATE));
            if (!editable) {
                ((Component.Editable) component).setEditable(false);
                return;
            }
        }
        String editable = element.attributeValue("editable");
        if (StringUtils.isNotEmpty(editable)) {
            fieldGroup.setEditable(Boolean.parseBoolean(editable));
        }
    }

    protected void loadEditable(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        if (field.isCustom()) {
            Element element = field.getXmlDescriptor();
            String editable = element.attributeValue("editable");
            if (StringUtils.isNotEmpty(editable)) {
                resultComponent.setEditable(field, Boolean.parseBoolean(editable));
            }
        } else {
            if (resultComponent.isEditable()) {
                MetaClass metaClass = getMetaClass(resultComponent, field);
                MetaPropertyPath propertyPath = field.getMetaPropertyPath();

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                boolean editableFromPermissions = security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString());

                if (!editableFromPermissions) {
                    resultComponent.setEditable(field, false);
                    boolean visible = security.isEntityAttrReadPermitted(metaClass, propertyPath.toString());

                    resultComponent.setVisible(field, visible);
                } else if (!DynamicAttributesUtils.isDynamicAttribute(propertyPath.getMetaProperty())) {
                    Element element = field.getXmlDescriptor();
                    String editable = element.attributeValue("editable");
                    if (StringUtils.isNotEmpty(editable)) {
                        resultComponent.setEditable(field, Boolean.parseBoolean(editable));
                    }
                }
            }
        }
    }

    protected MetaClass getMetaClass(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        if (field.isCustom()) {
            return null;
        }
        Datasource datasource;
        if (field.getDatasource() != null) {
            datasource = field.getDatasource();
        } else if (resultComponent.getDatasource() != null) {
            datasource = resultComponent.getDatasource();
        } else {
            throw new GuiDevelopmentException(String.format("Unable to get datasource for field '%s'",
                    field.getId()), context.getFullFrameId());
        }
        return datasource.getMetaClass();
    }

    protected void loadEnable(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        String enable = element.attributeValue("enable");
        if (StringUtils.isNotEmpty(enable)) {
            resultComponent.setEnabled(field, resultComponent.isEnabled() && Boolean.parseBoolean(enable));
        }
    }

    protected void loadVisible(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        String visible = element.attributeValue("visible");
        if (StringUtils.isNotEmpty(visible)) {
            resultComponent.setVisible(field, resultComponent.isVisible() && Boolean.parseBoolean(visible));
        }
    }

    protected void loadCaptionAlignment(FieldGroup resultComponent, Element element) {
        String captionAlignment = element.attributeValue("captionAlignment");
        if (!StringUtils.isEmpty(captionAlignment)) {
            resultComponent.setCaptionAlignment(FieldGroup.FieldCaptionAlignment.valueOf(captionAlignment));
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (FieldGroup) factory.createComponent(FieldGroup.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);
        assignXmlDescriptor(resultComponent, element);

        loadVisible(resultComponent, element);

        Datasource ds = loadDatasource(element);
        if (element.elements("column").isEmpty()) {
            List<FieldGroup.FieldConfig> rootFields = loadFields(resultComponent, element, ds, true);
            for (FieldGroup.FieldConfig field : rootFields) {
                resultComponent.addField(field);
            }
        } else {
            @SuppressWarnings("unchecked")
            List<Element> columnElements = element.elements("column");
            @SuppressWarnings("unchecked")
            List<Element> fieldElements = element.elements("field");
            if (fieldElements.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                String fieldGroupId = resultComponent.getId();
                if (StringUtils.isNotEmpty(fieldGroupId)) {
                    params.put("FieldGroup ID", fieldGroupId);
                }
                throw new GuiDevelopmentException("FieldGroup field elements should be placed within its column.", context.getFullFrameId(), params);
            }
            resultComponent.setColumns(columnElements.size());

            int colIndex = 0;
            for (Element columnElement : columnElements) {
                String flex = columnElement.attributeValue("flex");
                if (StringUtils.isNotEmpty(flex)) {
                    resultComponent.setColumnExpandRatio(colIndex, Float.parseFloat(flex));
                }

                String width = loadThemeString(columnElement.attributeValue("width"));

                List<FieldGroup.FieldConfig> columnFields = loadFields(resultComponent, columnElement, ds, colIndex == 0);
                for (FieldGroup.FieldConfig field : columnFields) {
                    resultComponent.addField(field, colIndex);
                    if (StringUtils.isNotEmpty(width) && field.getWidth() == null) {
                        field.setWidth(width);
                    }
                }

                String columnFieldCaptionWidth = columnElement.attributeValue("fieldCaptionWidth");
                if (StringUtils.isNotEmpty(columnFieldCaptionWidth)) {
                    if (columnFieldCaptionWidth.startsWith(MessageTools.MARK)) {
                        columnFieldCaptionWidth = loadResourceString(columnFieldCaptionWidth);
                    }
                    if (columnFieldCaptionWidth.endsWith("px")) {
                        columnFieldCaptionWidth = columnFieldCaptionWidth.substring(0, columnFieldCaptionWidth.indexOf("px"));
                    }

                    resultComponent.setFieldCaptionWidth(colIndex, Integer.parseInt(columnFieldCaptionWidth));
                }

                colIndex++;
            }
        }

        resultComponent.setDatasource(ds);

        loadEditable(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);

        loadBorder(resultComponent, element);

        loadCaptionAlignment(resultComponent, element);

        loadFieldCaptionWidth(resultComponent, element);

        for (FieldGroup.FieldConfig field : resultComponent.getFields()) {
            if (!field.isCustom()) {
                if (!DynamicAttributesUtils.isDynamicAttribute(field.getId())) {//the following does not make sense for dynamic attrs
                    loadValidators(resultComponent, field);
                    loadRequired(resultComponent, field);
                    loadEnable(resultComponent, field);
                    loadVisible(resultComponent, field);
                }
                loadEditable(resultComponent, field);
            }
        }

        // deffer attribute loading for custom fields
        context.addPostInitTask((context1, window) -> {
            for (FieldGroup.FieldConfig field : resultComponent.getFields()) {
                if (field.isCustom()) {
                    Component fieldComponent = resultComponent.getFieldComponent(field);
                    if (fieldComponent != null) {
                        loadValidators(resultComponent, field);
                        loadRequired(resultComponent, field);
                        loadEditable(resultComponent, field);
                        loadEnable(resultComponent, field);
                        loadVisible(resultComponent, field);
                    } else {
                        LogFactory.getLog(FieldGroupLoader.class).warn(
                                "Missing component for custom field " + field.getId());
                    }
                }
            }
        });
    }

    protected String getFrameId() {
        Context context = getContext();
        while (context.getParent() != null) {
            context = context.getParent();
        }
        return context.getFullFrameId();
    }
}