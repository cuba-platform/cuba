/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import groovy.lang.Binding;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AccessControlLoader extends ContainerLoader {

    protected static final Set<String> enabledActions = new HashSet<String>() {{
        add("excel");
        add("windowclose");
        add("cancel");
    }};

    /**
     * If actionId contains <key> then action.caption will be replaced with message <value>
     * key - string, containing in action Id
     * value - message name.
     */
    protected static final HashMap<String, String> renamingActions = new HashMap<String, String>(){{
       put("edit","actions.View");
    }};

    public AccessControlLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        AccessControl accessControl = factory.createComponent(element.getName());

        final AbstractAccessData data;
        String paramName = element.attributeValue("param");
        if (paramName != null) {
            AbstractAccessData d = (AbstractAccessData) context.getParams().get(paramName);
            if (d == null) {
                String dataClassName = element.attributeValue("data");
                if (dataClassName == null)
                    throw new GuiDevelopmentException("Can't instantiate AccessData: no 'data' attribute", context.getFullFrameId());
                Class dataClass = scripting.loadClass(dataClassName);
                if (dataClass == null)
                    throw new GuiDevelopmentException("Class is not found: " + dataClassName, context.getFullFrameId());
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
            components = loadSubComponents((Component.Container)parent, element, "editable", "visible");
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
            components = Collections.emptyList();
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

    protected boolean loadConditions(Element element, Object accessData, String access) {
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
                    scripting.<Boolean>evaluateGroovy(script, context.getBinding()));
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
                        scripting.<Boolean>runGroovyScript(scriptName, binding));
            }
        }
        return true;
    }

    protected static class AccessControlLoaderPostInitTask implements PostInitTask {

        private final Component component;

        public AccessControlLoaderPostInitTask(Component component) {
            this.component = component;
        }

        @Override
        public void execute(Context context, IFrame window) {

            component.setEnabled(false);
            if (component instanceof Component.ActionOwner) {
                Action action = ((Component.ActionOwner) component).getAction();
                if (action != null) {
                    if (renamingActions.containsKey(action.getId().toLowerCase())) {
                        action.setEnabled(true);
                        Messages messages = AppBeans.get(Messages.class);
                        ((Button) component).setCaption(messages.getMainMessage(renamingActions.get(action.getId().toLowerCase())));
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