/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ShortcutAction;
import com.haulmont.cuba.gui.components.ValuePathHelper;
import org.apache.commons.lang.ArrayUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Collections;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopComponentsHelper {

    public static final int BUTTON_HEIGHT = 30;
    public static final int FIELD_HEIGHT = 28;

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

    @Nullable
    public static <T extends Component> T getComponent(Component.Container comp, String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            final Component component = comp.getOwnComponent(id);

            if (component == null) {
                Component c = getComponentByIteration(comp, id);
                if (c != null)
                    return (T) c;
            } else {
                return (T) component;
            }
        } else {
            Component component = comp.getOwnComponent(elements[0]);
            if (component == null) {
                Component c = getComponentByIteration(comp, id);
                if (c != null)
                    return (T) c;
            } else {
                String[] subpath = (String[]) ArrayUtils.subarray(elements, 1, elements.length);
                if (component instanceof Component.Container) {
                    return ((Component.Container) component).<T>getComponent(ValuePathHelper.format(subpath));
                }
            }
        }
        return null;
    }

    @Nullable
    private static <T extends Component> T getComponentByIteration(Component.Container container, String id) {
        for (Component component : container.getOwnComponents()) {
            if (id.equals(component.getId()))
                return (T) component;
            else {
                if (component instanceof Component.Container) {
                    return (T) getComponentByIteration((Component.Container) component, id);
                }
            }
        }
        return null;
    }

    public static int convertMessageType(IFrame.MessageType messageType) {
        if (messageType.equals(IFrame.MessageType.CONFIRMATION))
            return JOptionPane.QUESTION_MESSAGE;
        else if (messageType.equals(IFrame.MessageType.WARNING))
            return JOptionPane.WARNING_MESSAGE;
        else
            return JOptionPane.INFORMATION_MESSAGE;
    }

    public static int convertNotificationType(IFrame.NotificationType type) {
        switch (type) {
            case WARNING:
                return JOptionPane.WARNING_MESSAGE;
            case ERROR:
                return JOptionPane.ERROR_MESSAGE;
            case HUMANIZED:
                return JOptionPane.INFORMATION_MESSAGE;
            case TRAY:
                return JOptionPane.WARNING_MESSAGE;
        }
        return JOptionPane.PLAIN_MESSAGE;
    }

    public static void adjustSize(JButton button) {
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_HEIGHT));
    }

    public static void adjustSize(JComboBox comboBox) {
        comboBox.setPreferredSize(new Dimension(0, FIELD_HEIGHT));
    }

    public static void adjustDateFieldSize(JPanel dateFieldComposition) {
        dateFieldComposition.setPreferredSize(new Dimension(0, FIELD_HEIGHT));
    }

    /**
     * Convert {@link ShortcutAction.KeyCombination} to {@link KeyStroke}.
     *
     * @param combination Key combination to convert
     * @return KeyStroke
     */
    public static KeyStroke convertKeyCombination(ShortcutAction.KeyCombination combination) {
        ShortcutAction.Modifier[] modifiers = combination.getModifiers();
        int modifiersMask = 0;
        if (modifiers != null && modifiers.length > 0) {
            for (ShortcutAction.Modifier modifier : modifiers) {
                switch (modifier) {
                    case CTRL:
                        modifiersMask = modifiersMask | InputEvent.CTRL_DOWN_MASK;
                        break;
                    case ALT:
                        modifiersMask = modifiersMask | InputEvent.ALT_DOWN_MASK;
                        break;
                    case SHIFT:
                        modifiersMask = modifiersMask | InputEvent.SHIFT_DOWN_MASK;
                        break;
                }
            }
        }
        return KeyStroke.getKeyStroke(combination.getKey().getVirtualKey(), modifiersMask, false);
    }

    /**
     * Make JTable handle TAB key as all other components - move focus to next/previous components.
     * <p>Default Swing behaviour for table is to move focus to next/previous cell inside the table.</p>
     * @param table table instance
     */
    public static void correctTableFocusTraversal(JTable table) {
        table.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                Collections.singleton(AWTKeyStroke.getAWTKeyStroke("TAB")));
        table.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                Collections.singleton(AWTKeyStroke.getAWTKeyStroke("shift TAB")));
    }

    /**
     * Add shortcut action to any JComponent.
     *
     * @param name name of action that used as action key in {@link InputMap} and {@link ActionMap}.
     * @param component
     * @param key
     * @param action
     */
    public static void addShortcutAction(String name, JComponent component, KeyStroke key, Action action) {
        ActionMap actionMap = component.getActionMap();
        InputMap inputMap = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(key, name);
        actionMap.put(name, action);
    }
}
