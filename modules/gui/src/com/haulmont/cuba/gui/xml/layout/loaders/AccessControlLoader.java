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
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import groovy.lang.Binding;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

public class AccessControlLoader extends ContainerLoader {

    private static final Set<String> enabledActions = new HashSet<String>() {{
        add("excel");
        add("windowclose");
        add("cancel");
    }};

    /**
     * If actionId contains <key> then action.caption will be replaced with message <value>
     * key - string, containing in action Id
     * value - message name.
     */
    private static  final HashMap<String, String> renamingActions = new HashMap<String, String>(){{
       put("edit","actions.View");
    }};

    public AccessControlLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        AccessControl accessControl = factory.createComponent(element.getName());

        final AbstractAccessData data;
        String paramName = element.attributeValue("param");
        if (paramName != null) {
            AbstractAccessData d = (AbstractAccessData) context.getParams().get(paramName);
            if (d == null) {
                String dataClassName = element.attributeValue("data");
                if (dataClassName == null)
                    throw new IllegalStateException("Can not instantiate AccessData: no 'data' attribute");
                Class dataClass = ScriptingProvider.loadClass(dataClassName);
                if (dataClass == null)
                    throw new IllegalStateException("Class not found: " + dataClassName);
                try {
                    data = (AbstractAccessData) ReflectionHelper.newInstance(dataClass, context.getParams());
                    context.getParams().put(paramName, data);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else {
                data = d;
            }
        } else {
            data = null;
        }

        final boolean visible = loadConditions(element, data, "visible");
        final boolean editable = loadConditions(element, data, "editable");

        Collection<Component> components;
        if (visible) {
            components = loadSubComponents(parent, element, "editable", "visible");
            for (Component component : components) {
                applyToComponent(component, editable, data, components);

                if (component instanceof Component.Container) {
                    Collection<Component> content = ((Component.Container) component).getComponents();
                    for (Component c : content) {
                        applyToComponent(c, editable, data, content);
                    }
                }
            }
        } else {
            components = Collections.EMPTY_LIST;
        }
        accessControl.setRealComponents(components);

        return accessControl;
    }

    protected void applyToComponent(Component component, boolean editable,
                                    AbstractAccessData data, Collection<Component> components) {
        if (component instanceof Component.Editable && !editable) {
            ((Component.Editable) component).setEditable(false);
        }

        if ((component instanceof Button || component instanceof PopupButton) && !editable) {
            context.addPostInitTask(new AccessControlLoaderPostInitTask(component));
        }

        if (component instanceof Component.HasButtonsPanel && !editable) {
            ButtonsPanel buttonsPanel = ((Component.HasButtonsPanel) component).getButtonsPanel();
            if (buttonsPanel != null) {
                Collection<Component> buttons = new ArrayList<Component>(buttonsPanel.getButtons());
                for (Component button : buttons) {
                    applyToComponent(button, editable, data, buttons);
                }
            }
        }

        if (data != null) {
            data.visitComponent(component, components);
        }
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
                    ScriptingProvider.<Boolean>evaluateGroovy(script, context.getBinding()));
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

    private static class AccessControlLoaderPostInitTask implements PostInitTask {

        private final Component component;

        public AccessControlLoaderPostInitTask(Component component) {
            this.component = component;
        }

        public void execute(Context context, IFrame window) {

            final String messagesPackage = AppConfig.getMessagesPack();
            component.setEnabled(false);
            if (component instanceof Component.ActionOwner) {
                Action action = ((Component.ActionOwner) component).getAction();
                if (action != null) {
                    if (renamingActions.containsKey(action.getId().toLowerCase())) {
                        action.setEnabled(true);
                        ((Button) component).setCaption(MessageProvider.getMessage(messagesPackage, renamingActions.get(action.getId().toLowerCase())));
                    } else if (enabledActions.contains(action.getId().toLowerCase())) {
                        action.setEnabled(true);
                    } else {
                        action.setEnabled(false);
                    }
                }

            }
        }
    }
}
