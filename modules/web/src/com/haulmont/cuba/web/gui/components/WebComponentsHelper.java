/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;

public class WebComponentsHelper {

    protected static Map<String, Class<? extends FontIcon>> fontIcons = new ConcurrentHashMap<>();

    static {
        registerFontIcon("font-icon", FontAwesome.class);
        registerFontIcon("font-awesome-icon", FontAwesome.class);
    }

    public static Resource getResource(String resURL) {
        if (StringUtils.isEmpty(resURL)) return null;

        if (resURL.startsWith("file:")) {
            return new FileResource(new File(resURL.substring("file:".length())));
        } else if (resURL.startsWith("jar:")) {
            return new ClassResource(resURL.substring("jar:".length()));
        } else if (resURL.startsWith("theme:")) {
            String resourceId = resURL.substring("theme:".length());

            Configuration configuration = AppBeans.get(Configuration.NAME);
            WebConfig webConfig = configuration.getConfig(WebConfig.class);

            if (webConfig.getUseFontIcons()) {
                String fontIcon;

                ThemeConstants themeConstants = App.getInstance().getThemeConstants();
                String iconKey = "cuba.web." + StringUtils.replace(resourceId, "/", ".");
                fontIcon = themeConstants.get(iconKey);

                try {
                    Resource resource = getFontIconResource(fontIcon);
                    if (resource != null) {
                        return resource;
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    LoggerFactory.getLogger(WebComponentsHelper.class).warn("Unable to use font icon " + fontIcon);
                }
            }

            return new VersionedThemeResource(resourceId);
        } else if (resURL.contains("icon:")) {
            try {
                return getFontIconResource(resURL);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LoggerFactory.getLogger(WebComponentsHelper.class).warn("Unable to use font icon " + resURL);
            }
            return null;
        } else {
            return new VersionedThemeResource(resURL);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Component> Collection<T> getComponents(HasComponents container, Class<T> aClass) {
        List<T> res = new ArrayList<>();
        for (Object aContainer : container) {
            Component component = (Component) aContainer;
            if (aClass.isAssignableFrom(component.getClass())) {
                res.add((T) component);
            } else if (HasComponents.class.isAssignableFrom(component.getClass())) {
                res.addAll(getComponents((HasComponents) component, aClass));
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
    public static Component unwrap(com.haulmont.cuba.gui.components.Component component) {
        Object comp = component;
        while (comp instanceof com.haulmont.cuba.gui.components.Component.Wrapper) {
            comp = ((com.haulmont.cuba.gui.components.Component.Wrapper) comp).getComponent();
        }

        return (Component) comp;
    }

    /**
     * Returns underlying Vaadin component, which serves as the outermost container for the supplied GUI component.
     * For simple components like {@link com.haulmont.cuba.gui.components.Button} this method returns the same
     * result as {@link #unwrap(com.haulmont.cuba.gui.components.Component)}.
     *
     * @param component GUI component
     * @return          Vaadin component
     * @see #unwrap(com.haulmont.cuba.gui.components.Component)
     */
    public static Component getComposition(com.haulmont.cuba.gui.components.Component component) {
        Object comp = component;
        while (comp instanceof com.haulmont.cuba.gui.components.Component.Wrapper) {
            comp = ((com.haulmont.cuba.gui.components.Component.Wrapper) comp).getComposition();
        }

        return (Component) comp;
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
                || (layout instanceof CubaVerticalActionsLayout);
    }

    public static boolean isHorizontalLayout(AbstractOrderedLayout layout) {
        return (layout instanceof HorizontalLayout)
                || (layout instanceof CubaHorizontalActionsLayout);
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

    public static Notification.Type convertNotificationType(Frame.NotificationType type) {
        switch (type) {
            case TRAY:
            case TRAY_HTML:
                return Notification.Type.TRAY_NOTIFICATION;
            case HUMANIZED:
            case HUMANIZED_HTML:
                return Notification.Type.HUMANIZED_MESSAGE;
            case WARNING:
            case WARNING_HTML:
                return Notification.Type.WARNING_MESSAGE;
            case ERROR:
            case ERROR_HTML:
                return Notification.Type.ERROR_MESSAGE;
            default:
                return Notification.Type.WARNING_MESSAGE;
        }
    }

    public static FilteringMode convertFilterMode(com.haulmont.cuba.gui.components.LookupField.FilterMode filterMode) {
        switch (filterMode) {
            case NO:
                return FilteringMode.OFF;
            case STARTS_WITH:
                return FilteringMode.STARTSWITH;
            case CONTAINS:
                return FilteringMode.CONTAINS;
            default:
                return FilteringMode.OFF;
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
            case CUSTOM:
                return AggregationContainer.Type.CUSTOM;
            default:
                throw new IllegalArgumentException("Unknown function: " + function);
        }
    }

    public static Table.Align convertColumnAlignment(com.haulmont.cuba.gui.components.Table.ColumnAlignment alignment) {
        if (alignment == null) {
            return null;
        }

        switch (alignment) {
            case LEFT:
                return Table.Align.LEFT;
            case CENTER:
                return Table.Align.CENTER;
            case RIGHT:
                return Table.Align.RIGHT;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static Button createButton() {
        return createButton(null);
    }

    public static Button createButton(String icon) {
        ComponentsFactory cf = AppBeans.get(ComponentsFactory.NAME);
        com.haulmont.cuba.gui.components.Button button =
                cf.createComponent(com.haulmont.cuba.gui.components.Button.class);
        button.setIcon(icon);
        return (Button) unwrap(button);
    }

    public static Frame getControllerFrame(Frame frame) {
        if (frame instanceof AbstractFrame) {
            return frame;
        } else if (frame instanceof WrappedWindow) {
            Frame wrapper = ((WrappedWindow) frame).getWrapper();
            if (wrapper != null) {
                return wrapper;
            }
        } else if (frame instanceof WrappedFrame) {
            Frame wrapper = ((WrappedFrame) frame).getWrapper();
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
        KeyCombination keyCombination = action.getShortcutCombination();
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
     * @param actions   map of actions
     */
    public static void setActions(Action.Container container,
                                  Map<Action, Runnable> actions) {
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

    /**
     * Checks if the component should be visible to the client. Returns false if
     * the child should not be sent to the client, true otherwise.
     *
     * @param child The child to check
     * @return true if the child is visible to the client, false otherwise
     */
    public static boolean isComponentVisibleToClient(Component child) {
        if (!child.isVisible()) {
            return false;
        }
        HasComponents parent = child.getParent();

        if (parent instanceof SelectiveRenderer) {
            if (!((SelectiveRenderer) parent).isRendered(child)) {
                return false;
            }
        }

        if (parent != null) {
            return isComponentVisibleToClient(parent);
        } else {
            if (child instanceof UI) {
                // UI has no parent and visibility was checked above
                return true;
            } else {
                // Component which is not attached to any UI
                return false;
            }
        }
    }

    /**
     * Tests if component visible and its container visible.
     *
     * @param child component
     * @return component visibility
     */
    public static boolean isComponentVisible(Component child) {
        if (child.getParent() instanceof TabSheet) {
            TabSheet tabSheet = (TabSheet) child.getParent();
            TabSheet.Tab tab = tabSheet.getTab(child);
            if (!tab.isVisible()) {
                return false;
            }
        }

        if (child.getParent() instanceof CubaGroupBox) {
            // ignore groupbox content container visibility
            return isComponentVisible(child.getParent());
        }

        return child.isVisible() && (child.getParent() == null || isComponentVisible(child.getParent()));
    }

    /**
     * Tests if component enabled and visible and its container enabled.
     *
     * @param child component
     * @return component enabled state
     */
    public static boolean isComponentEnabled(Component child) {
        if (child.getParent() instanceof TabSheet) {
            TabSheet tabSheet = (TabSheet) child.getParent();
            TabSheet.Tab tab = tabSheet.getTab(child);
            if (!tab.isEnabled()) {
                return false;
            }
        }

        return child.isEnabled() && (child.getParent() == null || isComponentEnabled(child.getParent())) &&
                isComponentVisible(child);
    }

    public static boolean convertFieldGroupCaptionAlignment(FieldGroup.FieldCaptionAlignment captionAlignment) {
        if (captionAlignment == FieldGroup.FieldCaptionAlignment.LEFT)
            return true;
        else
            return false;
    }

    public static Resolution convertDateFieldResolution(com.haulmont.cuba.gui.components.DateField.Resolution resolution) {
        switch (resolution) {
            case SEC:
                return Resolution.SECOND;

            case HOUR:
                return Resolution.HOUR;

            case DAY:
                return Resolution.DAY;

            case MONTH:
                return Resolution.MONTH;

            case YEAR:
                return Resolution.YEAR;

            case MIN:
            default:
                return Resolution.MINUTE;
        }
    }

    public static void setClickShortcut(Button button, String shortcut) {
        KeyCombination closeCombination = KeyCombination.create(shortcut);
        int[] closeModifiers = KeyCombination.Modifier.codes(closeCombination.getModifiers());
        int closeCode = closeCombination.getKey().getCode();

        button.setClickShortcut(closeCode, closeModifiers);
    }

    @Nullable
    public static Resource getIcon(String iconName) {
        if (StringUtils.isEmpty(iconName)) {
            return null;
        }

        Configuration configuration = AppBeans.get(Configuration.NAME);
        WebConfig webConfig = configuration.getConfig(WebConfig.class);

        if (webConfig.getUseFontIcons()) {
            String fontIcon;

            if (StringUtils.contains(iconName, ":")) {
                fontIcon = iconName;
            } else {
                ThemeConstants themeConstants = App.getInstance().getThemeConstants();
                String iconKey = "cuba.web." + StringUtils.replace(iconName, "/", ".");
                fontIcon = themeConstants.get(iconKey);
            }

            try {
                Resource resource = getFontIconResource(fontIcon);
                if (resource != null) {
                    return resource;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LoggerFactory.getLogger(WebComponentsHelper.class).warn("Unable to use font icon {}", fontIcon);
            }
        }
        return new VersionedThemeResource(iconName);
    }

    @Nullable
    public static Resource getFontIconResource(String fontIcon)
            throws NoSuchFieldException, IllegalAccessException {
        if (StringUtils.isNotEmpty(fontIcon)) {
            String fontIconName = "font-awesome-icon";
            String fontIconField;
            if (fontIcon.contains(":")) {
                fontIconName = StringUtils.substring(fontIcon, 0, fontIcon.indexOf(":"));
                fontIconField = StringUtils.substring(fontIcon, fontIcon.indexOf(":") + 1);
            } else {
                fontIconField = fontIcon;
            }
            return getFontIconResource(fontIconName, fontIconField);
        }
        return null;
    }

    @Nullable
    public static Resource getFontIconResource(String fontIconName, String fontIconField)
            throws NoSuchFieldException, IllegalAccessException {
        final Class<? extends FontIcon> fontIcon = fontIcons.get(fontIconName);
        if (fontIcon != null) {
            Field field = fontIcon.getDeclaredField(fontIconField);
            return (Resource) field.get(null);
        }
        return null;
    }

    public static void registerFontIcon(String name, Class<? extends FontIcon> font) {
        fontIcons.put(name, font);
    }

    public static void addEnterShortcut(TextField textField, final Runnable runnable) {
        CubaTextField cubaTextField = (CubaTextField) WebComponentsHelper.unwrap(textField);
        cubaTextField.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, KeyCombination.Modifier.codes()) {
            @Override
            public void handleAction(Object sender, Object target) {
                runnable.run();
            }
        });
    }
}