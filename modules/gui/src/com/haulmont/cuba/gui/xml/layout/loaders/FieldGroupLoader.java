/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

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
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author gorodnov
 * @version $Id$
 */
public class FieldGroupLoader extends AbstractFieldLoader {
    protected DynamicAttributes dynamicAttributes = AppBeans.get(DynamicAttributes.NAME);

    public FieldGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final FieldGroup component = (FieldGroup) factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(final FieldGroup component, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);

        assignFrame(component);

        loadVisible(component, element);

        final Datasource ds = loadDatasource(element);
        if (element.elements("column").isEmpty()) {
            final List<FieldGroup.FieldConfig> rootFields = loadFields(component, element, ds);
            for (final FieldGroup.FieldConfig field : rootFields) {
                component.addField(field);
            }
        } else {
            @SuppressWarnings("unchecked")
            final List<Element> columnElements = element.elements("column");
            @SuppressWarnings("unchecked")
            final List<Element> fieldElements = element.elements("field");
            if (fieldElements.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                String fieldGroupId = component.getId();
                if (StringUtils.isNotEmpty(fieldGroupId))
                    params.put("FieldGroup ID", fieldGroupId);
                throw new GuiDevelopmentException("FieldGroup field elements should be placed within its column.", context.getFullFrameId(), params);
            }
            component.setColumns(columnElements.size());

            int colIndex = 0;
            for (final Element columnElement : columnElements) {
                String flex = columnElement.attributeValue("flex");
                if (StringUtils.isNotEmpty(flex)) {
                    component.setColumnExpandRatio(colIndex, Float.parseFloat(flex));
                }

                String width = loadThemeString(columnElement.attributeValue("width"));

                final List<FieldGroup.FieldConfig> columnFields = loadFields(component, columnElement, ds);
                for (final FieldGroup.FieldConfig field : columnFields) {
                    component.addField(field, colIndex);
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

                    component.setFieldCaptionWidth(colIndex, Integer.parseInt(columnFieldCaptionWidth));
                }

                colIndex++;
            }
        }

        addDynamicAttributes(component, ds);

        component.setDatasource(ds);

        loadEditable(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);

        loadHeight(component, element);
        loadWidth(component, element);
        loadAlign(component, element);

        loadBorder(component, element);

        loadCaptionAlignment(component, element);

        loadFieldCaptionWidth(component, element);

        final List<FieldGroup.FieldConfig> fields = component.getFields();
        for (final FieldGroup.FieldConfig field : fields) {
            if (!field.isCustom()) {
                if (!DynamicAttributesUtils.isDynamicAttribute(field.getId())) {//the following does not make sense for dynamic attrs
                    loadValidators(component, field);
                    loadRequired(component, field);
                    loadEnable(component, field);
                    loadVisible(component, field);
                }

                loadEditable(component, field);
            }
        }

