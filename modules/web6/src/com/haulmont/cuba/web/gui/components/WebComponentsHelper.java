/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.ui.FieldGroupLayout;
import com.haulmont.cuba.web.toolkit.ui.HorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.event.Action;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebComponentsHelper {

    public static Resource getResource(String resURL) {
        if (StringUtils.isEmpty(resURL)) return null;

        if (resURL.startsWith("file:")) {
            return new FileResource(new File(resURL.substring("file:".length())), App.getInstance());
        } else if (resURL.startsWith("jar:")) {
            return new ClassResource(resURL.substring("jar:".length()), App.getInstance());
        } else if (resURL.startsWith("theme:")) {
            return new VersionedThemeResource(resURL.substring("theme:".length()));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static <T extends Component> Collection<T> getComponents(ComponentContainer container, Class<T> aClass) {
        List<T> res = new ArrayList<>();
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

    /**
     * Returns underlying Vaadin component implementation.
     *
     * @param component GUI component
     * @return          Vaadin component
     * @see #getComposition(com.haulmont.cuba.gui.components.Component)
     */
    public static <T extends com.vaadin.ui.Component> T unwrap(com.haulmont.cuba.gui.components.Component component) {
        Object comp = component;
        while (comp instanceof com.haulmont.cuba.gui.components.Component.Wrapper) {
            comp = ((com.haulmont.cuba.gui.components.Component.Wrapper) comp).getComponent();
        }

        return (T) comp;
    }

    /**
     * Returns underlying Vaadin component, which serves as the outermost container for the supplied GUI component.
     * For simple components like {@link com.haulmont.cuba.gui.components.Button} this method returns the same
     * result as {@link #unwrap(com.haulmont.cuba.gui.components.Component)}.
     *
     * @param component GUI component
     * @return          Vaadin component
     */
    public static <T extends com.vaadin.ui.Component> T getComposition(com.haulmont.cuba.gui.components.Component component) {
        Object comp = component;
        while (comp instanceof com.haulmont.cuba.gui.components.Component.Wrapper) {
            comp = ((com.haulmont.cuba.gui.components.Component.Wrapper) comp).getComposition();
        }

        return (T) comp;
    }

    public static void expand(AbstractOrderedLayout layout, Component component, String height, String width) {
        if (!isHorizontalLayout(layout)
                && (StringUtils.isEmpty(height) || "-1px".equals(height) || height.endsWith("%"))) {
            component.setHeight("100%");
        }

        if (!isVerticalLayout(layout)
                && (StringUtils.isEmpty(width) || "-1px".equals(width) || width.endsWith("%"))) {
            component.setWidth("100%");
        }

        layout.setExpandRatio(component, 1);
    }

    public static boolean isComponentExpanded(com.haulmont.cuba.gui.components.Component component) {
        Component vComponent = WebComponentsHelper.getComposition(component);
        if (vComponent.getParent() instanceof AbstractOrderedLayout) {
            AbstractOrderedLayout layout = (AbstractOrderedLayout) vComponent.getParent();
            return (int)layout.getExpandRatio(vComponent) == 1;
        }

        return false;
    }

    public static boolean isVerticalLayout(AbstractOrderedLayout layout) {
        return (layout instanceof VerticalLayout)
                || (layout instanceof VerticalActionsLayout);
    }

    public static boolean isHorizontalLayout(AbstractOrderedLayout layout) {
        return (layout instanceof HorizontalLayout)
                || (layout instanceof HorizontalActionsLayout);
    }

    public static Alignment convertAlignment(com.haulmont.cuba.gui.components.Component.Alignment alignment) {
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
            case TRAY:
            case TRAY_HTML:
                return com.vaadin.ui.Window.Notification.TYPE_TRAY_NOTIFICATION;
            case HUMANIZED:
            case HUMANIZED_HTML:
                return com.vaadin.ui.Window.Notification.TYPE_HUMANIZED_MESSAGE;
            case WARNING:
            case WARNING_HTML:
                return com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE;
            case ERROR:
            case ERROR_HTML:
                return com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;
            default:
                return com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE;
        }
    }

    public static int convertFilterMode(com.haulmont.cuba.gui.components.LookupField.FilterMode filterMode) {
        switch (filterMode) {
            case NO:
                return 0;
            case STARTS_WITH:
                return 1;
            case CONTAINS:
                return 2;
            default:
                return 0;
        }
    }

    public static AggregationContainer.Type convertAggregationType(AggregationInfo.Type function) {
        switch (function) {
            case COUNT:
                return AggregationContainer.Type.COUNT;
            case AVG:
                return AggregationContainer.Type.AVG;
            case MAX:
                return AggregationContainer.Type.MAX;
            case MIN:
                return AggregationContainer.Type.MIN;
            case SUM:
                return AggregationContainer.Type.SUM;
            default:
                throw new IllegalArgumentException("Unknown function: " + function);
        }
    }

    public static Button createButton() {
        return createButton(null);
    }

    public static Button createButton(String icon) {
        WebButton webButton = new WebButton();
        webButton.setIcon(icon);
        return (Button) unwrap(webButton);
    }

    public static IFrame getControllerFrame(IFrame frame) {
        if (frame instanceof AbstractFrame) {
            return frame;
        } else if (frame instanceof WrappedWindow) {
            IFrame wrapper = ((WrappedWindow) frame).getWrapper();
            if (wrapper != null) {
                return wrapper;
            }
        } else if (frame instanceof WrappedFrame) {
            IFrame wrapper = ((WrappedFrame) frame).getWrapper();
            if (wrapper != null) {
                return wrapper;
            }
        }
        return getControllerFrame(frame.getFrame());
    }

    public static void setLabelText(com.vaadin.ui.Label label, Object value, Formatter formatter) {
        label.setValue(value == null
                ? "" : String.class.isInstance(value)
                        ? (String) value : formatter != null
                                ? formatter.format(value) : value.toString()
        );
    }

    public static com.vaadin.event.ShortcutAction createShortcutAction(com.haulmont.cuba.gui.components.Action action) {
        KeyCombination keyCombination = action.getShortcut();
        if (keyCombination != null) {
            return new com.vaadin.event.ShortcutAction(
                    action.getCaption(),
                    keyCombination.getKey().getCode(),
                    KeyCombination.Modifier.codes(keyCombination.getModifiers())
            );
        } else {
            return null;
        }
    }

    /**
     * Add actions to vaadin action container.
     *
     * @param container any {@link Action.Container}
     * @param actions map of actions
     */
    public static void setActions(final Action.Container container,
                                  final Map<Action, Runnable> actions) {
        container.addActionHandler(new Action.Handler() {

            @Override
            public Action[] getActions(Object target, Object sender) {
                Set<Action> shortcuts = actions.keySet();
                return shortcuts.toArray(new Action[shortcuts.size()]);
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                Runnable runnable = actions.get(action);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    public static int convertFieldGroupCaptionAlignment(FieldGroup.FieldCaptionAlignment captionAlignment) {
        switch (captionAlignment) {
            case TOP:
                return FieldGroupLayout.CAPTION_ALIGN_TOP;
            case LEFT:
            default:
                return FieldGroupLayout.CAPTION_ALIGN_LEFT;
        }
    }

    public static int convertDateFieldResolution(com.haulmont.cuba.gui.components.DateField.Resolution resolution) {
        switch (resolution) {
            case MSEC: {
                return com.vaadin.ui.DateField.RESOLUTION_MSEC;
            }
            case SEC: {
                return com.vaadin.ui.DateField.RESOLUTION_SEC;
            }
            case HOUR: {
                return com.vaadin.ui.DateField.RESOLUTION_HOUR;
            }
            case DAY: {
                return com.vaadin.ui.DateField.RESOLUTION_DAY;
            }
            case MONTH: {
                return com.vaadin.ui.DateField.RESOLUTION_MONTH;
            }
            case YEAR: {
                return com.vaadin.ui.DateField.RESOLUTION_YEAR;
            }
            case MIN:
            default: {
                return com.vaadin.ui.DateField.RESOLUTION_MIN;
            }
        }
    }

    public static void setClickShortcut(Button button, String shortcut) {
        KeyCombination closeCombination = KeyCombination.create(shortcut);
        int[] closeModifiers = KeyCombination.Modifier.codes(closeCombination.getModifiers());
        int closeCode = closeCombination.getKey().getCode();

        button.setClickShortcut(closeCode, closeModifiers);
    }
}