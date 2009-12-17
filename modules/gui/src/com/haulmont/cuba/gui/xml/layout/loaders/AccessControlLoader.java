/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2009 17:45:39
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AccessControl;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import groovy.lang.Binding;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class AccessControlLoader extends ContainerLoader {

    public AccessControlLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        AccessControl accessControl = factory.createComponent(element.getName());

        Object accessData = null;
        String paramName = element.attributeValue("param");
        if (paramName != null) {
            accessData = context.getParams().get(paramName);
            if (accessData == null) {
                String dataClassName = element.attributeValue("data");
                if (dataClassName == null)
                    throw new IllegalStateException("Can not instantiate AccessData: no 'data' attribute");
                Class dataClass = ScriptingProvider.loadClass(dataClassName);
                if (dataClass == null)
                    throw new IllegalStateException("Class not found: " + dataClassName);
                try {
                    accessData = ReflectionHelper.newInstance(dataClass, context.getParams());
                    context.getParams().put(paramName, accessData);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

        }


        final boolean visible = loadConditions(element, accessData, "visible");
        final boolean editable = loadConditions(element, accessData, "editable");

        Collection<Component> components;
        if (visible) {
            components = loadSubComponents(parent, element, "editable", "visible");
            for (final Component component : components) {
                if (component instanceof Component.Editable && !editable) {
                    ((Component.Editable) component).setEditable(false);
                }
                if (component instanceof Button && !editable) {
                    context.addLazyTask(new AccessControlLoaderLazyTask(component));
                }

                if (component instanceof Component.Container) {
                    ComponentsHelper.walkComponents(((Component.Container) component),
                            new ComponentVisitor() {
                                public void visit(Component component, String name) {
                                    if (component instanceof Component.Editable && !editable) {
                                        ((Component.Editable) component).setEditable(false);
                                    }
                                    if (component instanceof Button && !editable) {
                                        context.addLazyTask(new AccessControlLoaderLazyTask(component));
                                    }
                                }
                            });
                }
            }
        } else {
            components = Collections.EMPTY_LIST;
        }
        accessControl.setRealComponents(components);

        return accessControl;
    }

    private boolean loadConditions(Element element, Object accessData, String access) {
        Element accessElement = element.element(access);

        if (accessElement == null)
            return true;

        if (accessData != null) {
            String property = accessElement.attributeValue("property");
            if (!StringUtils.isBlank(property)) {
                Boolean value;
                try {
                    value = ReflectionHelper.invokeMethod(accessData, "get" + StringUtils.capitalize(property));
                } catch (NoSuchMethodException e) {
                    try {
                        value = ReflectionHelper.invokeMethod(accessData, "is" + StringUtils.capitalize(property));
                    } catch (NoSuchMethodException e1) {
                        throw new RuntimeException(e1);
                    }
                }
                return BooleanUtils.isTrue(value);
            }
        }

        String script = accessElement.getText();
        if (!StringUtils.isBlank(script)) {
            return BooleanUtils.isTrue(
                    ScriptingProvider.<Boolean>evaluateGroovy(ScriptingProvider.Layer.GUI, script, context.getBinding()));
        } else {
            String scriptName = accessElement.attributeValue("script");
            if (!StringUtils.isBlank(scriptName)) {
                Binding binding = new Binding();
                for (Map.Entry<String, Object> entry : context.getParams().entrySet()) {
                    String name = entry.getKey().replace('$', '_');
                    binding.setVariable(name, entry.getValue());
                }
                for (Element paramElem : Dom4j.elements(accessElement, "param")) {
                    String paramName = paramElem.attributeValue("name");
                    String paramValue = paramElem.getText();
                    binding.setVariable(paramName, paramValue);
                }
                return BooleanUtils.isTrue(
                        ScriptingProvider.<Boolean>runGroovyScript(scriptName, binding));
            }
        }
        return true;
    }

    private static class AccessControlLoaderLazyTask implements LazyTask {

        private final Component component;

        public AccessControlLoaderLazyTask(Component component) {
            this.component = component;
        }

        public void execute(Context context, IFrame frame) {
            component.setEnabled(false);
            if (component instanceof Component.ActionOwner)
                ((Component.ActionOwner) component).getAction().setEnabled(false);
        }
    }
}
