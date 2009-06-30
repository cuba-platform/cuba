/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 9:54:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;
import com.itmill.toolkit.terminal.ClassResource;
import com.itmill.toolkit.terminal.FileResource;
import com.itmill.toolkit.terminal.Resource;
import com.itmill.toolkit.terminal.ThemeResource;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.Window;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;
import java.util.List;

public class ComponentsHelper {
    public static Resource getResource(String resURL) {
        if (StringUtils.isEmpty(resURL)) return null;

        if (resURL.startsWith("file:")) {
            return new FileResource(new File(resURL.substring("file:".length())), App.getInstance());
        } else if (resURL.startsWith("jar:")) {
            return new ClassResource(resURL.substring("jar:".length()), App.getInstance());
        } else if (resURL.startsWith("theme:")) {
            return new ThemeResource(resURL.substring("theme:".length()));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static class ComponentPath {
        String[] elements;
        com.haulmont.cuba.gui.components.Component[] components;

        public ComponentPath(String[] elements, com.haulmont.cuba.gui.components.Component[] components) {
            this.elements = elements;
            this.components = components;
        }

        public String[] getElements() {
            return elements;
        }

        public com.haulmont.cuba.gui.components.Component[] getComponents() {
            return components;
        }
    }

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

    public static Collection<com.haulmont.cuba.gui.components.Component> getComponents(
            com.haulmont.cuba.gui.components.Component.Container container)
    {
        final Collection<com.haulmont.cuba.gui.components.Component> ownComponents = container.getOwnComponents();
        Set<com.haulmont.cuba.gui.components.Component> res = new HashSet<com.haulmont.cuba.gui.components.Component>(ownComponents);

        for (com.haulmont.cuba.gui.components.Component component : ownComponents) {
            if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                res.addAll(getComponents((com.haulmont.cuba.gui.components.Component.Container) component));
            }
        }

        return res;
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
                if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                    return ((com.haulmont.cuba.gui.components.Component.Container) component).<T>getComponent(
                            ValuePathHelper.format(subpath.toArray(new String[]{})));
                } else {
                    return null;
                }
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

    public static void walkComponents(com.haulmont.cuba.gui.components.Component.Container container,
                                      ComponentVisitor visitor)
    {
        __walkComponents(container, visitor, "");
    }

    private static void __walkComponents(com.haulmont.cuba.gui.components.Component.Container container,
                                      ComponentVisitor visitor,
                                      String path)
    {
        for (com.haulmont.cuba.gui.components.Component component : container.getOwnComponents()) {
            visitor.visit(component, path + component.getId());

            if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                String p = component instanceof IFrame ? 
                        path + component.getId() + "." :
                        path;
                __walkComponents(((com.haulmont.cuba.gui.components.Component.Container) component), visitor, p);
            }
        }
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

    public static int convertFilterMode(com.haulmont.cuba.gui.components.LookupField.FilterMode filterMode) {
        switch (filterMode) {
            case NO: return 0;
            case STARTS_WITH: return 1;
            case CONTAINS: return 2;
            default: return 0;
        }
    }
}
