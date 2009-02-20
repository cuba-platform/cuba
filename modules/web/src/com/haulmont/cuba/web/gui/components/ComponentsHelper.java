/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:54:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.ComponentContainer;
import com.itmill.toolkit.ui.Form;
import com.itmill.toolkit.ui.AbstractOrderedLayout;
import com.haulmont.cuba.gui.components.ValuePathHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class ComponentsHelper {

    public static Component unwrap(com.haulmont.cuba.gui.components.Component component) {
        Object comp = component;
        while (comp instanceof com.haulmont.cuba.gui.components.Component.Wrapper) {
            comp = ((com.haulmont.cuba.gui.components.Component.Wrapper) comp).getComponent();
        }

        return  (com.itmill.toolkit.ui.Component) comp;
    }

    public static <T extends com.haulmont.cuba.gui.components.Component> T getComponent(
            com.haulmont.cuba.gui.components.Component.Container comp, String id)
    {
        final Component unwrapedComponent = unwrap(comp);
        final ComponentContainer container =
                unwrapedComponent instanceof Form ?
                        ((Form)unwrapedComponent).getLayout() :
                        (ComponentContainer) unwrapedComponent;

        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            final com.haulmont.cuba.gui.components.Component component =
                    comp.<com.haulmont.cuba.gui.components.Component>getOwnComponent(id);

            if (component == null) {
                return (T)getComponentByIterate(container, id);
            } else {
                return (T) component;
            }
        } else {
            com.haulmont.cuba.gui.components.Component component = comp.getOwnComponent(elements[0]);
            if (component == null) {
                return (T)getComponentByIterate(container, id);
            } else {
                final List<String> subpath = Arrays.asList(elements).subList(1, elements.length);
                return ((com.haulmont.cuba.gui.components.Component.Container) component).<T>getComponent(
                        ValuePathHelper.format(subpath.toArray(new String[]{})));
            }
        }
    }

    protected static <T extends com.haulmont.cuba.gui.components.Component> T getComponentByIterate(ComponentContainer container, String id) {
        com.haulmont.cuba.gui.components.Component component;
        final Iterator iterator = container.getComponentIterator();
        while (iterator.hasNext()) {
            Component c = (Component) iterator.next();

            if (c instanceof com.haulmont.cuba.gui.components.Component.Container) {
                component = ((com.haulmont.cuba.gui.components.Component.Container) c).getComponent(id);
                if (component != null) return (T) component;
            } else if (c instanceof ComponentEx) {
                component = ((ComponentEx) c).asComponent();
                if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                    component = ((com.haulmont.cuba.gui.components.Component.Container) component).getComponent(id);
                    if (component != null) return (T) component;
                }
            } else if (c instanceof ComponentContainer) {
                component = getComponentByIterate(((ComponentContainer) c), id);
                if (component != null) return (T) component;
            } else if (c instanceof Form) {
                component = getComponentByIterate(((Form) c).getLayout(), id);
                if (component != null) return (T) component;
            }
        }

        return null;
    }

    public static void expand(AbstractOrderedLayout layout, Component component, String height, String width) {
        if (StringUtils.isEmpty(height) && StringUtils.isEmpty(width)) {
            component.setSizeFull();
        } else {
            if (!StringUtils.isEmpty(height)) {
                component.setHeight(height);
            }

            if (!StringUtils.isEmpty(width)) {
                component.setWidth(width);
            }
        }
        layout.setExpandRatio(component, 1);
    }
}