        // deffer attribute loading for custom fields
        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, Frame window) {
                final List<FieldGroup.FieldConfig> fields = component.getFields();
                for (final FieldGroup.FieldConfig field : fields) {
                    if (field.isCustom()) {
                        Component fieldComponent = component.getFieldComponent(field);
                        if (fieldComponent != null) {
                            loadValidators(component, field);
                            loadRequired(component, field);
                            loadEditable(component, field);
                            loadEnable(component, field);
                            loadVisible(component, field);
                        } else {
                            LogFactory.getLog(FieldGroupLoader.class).warn(
                                    "Missing component for custom field " + field.getId());
                        }
                    }
                }
            }
        });
    }

    protected void addDynamicAttributes(FieldGroup fieldGroup, Datasource ds) {
        if (ds != null && AppBeans.get(MetadataTools.NAME, MetadataTools.class).isPersistent(ds.getMetaClass())) {
            DynamicAttributesGuiTools dynamicAttributesGuiTools =
                    AppBeans.get(DynamicAttributesGuiTools.NAME, DynamicAttributesGuiTools.class);
            Set<CategoryAttribute> attributesToShow =
                    dynamicAttributesGuiTools.getAttributesToShowOnTheScreen(ds.getMetaClass(), context.getFullFrameId(), fieldGroup.getId());
            if (CollectionUtils.isNotEmpty(attributesToShow)) {
                ds.setLoadDynamicAttributes(true);
                for (CategoryAttribute attribute : attributesToShow) {
                    MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(ds.getMetaClass(), attribute);
                    final FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(
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

                    if (fieldGroup.getWidth() > 0) {
                        field.setWidth("100%");
                    } else {
                        field.setWidth(FieldGroup.DEFAULT_FIELD_WIDTH);
                    }
                    fieldGroup.addField(field);
                }
            }

            dynamicAttributesGuiTools.listenDynamicAttributesChanges(ds);
        }
    }

    protected void loadFieldCaptionWidth(FieldGroup component, Element element) {
        String fieldCaptionWidth = element.attributeValue("fieldCaptionWidth");
        if (StringUtils.isNotEmpty(fieldCaptionWidth)) {
            if (fieldCaptionWidth.startsWith(MessageTools.MARK)) {
                fieldCaptionWidth = loadResourceString(fieldCaptionWidth);
            }
            if (fieldCaptionWidth.endsWith("px")) {
                fieldCaptionWidth = fieldCaptionWidth.substring(0, fieldCaptionWidth.indexOf("px"));
            }

            component.setFieldCaptionWidth(Integer.parseInt(fieldCaptionWidth));
        }
    }

    protected Datasource loadDatasource(Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            Datasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getFullFrameId());
            }
            return ds;
        }
        return null;
    }

    protected List<FieldGroup.FieldConfig> loadFields(FieldGroup component, Element element, Datasource ds) {
        @SuppressWarnings("unchecked")
        final List<Element> fieldElements = element.elements("field");
        if (!fieldElements.isEmpty()) {
            return loadFields(component, fieldElements, ds);
        }
        return Collections.emptyList();
    }

    protected List<FieldGroup.FieldConfig> loadFields(FieldGroup component, List<Element> elements, Datasource ds) {
        final List<FieldGroup.FieldConfig> fields = new ArrayList<>(elements.size());
        List<String> ids = new ArrayList<>();
        for (final Element fieldElement : elements) {
            FieldGroup.FieldConfig field = loadField(component, fieldElement, ds);
            if (ids.contains(field.getId())) {
                Map<String, Object> params = new HashMap<>();
                String fieldGroupId = component.getId();
                if (StringUtils.isNotEmpty(fieldGroupId))
                    params.put("FieldGroup ID", fieldGroupId);
                throw new GuiDevelopmentException(String.format("FieldGroup column contains duplicate fields '%s'.", field.getId()), context.getFullFrameId(), params);
            }
            fields.add(field);
            ids.add(field.getId());
        }
        return fields;
    }

    protected FieldGroup.FieldConfig loadField(FieldGroup component, Element element, Datasource ds) {
        final String id = element.attributeValue("id");

        final Datasource datasource = loadDatasource(element);
        if (datasource != null) {
            ds = datasource;
        }

        boolean customField = false;
        String custom = element.attributeValue("custom");
        if (!StringUtils.isEmpty(custom)) {
            customField = BooleanUtils.toBoolean(custom);
        }

        if (!customField && ds == null)
            throw new GuiDevelopmentException(String.format("Datasource is not defined for FieldGroup field '%s'. " +
                    "Only custom fields can have no datasource.", id), context.getFullFrameId());

        MetaPropertyPath metaPropertyPath = null;

        if (ds != null) {
            final MetaClass metaClass = ds.getMetaClass();
            metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                    .resolveMetaPropertyPath(ds.getMetaClass(), id);
            if (metaPropertyPath == null) {
                if (!customField) {
                    throw new GuiDevelopmentException(String.format("Property '%s' is not found in entity '%s'",
                            id, metaClass.getName()), context.getFullFrameId());
                }
            }
        }

        final FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(id);
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
            field.setRequired(BooleanUtils.toBoolean(required));
        }

        String requiredMsg = element.attributeValue("requiredMessage");
        if (requiredMsg != null) {
            requiredMsg = loadResourceString(requiredMsg);
            field.setRequiredError(requiredMsg);
        }

        return field;
    }

    protected void loadValidators(FieldGroup component, FieldGroup.FieldConfig field) {
        Element descriptor = field.getXmlDescriptor();
        @SuppressWarnings("unchecked")
        final List<Element> validatorElements = (descriptor == null) ? null : descriptor.elements("validator");
        if (validatorElements != null) {
            if (!validatorElements.isEmpty()) {
                for (Element validatorElement : validatorElements) {
                    final Field.Validator validator = loadValidator(validatorElement);
                    if (validator != null) {
                        component.addValidator(field, validator);
                    }
                }
            }
        } else {
            Datasource ds;
            if (field.getDatasource() == null) {
                ds = component.getDatasource();
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
                        component.addValidator(field, validator);
                    }
                }
            }
        }
    }

    protected void loadRequired(FieldGroup component, FieldGroup.FieldConfig field) {
        if (!field.isCustom()) {
            boolean isMandatory = false;
            MetaClass metaClass = getMetaClass(component, field);
            if (metaClass != null) {
                MetaPropertyPath propertyPath = field.getMetaPropertyPath();

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                MetaProperty metaProperty = propertyPath.getMetaProperty();
                isMandatory = metaProperty.isMandatory();
            }

            Element element = field.getXmlDescriptor();
            final String required = element.attributeValue("required");

            if (StringUtils.isNotEmpty(required)) {
                isMandatory = BooleanUtils.toBoolean(required);
            }

            String requiredMsg = element.attributeValue("requiredMessage");
            if (StringUtils.isEmpty(requiredMsg) && metaClass != null) {
                MetaPropertyPath propertyPath = field.getMetaPropertyPath();

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                MetaProperty metaProperty = propertyPath.getMetaProperty();
                requiredMsg = messageTools.getDefaultRequiredMessage(metaProperty);
            }

            String requiredError = loadResourceString(requiredMsg);
            component.setRequired(field, isMandatory, requiredError);
        } else {
            Element element = field.getXmlDescriptor();
            if (element == null)
                return;

            String required = element.attributeValue("required");

            if (StringUtils.isNotEmpty(required)) {
                component.setRequired(field, BooleanUtils.toBoolean(required),
                        loadResourceString(element.attributeValue("requiredMessage")));
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
        final String editable = element.attributeValue("editable");
        if (!StringUtils.isEmpty(editable)) {
            fieldGroup.setEditable(BooleanUtils.toBoolean(editable));
        }
    }

    protected void loadEditable(FieldGroup component, FieldGroup.FieldConfig field) {
        if (field.isCustom()) {
            Element element = field.getXmlDescriptor();
            final String editable = element.attributeValue("editable");
            if (!StringUtils.isEmpty(editable)) {
                component.setEditable(field, BooleanUtils.toBoolean(editable));
            }
        } else {
            if (component.isEditable()) {
                MetaClass metaClass = getMetaClass(component, field);
                MetaPropertyPath propertyPath = field.getMetaPropertyPath();

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                boolean editableFromPermissions = security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString());

                if (!editableFromPermissions) {
                    component.setEditable(field, false);
                    boolean visible = security.isEntityAttrReadPermitted(metaClass, propertyPath.toString());

                    component.setVisible(field, visible);
                } else if (!DynamicAttributesUtils.isDynamicAttribute(propertyPath.getMetaProperty())) {
                    Element element = field.getXmlDescriptor();
                    final String editable = element.attributeValue("editable");
                    if (!StringUtils.isEmpty(editable)) {
                        component.setEditable(field, BooleanUtils.toBoolean(editable));
                    }
                }
            }
        }
    }

    protected MetaClass getMetaClass(FieldGroup component, FieldGroup.FieldConfig field) {
        if (field.isCustom()) return null;
        Datasource datasource;
        if (field.getDatasource() != null) {
            datasource = field.getDatasource();
        } else if (component.getDatasource() != null) {
            datasource = component.getDatasource();
        } else {
            throw new GuiDevelopmentException(String.format("Unable to get datasource for field '%s'",
                    field.getId()), context.getFullFrameId());
        }
        return datasource.getMetaClass();
    }

    protected void loadEnable(FieldGroup component, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        final String enable = element.attributeValue("enable");
        if (!StringUtils.isEmpty(enable)) {
            component.setEnabled(field, component.isEnabled() && BooleanUtils.toBoolean(enable));
        }
    }

    protected void loadVisible(FieldGroup component, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        final String visible = element.attributeValue("visible");
        if (!StringUtils.isEmpty(visible)) {
            component.setVisible(field, component.isVisible() && BooleanUtils.toBoolean(visible));
        }
    }

    protected void loadCaptionAlignment(FieldGroup component, Element element) {
        String captionAlignment = element.attributeValue("captionAlignment");
        if (!StringUtils.isEmpty(captionAlignment)) {
            component.setCaptionAlignment(FieldGroup.FieldCaptionAlignment.valueOf(captionAlignment));
        }
    }
}