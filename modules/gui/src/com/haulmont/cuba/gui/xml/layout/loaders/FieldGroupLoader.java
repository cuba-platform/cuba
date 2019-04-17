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
import com.google.common.collect.Iterables;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.FieldGroup.FieldCaptionAlignment;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.xml.DeclarativeFieldGenerator;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class FieldGroupLoader extends AbstractComponentLoader<FieldGroup> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(FieldGroup.NAME);
        loadId(resultComponent, element);

        // required for border visible
        loadBorder(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        String fieldFactoryBean = element.attributeValue("fieldFactoryBean");
        if (StringUtils.isNotEmpty(fieldFactoryBean)) {
            FieldGroupFieldFactory fieldFactory = beanLocator.get(fieldFactoryBean, FieldGroupFieldFactory.class);
            resultComponent.setFieldFactory(fieldFactory);
        }

        assignXmlDescriptor(resultComponent, element);

        loadVisible(resultComponent, element);
        loadWidth(resultComponent, element);

        loadEditable(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadCss(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);

        loadHeight(resultComponent, element);

        loadAlign(resultComponent, element);

        loadCaptionAlignment(resultComponent, element);

        loadFieldCaptionWidth(resultComponent, element);

        Datasource ds = loadDatasource(element);
        resultComponent.setDatasource(ds);

        if (element.elements("column").isEmpty()) {
            Iterable<FieldGroup.FieldConfig> rootFields = loadFields(resultComponent, element, ds, null);
            Iterable<FieldGroup.FieldConfig> dynamicAttributeFields = loadDynamicAttributeFields(ds);
            for (FieldGroup.FieldConfig field : dynamicAttributeFields) {
                if (resultComponent.getWidth() > 0 && field.getWidth() == null) {
                    field.setWidth("100%");
                }
            }
            for (FieldGroup.FieldConfig field : Iterables.concat(rootFields, dynamicAttributeFields)) {
                resultComponent.addField(field);
            }
        } else {
            List<Element> columnElements = element.elements("column");
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

                String columnWidth = loadThemeString(columnElement.attributeValue("width"));

                Iterable<FieldGroup.FieldConfig> columnFields = loadFields(resultComponent, columnElement, ds, columnWidth);
                if (colIndex == 0) {
                     columnFields = Iterables.concat(columnFields, loadDynamicAttributeFields(ds));
                }
                for (FieldGroup.FieldConfig field : columnFields) {
                    resultComponent.addField(field, colIndex);
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

        for (FieldGroup.FieldConfig field : resultComponent.getFields()) {
            if (!field.isCustom()) {
                if (!DynamicAttributesUtils.isDynamicAttribute(field.getProperty())) {
                    // the following does not make sense for dynamic attributes
                    loadValidators(resultComponent, field);
                    loadRequired(resultComponent, field);
                    loadEnable(resultComponent, field);
                }
                loadVisible(resultComponent, field);
                loadEditable(resultComponent, field);
            }
        }

        resultComponent.bind();

        for (FieldGroup.FieldConfig field : resultComponent.getFields()) {
            if (field.getXmlDescriptor() != null) {
                String generator = field.getXmlDescriptor().attributeValue("generator");
                if (generator != null) {
                    context.addInjectTask((boundContext, window) -> {
                        DeclarativeFieldGenerator fieldGenerator = new DeclarativeFieldGenerator(resultComponent, generator);
                        Component fieldComponent = fieldGenerator.generateField(field.getTargetDatasource(), field.getProperty());
                        field.setComponent(fieldComponent);
                    });
                }
            }
        }
    }

    protected DynamicAttributesGuiTools getDynamicAttributesGuiTools() {
        return beanLocator.get(DynamicAttributesGuiTools.NAME);
    }

    protected MetadataTools getMetadataTools() {
        return beanLocator.get(MetadataTools.NAME);
    }

    protected List<FieldGroup.FieldConfig> loadDynamicAttributeFields(Datasource ds) {
        if (ds != null && getMetadataTools().isPersistent(ds.getMetaClass())) {
            String windowId = getWindowId(context);

            List<CategoryAttribute> attributesToShow =
                    getDynamicAttributesGuiTools().getSortedAttributesToShowOnTheScreen(ds.getMetaClass(),
                            windowId, resultComponent.getId());

            if (!attributesToShow.isEmpty()) {
                List<FieldGroup.FieldConfig> fields = new ArrayList<>();

                ds.setLoadDynamicAttributes(true);

                for (CategoryAttribute attribute : attributesToShow) {
                    FieldGroup.FieldConfig field = resultComponent.createField(
                            DynamicAttributesUtils.encodeAttributeCode(attribute.getCode()));
                    field.setProperty(DynamicAttributesUtils.encodeAttributeCode(attribute.getCode()));
                    field.setCaption(attribute.getLocaleName());
                    field.setDatasource(ds);
                    field.setRequired(attribute.getRequired());
                    field.setRequiredMessage(getMessages().formatMainMessage(
                            "validation.required.defaultMsg",
                            attribute.getLocaleName()));
                    loadWidth(field, attribute.getWidth());

                    fields.add(field);
                }

                getDynamicAttributesGuiTools().listenDynamicAttributesChanges(ds);
                return fields;
            }
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

    protected List<FieldGroup.FieldConfig> loadFields(FieldGroup resultComponent, Element element, Datasource ds,
                                                      @Nullable String columnWidth) {
        List<Element> fieldElements = element.elements("field");
        if (!fieldElements.isEmpty()) {
            return loadFields(resultComponent, fieldElements, ds, columnWidth);
        }
        return Collections.emptyList();
    }

    protected List<FieldGroup.FieldConfig> loadFields(FieldGroup resultComponent, List<Element> elements, Datasource ds,
                                                      @Nullable String columnWidth) {
        List<FieldGroup.FieldConfig> fields = new ArrayList<>(elements.size());
        List<String> ids = new ArrayList<>();
        for (Element fieldElement : elements) {
            FieldGroup.FieldConfig field = loadField(fieldElement, ds, columnWidth);
            if (ids.contains(field.getId())) {
                Map<String, Object> params = new HashMap<>();
                String fieldGroupId = resultComponent.getId();
                if (StringUtils.isNotEmpty(fieldGroupId)) {
                    params.put("FieldGroup ID", fieldGroupId);
                }

                throw new GuiDevelopmentException(
                        String.format("FieldGroup column contains duplicate fields '%s'.", field.getId()),
                        context.getFullFrameId(), params);
            }
            fields.add(field);
            ids.add(field.getId());
        }
        return fields;
    }

    protected CollectionDatasource findDatasourceRecursively(DsContext dsContext, String dsName) {
        if (dsContext == null) {
            return null;
        }

        Datasource datasource = dsContext.get(dsName);
        if (datasource != null && datasource instanceof CollectionDatasource) {
            return (CollectionDatasource) datasource;
        } else {
            if (dsContext.getParent() != null) {
                return findDatasourceRecursively(dsContext.getParent(), dsName);
            } else {
                return null;
            }
        }
    }

    protected FieldGroup.FieldConfig loadField(Element element, Datasource ds, String columnWidth) {
        String id = element.attributeValue("id");
        String property = element.attributeValue("property");

        if (Strings.isNullOrEmpty(id) && Strings.isNullOrEmpty(property)) {
            throw new GuiDevelopmentException(String.format("id/property is not defined for field of FieldGroup '%s'. " +
                    "Set id or property attribute.", resultComponent.getId()), context.getFullFrameId());
        }

        if (Strings.isNullOrEmpty(property)) {
            property = id;
        } else if (Strings.isNullOrEmpty(id)) {
            id = property;
        }

        Datasource targetDs = ds;

        Datasource datasource = loadDatasource(element);
        if (datasource != null) {
            targetDs = datasource;
        }

        CollectionDatasource optionsDs = null;
        String optDsName = element.attributeValue("optionsDatasource");
        if (StringUtils.isNotBlank(optDsName)) {
            LegacyFrame frame = (LegacyFrame) getContext().getFrame().getFrameOwner();
            DsContext dsContext = frame.getDsContext();
            optionsDs = findDatasourceRecursively(dsContext, optDsName);
            if (optionsDs == null) {
                throw new GuiDevelopmentException(String.format("Options datasource %s not found for field %s", optDsName, id)
                        , context.getFullFrameId());
            }
        }

        boolean customField = false;
        String custom = element.attributeValue("custom");
        if (StringUtils.isNotEmpty(custom)) {
            customField = Boolean.parseBoolean(custom);
        }

        if (StringUtils.isNotEmpty(element.attributeValue("generator"))) {
            customField = true;
        }

        List<Element> elements = Dom4j.elements(element);
        List<Element> customElements = elements.stream()
                .filter(e -> !("formatter".equals(e.getName()) || "validator".equals(e.getName())))
                .collect(Collectors.toList());

        if (!customElements.isEmpty()) {
            if (customElements.size() > 1) {
                throw new GuiDevelopmentException(
                        String.format("FieldGroup field %s element cannot contains two or more custom field definitions", id),
                        context.getCurrentFrameId());
            }
            if (customField) {
                throw new GuiDevelopmentException(
                        String.format("FieldGroup field %s cannot use both custom/generator attribute and inline component definition", id),
                        context.getCurrentFrameId());
            }
            customField = true;
        }

        if (!customField && targetDs == null) {
            throw new GuiDevelopmentException(String.format("Datasource is not defined for FieldGroup field '%s'. " +
                    "Only custom fields can have no datasource.", property), context.getFullFrameId());
        }

        FieldGroup.FieldConfig field = resultComponent.createField(id);
        if (property != null) {
            field.setProperty(property);
        }
        if (datasource != null) {
            field.setDatasource(datasource);
        }
        if (optionsDs != null) {
            field.setOptionsDatasource(optionsDs);
        }

        String stylename = element.attributeValue("stylename");
        if (StringUtils.isNotEmpty(stylename)) {
            field.setStyleName(stylename);
        }

        MetaPropertyPath metaPropertyPath = null;
        if (targetDs != null && property != null) {
            MetaClass metaClass = targetDs.getMetaClass();
            metaPropertyPath = getMetadataTools().resolveMetaPropertyPath(targetDs.getMetaClass(), property);
            if (metaPropertyPath == null) {
                if (!customField) {
                    throw new GuiDevelopmentException(String.format("Property '%s' is not found in entity '%s'",
                            property, metaClass.getName()), context.getFullFrameId());
                }
            }
        }
        String propertyName = metaPropertyPath != null ? metaPropertyPath.getMetaProperty().getName() : null;
        if (metaPropertyPath != null && DynamicAttributesUtils.isDynamicAttribute(metaPropertyPath.getMetaProperty())) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaPropertyPath.getMetaProperty());
            field.setCaption(categoryAttribute != null ? categoryAttribute.getLocaleName() : propertyName);
        } else {
            loadCaption(field, element);

            if (field.getCaption() == null) {
                field.setCaption(getDefaultCaption(field, targetDs));
            }
        }
        loadDescription(field, element);
        loadContextHelp(field, element);

        field.setXmlDescriptor(element);

        Function formatter = loadFormatter(element);
        if (formatter != null) {
            field.setFormatter(formatter);
        }

        String defaultWidth = element.attributeValue("width");
        if (StringUtils.isEmpty(defaultWidth)) {
            defaultWidth = columnWidth;
        }
        loadWidth(field, defaultWidth);

        if (customField) {
            field.setCustom(true);
        }

        String required = element.attributeValue("required");
        if (StringUtils.isNotEmpty(required)) {
            field.setRequired(Boolean.parseBoolean(required));
        }

        String requiredMsg = element.attributeValue("requiredMessage");
        if (requiredMsg != null) {
            requiredMsg = loadResourceString(requiredMsg);
            field.setRequiredMessage(requiredMsg);
        }

        String tabIndex = element.attributeValue("tabIndex");
        if (StringUtils.isNotEmpty(tabIndex)) {
            field.setTabIndex(Integer.parseInt(tabIndex));
        }

        loadInputPrompt(field, element);

        if (customElements.size() == 1) {
            // load nested component defined as inline
            Element customFieldElement = customElements.get(0);

            LayoutLoader loader = beanLocator.getPrototype(LayoutLoader.NAME, context);
            loader.setLocale(getLocale());
            loader.setMessagesPack(getMessagesPack());

            ComponentLoader childComponentLoader = loader.createComponent(customFieldElement);
            childComponentLoader.loadComponent();

            Component customComponent = childComponentLoader.getResultComponent();

            String inlineAttachMode = element.attributeValue("inlineAttachMode");
            if (StringUtils.isNotEmpty(inlineAttachMode)) {
                field.setComponent(customComponent, FieldGroup.FieldAttachMode.valueOf(inlineAttachMode));
            } else {
                field.setComponent(customComponent);
            }
        }

        return field;
    }

    protected void loadContextHelp(FieldGroup.FieldConfig field, Element element) {
        String contextHelpText = element.attributeValue("contextHelpText");
        if (StringUtils.isNotEmpty(contextHelpText)) {
            contextHelpText = loadResourceString(contextHelpText);
            field.setContextHelpText(contextHelpText);
        }

        String htmlEnabled = element.attributeValue("contextHelpTextHtmlEnabled");
        if (StringUtils.isNotEmpty(htmlEnabled)) {
            field.setContextHelpTextHtmlEnabled(Boolean.parseBoolean(htmlEnabled));
        }
    }

    protected String getDefaultCaption(FieldGroup.FieldConfig fieldConfig, Datasource fieldDatasource) {
        String caption = fieldConfig.getCaption();
        if (caption == null) {
            String propertyId = fieldConfig.getProperty();
            MetaPropertyPath propertyPath = fieldDatasource != null ?
                    fieldDatasource.getMetaClass().getPropertyPath(propertyId) : null;

            if (propertyPath != null) {
                MetaClass propertyMetaClass = getMetadataTools().getPropertyEnclosingMetaClass(propertyPath);
                String propertyName = propertyPath.getMetaProperty().getName();
                caption = getMessageTools().getPropertyCaption(propertyMetaClass, propertyName);
            }
        }
        return caption;
    }

    protected void loadWidth(FieldGroup.FieldConfig field, String width) {
        if ("auto".equalsIgnoreCase(width)) {
            field.setWidth(Component.AUTO_SIZE);
        } else if (StringUtils.isNotBlank(width)) {
            field.setWidth(loadThemeString(width));
        }
    }

    protected void loadValidators(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element descriptor = field.getXmlDescriptor();
        List<Element> validatorElements = (descriptor == null) ? null : descriptor.elements("validator");
        if (validatorElements != null) {
            if (!validatorElements.isEmpty()) {
                for (Element validatorElement : validatorElements) {
                    Field.Validator validator = loadValidator(validatorElement);
                    if (validator != null) {
                        field.addValidator(validator);
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
                MetaClass metaClass = ds.getMetaClass();
                MetaPropertyPath metaPropertyPath = metaClass.getPropertyPath(field.getProperty());

                if (metaPropertyPath != null) {
                    MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
                    Field.Validator validator = null;
                    if (descriptor == null) {
                        validator = getDefaultValidator(metaProperty);
                    } else if (!"timeField".equals(descriptor.attributeValue("field"))) {
                        validator = getDefaultValidator(metaProperty); //In this case we no need to use validator
                    }

                    if (validator != null) {
                        field.addValidator(validator);
                    }
                }
            }
        }
    }

    protected void loadRequired(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        if (field.isCustom()) {
            Element element = field.getXmlDescriptor();
            if (element == null) {
                return;
            }

            String required = element.attributeValue("required");
            if (StringUtils.isNotEmpty(required)) {
                field.setRequired(Boolean.parseBoolean(required));
            }

            String requiredMessage = element.attributeValue("requiredMessage");
            if (StringUtils.isNotEmpty(requiredMessage)) {
                field.setRequiredMessage(loadResourceString(requiredMessage));
            }
        } else {
            Element element = field.getXmlDescriptor();

            String required = element.attributeValue("required");
            if (StringUtils.isNotEmpty(required)) {
                field.setRequired(Boolean.parseBoolean(required));
            }

            String requiredMsg = element.attributeValue("requiredMessage");
            if (requiredMsg != null) {
                field.setRequiredMessage(loadResourceString(requiredMsg));
            }
        }
    }

    @Override
    protected void loadEditable(Component component, Element element) {
        FieldGroup fieldGroup = (FieldGroup) component;

        if (fieldGroup.getDatasource() != null) {
            MetaClass metaClass = fieldGroup.getDatasource().getMetaClass();
            boolean editableByPermission = (getSecurity().isEntityOpPermitted(metaClass, EntityOp.CREATE)
                    || getSecurity().isEntityOpPermitted(metaClass, EntityOp.UPDATE));
            if (!editableByPermission) {
                fieldGroup.setEditable(false);
                return;
            }
        }

        String editable = element.attributeValue("editable");
        if (StringUtils.isNotEmpty(editable)) {
            fieldGroup.setEditable(Boolean.parseBoolean(editable));
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

    protected void loadEditable(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        if (element != null) {
            String editable = element.attributeValue("editable");
            if (StringUtils.isNotEmpty(editable)) {
                field.setEditable(Boolean.parseBoolean(editable));
            }
        }

        if (!field.isCustom() && BooleanUtils.isNotFalse(field.isEditable())) {
            MetaClass metaClass = getMetaClass(resultComponent, field);
            MetaPropertyPath propertyPath = getMetadataTools().resolveMetaPropertyPath(metaClass, field.getProperty());

            checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

            if (!getSecurity().isEntityAttrUpdatePermitted(metaClass, propertyPath.toString())) {
                field.setEditable(false);
            }
        }
    }

    protected void loadVisible(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        if (element != null) {
            String visible = element.attributeValue("visible");
            if (StringUtils.isNotEmpty(visible)) {
                field.setVisible(Boolean.parseBoolean(visible));
            }
        }

        if (!field.isCustom() && BooleanUtils.isNotFalse(field.isVisible())) {
            MetaClass metaClass = getMetaClass(resultComponent, field);
            MetaPropertyPath propertyPath = getMetadataTools().resolveMetaPropertyPath(metaClass, field.getProperty());

            checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

            if (!getSecurity().isEntityAttrReadPermitted(metaClass, propertyPath.toString())) {
                field.setVisible(false);
            }
        }
    }

    protected void loadEnable(FieldGroup resultComponent, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        if (element != null) {
            String enable = element.attributeValue("enable");
            if (StringUtils.isNotEmpty(enable)) {
                field.setEnabled(Boolean.parseBoolean(enable));
            }
        }
    }

    protected void loadCaptionAlignment(FieldGroup resultComponent, Element element) {
        String captionAlignment = element.attributeValue("captionAlignment");
        if (StringUtils.isNotEmpty(captionAlignment)) {
            resultComponent.setCaptionAlignment(FieldCaptionAlignment.valueOf(captionAlignment));
        }
    }
}