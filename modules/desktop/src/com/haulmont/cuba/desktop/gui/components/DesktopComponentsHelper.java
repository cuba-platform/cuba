/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ValuePathHelper;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopComponentsHelper {

    public static final int BUTTON_HEIGHT = 30;
    public static final int FIELD_HEIGHT = 21;

    public static JComponent unwrap(Component component) {
        Object comp = component;
        while (comp instanceof Component.Wrapper) {
            comp = ((Component.Wrapper) comp).getComponent();
        }
        return (JComponent) comp;
    }

    public static JComponent getComposition(Component component) {
        Object comp = component;
        while (comp instanceof Component.Wrapper) {
            comp = ((Component.Wrapper) comp).getComposition();
        }
        return (JComponent) comp;
    }

    public static <T extends Component> T getComponent(Component.Container comp, String id) {
        final JComponent container = unwrap(comp);

        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            final Component component = comp.getOwnComponent(id);

            if (component == null) {
                return (T) getComponentByIteration(container, id);
            } else {
                return (T) component;
            }
        } else {
            Component component = comp.getOwnComponent(elements[0]);
            if (component == null) {
                return (T) getComponentByIteration(container, id);
            } else {
                final List<String> subpath = Arrays.asList(elements).subList(1, elements.length);
                if (component instanceof Component.Container) {
                    return ((Component.Container) component).<T>getComponent(
                            ValuePathHelper.format(subpath.toArray(new String[subpath.size()])));
                } else {
                    return null;
                }
            }
        }
    }

    private static <T extends Component> T getComponentByIteration(JComponent container, String id) {
        throw new UnsupportedOperationException();
    }

    public static Collection<Component> getComponents(Component container) {
        Collection<Component> ownComponents;
        if (container instanceof Component.Container) {
            ownComponents = ((Component.Container) container).getOwnComponents();
        } else if (container instanceof DesktopFieldGroup) {
            ownComponents = ((DesktopFieldGroup) container).getComponents();
        } else
            ownComponents = Collections.emptySet();
        Set<Component> res = new HashSet<Component>(ownComponents);

        for (Component component : ownComponents) {
            res.addAll(getComponents(component));
        }

        return res;
    }
    public static int convertMessageType(IFrame.MessageType messageType) {
        if (messageType.equals(IFrame.MessageType.CONFIRMATION))
            return JOptionPane.QUESTION_MESSAGE;
        else if (messageType.equals(IFrame.MessageType.WARNING))
            return JOptionPane.WARNING_MESSAGE;
        else
            return JOptionPane.INFORMATION_MESSAGE;
    }

    public static void adjustSize(JButton button) {
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_HEIGHT));
    }

    public static void adjustSize(JComboBox comboBox) {
        comboBox.setPreferredSize(new Dimension(0, FIELD_HEIGHT));
    }
}
