/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 23.06.2010 11:49:55
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.UserSessionClient;
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

@SuppressWarnings("serial")
public class FieldGroupLoader extends AbstractFieldLoader {
    public FieldGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final FieldGroup component = factory.createComponent("fieldGroup");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        assignFrame(component);

        loadVisible(component, element);

        final Datasource ds = loadDatasource(element);
        if (element.elements("column").isEmpty()) {
            final List<FieldGroup.Field> rootFields = loadFields(component, element, ds);
            for (final FieldGroup.Field field : rootFields) {
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

                final List<FieldGroup.Field> columnFields = loadFields(component, columnElement, ds);
                for (final FieldGroup.Field field : columnFields) {
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

        loadCollapsable(component, element);
        loadExpandable(component, element);

        loadCaptionAlignment(component, element);

        context.addPostInitTask(new PostInitTask() {
            public void execute(Context context, IFrame window) {
                component.postInit();
                final List<FieldGroup.Field> fields = component.getFields();
                for (final FieldGroup.Field field : fields) {
                    loadValidators(component, field);
                    loadRequired(component, field);
                    loadEditable(component, field);
                    loadEnabled(component, field);
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
                throw new IllegalStateException("Cannot find data source by name: " + datasource);
            }
            return ds;
        }
        return null;
    }

    protected List<FieldGroup.Field> loadFields(FieldGroup component, Element element, Datasource ds) {
        final List<Element> fieldElements = element.elements("field");
        if (!fieldElements.isEmpty()) {
            return loadFields(component, fieldElements, ds);
        }
        return Collections.emptyList();
    }

    protected List<FieldGroup.Field> loadFields(FieldGroup component, List<Element> elements, Datasource ds) {
        final List<FieldGroup.Field> fields = new ArrayList<FieldGroup.Field>(elements.size());
        for (final Element fieldElement : elements) {
            String visible = fieldElement.attributeValue("visible");
            if (visible == null) {
                final Element e = fieldElement.element("visible");
                if (e != null) {
                    visible = e.getText();
                }
            }

            if (StringUtils.isEmpty(visible) || evaluateBoolean(visible)) {
                fields.add(loadField(component, fieldElement, ds));
            }
        }
        return fields;
    }

    protected FieldGroup.Field loadField(FieldGroup component, Element element, Datasource ds) {
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

        MetaPropertyPath metaPropertyPath = null;

        final MetaClass metaClass = ds.getMetaClass();
        if (metaClass.getPropertyPath(id) == null) {
            if (!customField) {
                throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'",
                        id, metaClass.getName()));
            }
        } else {
            metaPropertyPath = metaClass.getPropertyPath(id);
        }

        final FieldGroup.Field field = new FieldGroup.Field(id);

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

        return field;
    }

    protected void loadValidators(FieldGroup component, FieldGroup.Field field) {
        Element descriptor = field.getXmlDescriptor();
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

    protected void loadRequired(FieldGroup component, FieldGroup.Field field) {
        Element element = field.getXmlDescriptor();
        final String required = element.attributeValue("required");
        if (!StringUtils.isEmpty(required)) {
            String requiredMsg = element.attributeValue("requiredMessage");
            if (StringUtils.isEmpty(requiredMsg)) {
                MetaClass metaClass = metaClass(component, field);
                if (metaClass != null) {
                    MetaProperty metaProperty = metaClass.getPropertyPath(field.getId()).getMetaProperty();
                    requiredMsg = MessageProvider.formatMessage(
                            messagesPack,
                            "validation.required.defaultMsg",
                            MessageUtils.getPropertyCaption(metaProperty)
                    );
                }
            }
            component.setRequired(field, BooleanUtils.toBoolean(required), loadResourceString(requiredMsg));
        }
    }

    @Override
    protected void loadEditable(Component component, Element element) {
        FieldGroup fieldGroup = (FieldGroup) component;
        if (fieldGroup.getDatasource() != null) {
            MetaClass metaClass = fieldGroup.getDatasource().getMetaClass();
            UserSession userSession = UserSessionClient.getUserSession();
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

    protected void loadEditable(FieldGroup component, FieldGroup.Field field) {
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

                UserSession userSession = UserSessionClient.getUserSession();
                boolean b = (userSession.isEntityOpPermitted(metaClass, EntityOp.CREATE)
                        || userSession.isEntityOpPermitted(metaClass, EntityOp.UPDATE))
                        && userSession.isEntityAttrPermitted(metaClass, metaProperty.getName(), EntityAttrAccess.MODIFY);

                if (!b) {
                    component.setEditable(field, false);
                    return;
                }

                Element element = field.getXmlDescriptor();
                final String editable = element.attributeValue("editable");
                if (!StringUtils.isEmpty(editable)) {
                    component.setEditable(field, BooleanUtils.toBoolean(editable));
                }
            } else {
                component.setEditable(field, false);
            }
        }
    }

    private MetaClass metaClass(FieldGroup component, FieldGroup.Field field) {
        if (field.isCustom()) return null;
        Datasource datasource;
        if (field.getDatasource() != null) {
            datasource = field.getDatasource();
        } else if (component.getDatasource() != null) {
            datasource = component.getDatasource();
        } else {
            throw new IllegalStateException(String.format("Unable to get datasource for field '%s'",
                    field.getId()));
        }
        return datasource.getMetaClass();
    }

    protected void loadEnabled(FieldGroup component, FieldGroup.Field field) {
        Element element = field.getXmlDescriptor();
        final String enabled = element.attributeValue("enabled");
        if (!StringUtils.isEmpty(enabled)) {
            component.setEnabled(field, component.isEnabled() && BooleanUtils.toBoolean(enabled));
        }
    }

    private void loadCaptionAlignment(FieldGroup component, Element element) {
        String captionAlignment = element.attributeValue("captionAlignment");
        if (!StringUtils.isEmpty(captionAlignment)) {
            component.setCaptionAlignment(FieldGroup.FieldCaptionAlignment.valueOf(captionAlignment));
        }
    }

    private void loadCollapsable(FieldGroup component, Element element) {
        String collapsable = element.attributeValue("collapsable");
        if (!StringUtils.isEmpty(collapsable)) {
            boolean b = BooleanUtils.toBoolean(collapsable);
            component.setCollapsable(b);
            if (b) {
                String expanded = element.attributeValue("expanded");
                if (!StringUtils.isBlank(expanded)) {
                    component.setExpanded(BooleanUtils.toBoolean(expanded));
                }
            }
        }
    }
}
