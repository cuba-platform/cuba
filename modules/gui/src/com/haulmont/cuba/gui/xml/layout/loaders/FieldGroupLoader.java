/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author gorodnov
 * @version $Id$
 */
public class FieldGroupLoader extends AbstractFieldLoader {
    public FieldGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {

        final FieldGroup component = factory.createComponent("fieldGroup");

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
            component.setColumns(columnElements.size());

            int colIndex = 0;
            for (final Element columnElement : columnElements) {
                String flex = columnElement.attributeValue("flex");
                if (!StringUtils.isEmpty(flex)) {
                    component.setColumnExpandRatio(colIndex, Float.parseFloat(flex));
                }

                String width = loadThemeString(columnElement.attributeValue("width"));

                final List<FieldGroup.FieldConfig> columnFields = loadFields(component, columnElement, ds);
                for (final FieldGroup.FieldConfig field : columnFields) {
                    component.addField(field, colIndex);
                    if (!StringUtils.isEmpty(width) && field.getWidth() == null) {
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

                    component.setFieldCaptionWidth(colIndex, Integer.valueOf(columnFieldCaptionWidth));
                }

                colIndex++;
            }
        }
        component.setDatasource(ds);

        loadEditable(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadBorder(component, element);

        loadCaptionAlignment(component, element);

        loadFieldCaptionWidth(component, element);

        final List<FieldGroup.FieldConfig> fields = component.getFields();
        for (final FieldGroup.FieldConfig field : fields) {
            if (!field.isCustom()) {
                loadValidators(component, field);
                loadRequired(component, field);
                loadEditable(component, field);
                loadEnable(component, field);
                loadVisible(component, field);
            }
        }

        // deffer attribute loading for custom fields
        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, IFrame window) {
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

        return component;
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

            component.setFieldCaptionWidth(Integer.valueOf(fieldCaptionWidth));
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
        for (final Element fieldElement : elements) {
            fields.add(loadField(component, fieldElement, ds));
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
            if (metaClass.getPropertyPath(id) == null) {
                if (!customField) {
                    throw new GuiDevelopmentException(String.format("Property '%s' is not found in entity '%s'",
                            id, metaClass.getName()), context.getFullFrameId());
                }
            } else {
                metaPropertyPath = metaClass.getPropertyPath(id);
            }
        }

        final FieldGroup.FieldConfig field = new FieldGroup.FieldConfig(id);

        if (datasource != null) {
            field.setDatasource(datasource);
        }

        loadCaption(field, element);
        loadDescription(field, element);

        field.setXmlDescriptor(element);
        if (metaPropertyPath != null) {
            field.setType(metaPropertyPath.getRangeJavaClass());
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
                MetaPropertyPath metaPropertyPath = ds.getMetaClass().getPropertyPath(field.getId());
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
                MetaPropertyPath propertyPath = metaClass.getPropertyPath(field.getId());

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
                MetaPropertyPath propertyPath = metaClass.getPropertyPath(field.getId());

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
                MetaPropertyPath propertyPath = metaClass.getPropertyPath(field.getId());

                checkNotNullArgument(propertyPath, "Could not resolve property path '%s' in '%s'", field.getId(), metaClass);

                boolean editableFromPermissions = security.isEntityAttrUpdatePermitted(metaClass, propertyPath.toString());

                if (!editableFromPermissions) {
                    component.setEditable(field, false);
                    boolean visible = security.isEntityAttrReadPermitted(metaClass, propertyPath.toString());

                    component.setVisible(field, visible);
                } else {
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