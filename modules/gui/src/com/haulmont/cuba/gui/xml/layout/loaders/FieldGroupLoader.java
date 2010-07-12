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

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
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

        loadVisible(component, element);
        loadEditable(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadSwitchable(component, element);
        loadExpandable(component, element);

        loadCaptionAlignment(component, element);

        String expanded = element.attributeValue("expanded");
        if (!StringUtils.isBlank(expanded)) {
            component.setExpanded(BooleanUtils.toBoolean(expanded));
        }

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

        context.addLazyTask(new LazyTask() {
            public void execute(Context context, IFrame frame) {
                final List<FieldGroup.Field> fields = component.getFields();
                for (final FieldGroup.Field field : fields) {
                    loadValidators(component, field);
                    loadRequired(component, field);
                    loadEditable(component, field);
                }
            }
        });

        assignFrame(component);

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
        final List<Element> fieldElements = element.elements("property");
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

        final MetaClass metaClass = ds.getMetaClass();
        final MetaPropertyPath metaPropertyPath = metaClass.getPropertyEx(id);
        if (metaPropertyPath == null)
            throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'", id, metaClass.getName()));

        final FieldGroup.Field field = new FieldGroup.Field(metaPropertyPath);

        if (datasource != null) {
            field.setDatasource(datasource);
        }

        loadCaption(field, element);
        loadDescription(field, element);

        field.setXmlDescriptor(element);
        field.setType(metaPropertyPath.getRangeJavaClass());

        field.setFormatter(loadFormatter(element));

        String width = element.attributeValue("width");
        if (!StringUtils.isEmpty(width)) {
            field.setWidth(width);
        }

        String custom = element.attributeValue("custom");
        if (!StringUtils.isEmpty(custom)) {
            field.setCustom(BooleanUtils.toBoolean(custom));
        }

        return field;
    }

    protected void loadValidators(FieldGroup component, FieldGroup.Field field) {
        final List<Element> validatorElements = field.getXmlDescriptor().elements("validator");

        for (Element validatorElement : validatorElements) {
            final String className = validatorElement.attributeValue("class");
            final Class<Field.Validator> aClass = ReflectionHelper.getClass(className);

            try {
                final Constructor<Field.Validator> constructor = aClass.getConstructor(Element.class);
                try {
                    final Field.Validator validator = constructor.newInstance(validatorElement);
                    component.addValidator(field, validator);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    final Field.Validator validator = aClass.newInstance();
                    component.addValidator(field, validator);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    protected void loadRequired(FieldGroup component, FieldGroup.Field field) {
        Element element = field.getXmlDescriptor();
        final String required = element.attributeValue("required");
        if (!StringUtils.isEmpty(required)) {
            String requiredMsg = element.attributeValue("requiredMessage");
            component.setRequired(field, BooleanUtils.toBoolean(required), loadResourceString(requiredMsg));
        }
    }

    protected void loadEditable(FieldGroup component, FieldGroup.Field field) {
        Element element = field.getXmlDescriptor();
        final String editable = element.attributeValue("editable");
        if (!StringUtils.isEmpty(editable)) {
            component.setEditable(field, BooleanUtils.toBoolean(editable));
        }
    }

    private void loadCaptionAlignment(FieldGroup component, Element element) {
        String captionAlignment = element.attributeValue("captionAlignment");
        if (!StringUtils.isEmpty(captionAlignment)) {
            component.setCaptionAlignment(FieldGroup.FieldCaptionAlignment.valueOf(captionAlignment));
        }
    }

    private void loadSwitchable(FieldGroup component, Element element) {
        String switchable = element.attributeValue("switchable");
        if (!StringUtils.isEmpty(switchable)) {
            component.setSwitchable(BooleanUtils.toBoolean(switchable));
        }
    }
}
