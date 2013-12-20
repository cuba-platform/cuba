/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            final List<Element> columnElements = element.elements("column");
            component.setColumns(columnElements.size());

            int colIndex = 0;
            for (final Element columnElement : columnElements) {
                String flex = columnElement.attributeValue("flex");
                if (!StringUtils.isEmpty(flex)) {
                    component.setColumnExpandRatio(colIndex, Float.parseFloat(flex));
                }

                String width = columnElement.attributeValue("width");

                final List<FieldGroup.FieldConfig> columnFields = loadFields(component, columnElement, ds);
                for (final FieldGroup.FieldConfig field : columnFields) {
                    component.addField(field, colIndex);
                    if (!StringUtils.isEmpty(width) && field.getWidth() == null) {
                        field.setWidth(width);
                    }
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

        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, IFrame window) {
                final List<FieldGroup.FieldConfig> fields = component.getFields();
                for (final FieldGroup.FieldConfig field : fields) {
                    loadValidators(component, field);
                    loadRequired(component, field);
                    loadEditable(component, field);
                    loadEnabled(component, field);
                    loadVisible(component, field);
                }
            }
        });

        return component;
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

        final MetaClass metaClass = ds.getMetaClass();
        if (metaClass.getPropertyPath(id) == null) {
            if (!customField) {
                throw new GuiDevelopmentException(String.format("Property '%s' is not found in entity '%s'",
                        id, metaClass.getName()), context.getFullFrameId());
            }
        } else {
            metaPropertyPath = metaClass.getPropertyPath(id);
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

        String width = element.attributeValue("width");
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
            MetaClass metaClass = metaClass(component, field);
            if (metaClass != null) {
                MetaProperty metaProperty = metaClass.getPropertyPath(field.getId()).getMetaProperty();
                isMandatory = metaProperty.isMandatory();
            }

            Element element = field.getXmlDescriptor();
            final String required = element.attributeValue("required");

            if (StringUtils.isNotEmpty(required)) {
                isMandatory = BooleanUtils.toBoolean(required);
            }

            String requiredMsg = element.attributeValue("requiredMessage");
            if (StringUtils.isEmpty(requiredMsg) && metaClass != null) {
                MetaProperty metaProperty = metaClass.getPropertyPath(field.getId()).getMetaProperty();
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
            UserSession userSession = userSessionSource.getUserSession();
            boolean editable = (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                    || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE));
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
                MetaClass metaClass = metaClass(component, field);
                MetaProperty metaProperty = metaClass.getPropertyPath(field.getId()).getMetaProperty();

                UserSession userSession = userSessionSource.getUserSession();
                boolean editableFromPermissions = (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                        || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                        && userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);

                if (!editableFromPermissions) {
                    component.setEditable(field, false);
                    boolean visible = userSession.isEntityAttrPermitted(metaClass,
                            metaProperty.getName(), EntityAttrAccess.VIEW);

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

    protected MetaClass metaClass(FieldGroup component, FieldGroup.FieldConfig field) {
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

    protected void loadEnabled(FieldGroup component, FieldGroup.FieldConfig field) {
        Element element = field.getXmlDescriptor();
        final String enabled = element.attributeValue("enabled");
        if (!StringUtils.isEmpty(enabled)) {
            component.setEnabled(field, component.isEnabled() && BooleanUtils.toBoolean(enabled));
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