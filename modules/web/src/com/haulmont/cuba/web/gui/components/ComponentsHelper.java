/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:54:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.IFrame;

import java.util.*;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ComponentsHelper {
    public static <T extends Component> Collection<T> getComponents(ComponentContainer container, Class<T> aClass) {
        List<T> res  = new ArrayList<T>();
        final Iterator iterator = container.getComponentIterator();
        while (iterator.hasNext()) {
            Component component = (Component) iterator.next();
            if (aClass.isAssignableFrom(component.getClass())) {
                res.add((T) component);
            } else if (ComponentContainer.class.isAssignableFrom(component.getClass())) {
                res.addAll(getComponents((ComponentContainer) component, aClass));
            }

        }

        return res;
    }

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

    public static Alignment convertAlignment(com.haulmont.cuba.gui.components.Component.Alignment  alignment) {
        if (alignment == null) return null;

        switch (alignment) {
            case TOP_LEFT: {return Alignment.TOP_LEFT;}
            case TOP_CENTER: {return Alignment.TOP_CENTER;}
            case TOP_RIGHT: {return Alignment.TOP_RIGHT;}
            case MIDDLE_LEFT: {return Alignment.MIDDLE_LEFT;}
            case MIDDLE_CENTER: {return Alignment.MIDDLE_CENTER;}
            case MIDDLE_RIGHT: {return Alignment.MIDDLE_RIGHT;}
            case BOTTOM_LEFT: {return Alignment.BOTTOM_LEFT;}
            case BOTTOM_CENTER: {return Alignment.BOTTOM_CENTER;}
            case BOTTOM_RIGHT: {return Alignment.BOTTOM_RIGHT;}
            default: {throw new UnsupportedOperationException();}
        }
    }

    public static int convertNotificationType(IFrame.NotificationType type) {
        switch (type) {
            case TRAY: return com.itmill.toolkit.ui.Window.Notification.TYPE_TRAY_NOTIFICATION;
            case HUMANIZED: return com.itmill.toolkit.ui.Window.Notification.TYPE_HUMANIZED_MESSAGE;
            case WARNING: return com.itmill.toolkit.ui.Window.Notification.TYPE_WARNING_MESSAGE;
            case ERROR: return com.itmill.toolkit.ui.Window.Notification.TYPE_ERROR_MESSAGE;
            default: return com.itmill.toolkit.ui.Window.Notification.TYPE_WARNING_MESSAGE;
        }
    }
}
