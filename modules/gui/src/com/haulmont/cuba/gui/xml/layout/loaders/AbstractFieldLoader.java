/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:20:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.List;

public class AbstractFieldLoader extends AbstractDatasourceComponentLoader {
    protected LayoutLoaderConfig config;
    protected ComponentsFactory factory;

    public AbstractFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final Field component = factory.createComponent(element.getName());

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadDatasource(component, element);

        loadVisible(component, element);
        loadEditable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);

        loadValidators(component, element);
        loadRequired(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadExpandable(component, element);

        addAssignWindowTask(component);

        return component;
    }

    protected void loadRequired(Field component, Element element) {
        final String required = element.attributeValue("required");
        if (!StringUtils.isEmpty(required)) {
            component.setRequired(BooleanUtils.toBoolean(required));
            String msg = element.attributeValue("requiredMessage");
            if (msg != null)
                component.setRequiredMessage(loadResourceString(msg));
        }
    }

    protected void loadValidators(Field component, Element element) {
        @SuppressWarnings({"unchecked"})
        final List<Element> validatorElements = element.elements("validator");

        for (Element validatorElement : validatorElements) {
            final String className = validatorElement.attributeValue("class");
            final Class<Field.Validator> aClass = ReflectionHelper.getClass(className);

            try {
                final Constructor<Field.Validator> constructor = aClass.getConstructor(Element.class);
                try {
                    final Field.Validator validator = constructor.newInstance(validatorElement);
                    component.addValidator(validator);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    final Field.Validator validator = aClass.newInstance();
                    component.addValidator(validator);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

}