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
package com.haulmont.cuba.gui;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetadataObject;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.getFilteredAttributes;

/**
 * Utility class working with GenericUI components.
 */
public abstract class ComponentsHelper {
    public static final String[] UNIT_SYMBOLS = { "px", "pt", "pc", "em", "ex", "mm", "cm", "in", "%" };

    /**
     * Returns the collection of components within the specified container and all of its children.
     *
     * @param container container to start from
     * @return          collection of components
     */
    public static Collection<Component> getComponents(Component.Container container) {
        Collection<Component> res = new LinkedHashSet<>();

        fillChildComponents(container, res);

        // do not return LinkedHashSet, it uses much more memory than ArrayList
        return new ArrayList<>(res);
    }

    @Nullable
    public static Component getWindowComponent(
            Window window, String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            Component component = window.getRegisteredComponent(id);
            if (component != null)
                return component;
            else
                return window.getTimer(id);
        } else {
            Component innerComponent = window.getRegisteredComponent(elements[0]);
            if (innerComponent instanceof Component.Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((Component.Container) innerComponent).getComponent(subPath);
            } else if (innerComponent instanceof Component.HasNamedComponents) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((Component.HasNamedComponents) innerComponent).getComponent(subPath);
            } else if (innerComponent instanceof FieldGroup) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));

                return ((FieldGroup) innerComponent).getFieldComponent(subPath);
            }
            return null;
        }
    }

    @Nullable
    public static Component getFrameComponent(Frame frame, String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            Component component = frame.getRegisteredComponent(id);
            if (component == null && frame.getFrame() != null && frame.getFrame() != frame) {
                component = frame.getFrame().getComponent(id);
            }
            return component;
        } else {
            Component innerComponent = frame.getRegisteredComponent(elements[0]);
            if (innerComponent instanceof Component.Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((Component.Container) innerComponent).getComponent(subPath);
            } else if (innerComponent instanceof Component.HasNamedComponents) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((Component.HasNamedComponents) innerComponent).getComponent(subPath);
            } else if (innerComponent instanceof FieldGroup) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));

                return ((FieldGroup) innerComponent).getFieldComponent(subPath);
            }
            return null;
        }
    }

    @Nullable
    public static Component getComponent(Component.Container container, String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            final com.haulmont.cuba.gui.components.Component component = container.getOwnComponent(id);

            if (component == null)
                return getComponentByIteration(container, id);
            else
                return component;

        } else {
            Component innerComponent = container.getOwnComponent(elements[0]);

            if (innerComponent == null) {
                return getComponentByIteration(container, id);
            } else {
                if (innerComponent instanceof Component.Container) {
                    final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                    String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                    return ((com.haulmont.cuba.gui.components.Component.Container) innerComponent).getComponent(subPath);
                } else if (innerComponent instanceof Component.HasNamedComponents) {
                    final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                    String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                    return ((Component.HasNamedComponents) innerComponent).getComponent(subPath);
                } else if (innerComponent instanceof com.haulmont.cuba.gui.components.FieldGroup) {
                    final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                    String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));

                    return ((FieldGroup) innerComponent).getFieldComponent(subPath);
                }
                return null;
            }
        }
    }

    @Nullable
    private static Component getComponentByIteration(Component.Container container, String id) {
        for (Component component : container.getOwnComponents()) {
            if (id.equals(component.getId())) {
                return component;
            } else if (component instanceof Component.Container) {
                Component innerComponent = getComponentByIteration((Component.Container) component, id);
                if (innerComponent != null) {
                    return innerComponent;
                }
            }
        }
        return null;
    }

    private static void fillChildComponents(Component.Container container, Collection<Component> components) {
        final Collection<Component> ownComponents = container.getOwnComponents();
        components.addAll(ownComponents);

        for (Component component : ownComponents) {
            if (component instanceof Component.Container) {
                fillChildComponents((Component.Container) component, components);
            }
        }
    }

    /**
     * Searches for a component by identifier, down by the hierarchy of frames.
     * @param frame frame to start from
     * @param id    component identifier
     * @return      component instance or null if not found
     */
    @Nullable
    public static Component findComponent(Frame frame, String id) {
        Component find = frame.getComponent(id);
        if (find != null) {
            return find;
        } else {
            for (Component c : frame.getComponents()) {
                if (c instanceof Frame) {
                    Component comp = ((Frame) c).getComponent(id);
                    if (comp != null) {
                        return comp;
                    } else {
                        findComponent((Frame) c, id);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Visit all components below the specified container.
     * @param container container to start from
     * @param visitor   visitor instance
     */
    public static void walkComponents(
            com.haulmont.cuba.gui.components.Component.Container container,
            ComponentVisitor visitor
    ) {
        __walkComponents(container, visitor, "");
    }

    private static void __walkComponents(
            com.haulmont.cuba.gui.components.Component.Container container,
            ComponentVisitor visitor,
            String path
    ) {
        for (com.haulmont.cuba.gui.components.Component component : container.getOwnComponents()) {
            String id = component.getId();
            if (id == null && component instanceof Component.ActionOwner
                    && ((Component.ActionOwner) component).getAction() != null) {
                id = ((Component.ActionOwner) component).getAction().getId();
            }
            if (id == null) {
                id = component.getClass().getSimpleName();
            }
            visitor.visit(component, path + id);

            if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                String p = component instanceof Frame ?
                        path + id + "." :
                        path;
                __walkComponents(((com.haulmont.cuba.gui.components.Component.Container) component), visitor, p);
            } else if (component instanceof AppWorkArea) {
                AppWorkArea workArea = (AppWorkArea) component;
                if (workArea.getState() == AppWorkArea.State.INITIAL_LAYOUT) {
                    VBoxLayout initialLayout = workArea.getInitialLayout();

                    __walkComponents(initialLayout, visitor, path);
                }
            }
        }
    }

    /**
     * Find first component by predicate
     *
     * @param container container to start from
     * @param finder   finder instance
     */
    public static boolean walkComponents(com.haulmont.cuba.gui.components.Component.Container container,
                                      ComponentFinder finder) {
        return __walkComponents(container, finder);
    }

    private static boolean __walkComponents(com.haulmont.cuba.gui.components.Component.Container container,
                                            ComponentFinder finder) {
        for (com.haulmont.cuba.gui.components.Component component : container.getOwnComponents()) {
            if (finder.visit(component)) {
                return true;
            }

            if (component instanceof com.haulmont.cuba.gui.components.Component.Container) {
                if (__walkComponents(((com.haulmont.cuba.gui.components.Component.Container) component), finder)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getFilterComponentPath(Filter filter) {
        StringBuilder sb = new StringBuilder(filter.getId() != null ? filter.getId() : "filterWithoutId");
        Frame frame = filter.getFrame();
        while (frame != null) {
            sb.insert(0, ".");
            String s = frame.getId() != null ? frame.getId() : "frameWithoutId";
            if (s.contains(".")) {
                s = "[" + s + "]";
            }
            sb.insert(0, s);
            if (frame instanceof Window) {
                break;
            }
            frame = frame.getFrame();
        }
        return sb.toString();
    }

    /**
     * Get the topmost window for the specified component.
     * @param component component instance
     * @return          topmost window in the hierarchy of frames for this component.
     * <br/>If the window has a controller class, an instance of the controller is returned.
     * <br/>Can be null only if the component wasn't properly initialized.
     */
    @Nullable
    public static Window getWindow(Component.BelongToFrame component) {
        Frame frame = component.getFrame();
        while (frame != null) {
            if (frame instanceof Window && frame.getFrame() == frame) {
                Window window = (Window) frame;
                return window instanceof WrappedWindow ? ((WrappedWindow) window).getWrapper() : window;
            }
            frame = frame.getFrame();
        }
        return null;
    }

    /**
     * Get the topmost window for the specified component.
     * @param component component instance
     * @return          topmost client specific window in the hierarchy of frames for this component.
     *
     * <br/>Can be null only if the component wasn't properly initialized.
     */
    @Nullable
    public static Window getWindowImplementation(Component.BelongToFrame component) {
        Frame frame = component.getFrame();
        while (frame != null) {
            if (frame instanceof Window && frame.getFrame() == frame) {
                Window window = (Window) frame;
                return window instanceof Window.Wrapper ? ((Window.Wrapper) window).getWrappedWindow() : window;
            }
            frame = frame.getFrame();
        }
        return null;
    }

    public static Frame getFrameController(Frame frame) {
        if (frame instanceof WrappedFrame) {
            return  ((WrappedFrame) frame).getWrapper();
        } else if (frame instanceof WrappedWindow) {
            return ((WrappedWindow) frame).getWrapper();
        } else {
            return frame;
        }
    }

    public static String getFullFrameId(Frame frame) {
        LinkedList<String> frameIds = new LinkedList<>();
        frameIds.addFirst(frame.getId());
        while (frame != null && !(frame instanceof Window) && frame != frame.getFrame()) {
            frame = frame.getFrame();
            if (frame != null)
                frameIds.addFirst(frame.getId());
        }
        return StringUtils.join(frameIds, '.');
    }

    /**
     * Searches for an action by name.
     * @param actionName    action name, can be a path to an action contained in some {@link Component.ActionsHolder}
     * @param frame         current frame
     * @return              action instance or null if there is no action with the specified name
     * @throws IllegalStateException    if the component denoted by the path doesn't exist or is not an ActionsHolder
     */
    @Nullable
    public static Action findAction(String actionName, Frame frame) {
        String[] elements = ValuePathHelper.parse(actionName);
        if (elements.length > 1) {
            String id = elements[elements.length - 1];

            String[] subPath = (String[]) ArrayUtils.subarray(elements, 0, elements.length - 1);
            Component component = frame.getComponent(ValuePathHelper.format(subPath));
            if (component != null) {
                if (component instanceof Component.ActionsHolder) {
                    return ((Component.ActionsHolder) component).getAction(id);
                } else {
                    throw new IllegalArgumentException(
                            String.format("Component '%s' can't contain actions", Arrays.toString(subPath)));
                }
            } else {
                throw new IllegalArgumentException(
                        String.format("Can't find component '%s'", Arrays.toString(subPath)));
            }
        } else if (elements.length == 1) {
            String id = elements[0];
            return frame.getAction(id);
        } else {
            throw new IllegalArgumentException("Invalid action name: " + actionName);
        }
    }

    public static String getComponentPath(Component c) {
        StringBuilder sb = new StringBuilder(c.getId() == null ? "" : c.getId());
        if (c instanceof Component.BelongToFrame) {
            Frame frame = ((Component.BelongToFrame) c).getFrame();
            while (frame != null) {
                sb.insert(0, ".");
                String s = frame.getId();
                if (s.contains("."))
                    s = "[" + s + "]";
                sb.insert(0, s);
                if (frame instanceof Window)
                    break;
                frame = frame.getFrame();
            }
        }
        return sb.toString();
    }

    public static String getComponentWidth(Component c) {
        float width = c.getWidth();
        int widthUnit = c.getWidthUnits();
        return width + UNIT_SYMBOLS[widthUnit];
    }

    public static String getComponentHeigth(Component c) {
        float height = c.getHeight();
        int heightUnit = c.getHeightUnits();
        return height + UNIT_SYMBOLS[heightUnit];
    }

    public static boolean hasFullWidth(Component c) {
        return (int) c.getWidth() == 100 && c.getWidthUnits() == Component.UNITS_PERCENTAGE;
    }

    public static boolean hasFullHeight(Component c) {
        return (int) c.getHeight() == 100 && c.getHeightUnits() == Component.UNITS_PERCENTAGE;
    }

    /**
     * Creates standard Create, Edit and Remove actions for the component
     * @param owner List, Table or Tree component
     */
    public static void createActions(ListComponent owner) {
        createActions(owner, EnumSet.of(ListActionType.CREATE, ListActionType.EDIT, ListActionType.REMOVE));
    }

    /**
     * Creates standard actions for the component
     * @param owner List, Table or Tree component
     * @param actions set of actions to create
     */
    public static void createActions(ListComponent owner, EnumSet<ListActionType> actions) {
        if (actions.contains(ListActionType.CREATE))
            owner.addAction(new CreateAction(owner));

        if (actions.contains(ListActionType.EDIT))
            owner.addAction(new EditAction(owner));

        if (actions.contains(ListActionType.REMOVE))
            owner.addAction(new RemoveAction(owner));

        if (actions.contains(ListActionType.REFRESH))
            owner.addAction(new RefreshAction(owner));
    }

    /**
     * Converts \n and \t symbols to HTML form.
     *
     * @param message HTML text
     * @return HTML text or null if the input is null
     */
    public static String preprocessHtmlMessage(String message) {
        if (message == null)
            return null;
        String html = StringUtils.replace(message, "\n", "<br/>");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return html;
    }

    /**
     * Place component with error message to validation errors container.
     *
     * @param component validatable component
     * @param e         exception
     * @param errors    errors container
     */
    public static void fillErrorMessages(Component.Validatable component, ValidationException e, ValidationErrors errors) {
        if (e instanceof FieldGroup.FieldsValidationException && component instanceof FieldGroup) {
            FieldGroup fieldGroup = (FieldGroup) component;

            Map<FieldGroup.FieldConfig, Exception> fields = ((FieldGroup.FieldsValidationException) e).getProblemFields();
            for (Map.Entry<FieldGroup.FieldConfig, Exception> problem : fields.entrySet()) {
                Component fieldComponent = fieldGroup.getFieldComponent(problem.getKey());
                errors.add(fieldComponent, problem.getValue().getMessage());
            }
        } else if (e instanceof RequiredValueMissingException) {
            errors.add(((RequiredValueMissingException) e).getComponent(), e.getMessage());
        } else {
            errors.add((Component) component, e.getMessage());
        }
    }

    public static int indexOf(Iterable<Component> components, Component component) {
        Preconditions.checkNotNullArgument(components);

        Iterator<Component> iterator = components.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            Component current = iterator.next();
            if (current == component) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set field's "required" flag to false if the value has been filtered by Row Level Security
     * This is necessary to allow user to submit form with filtered attribute even if attribute is required
     */
    public static void handleFilteredAttributes(Field component, Datasource datasource, MetaPropertyPath mpp) {
        if (component.isRequired()
                && datasource.getState() == Datasource.State.VALID
                && datasource.getItem() != null
                && mpp.getMetaProperty().getRange().isClass()) {

            Entity targetItem = datasource.getItem();

            MetaProperty[] propertiesChain = mpp.getMetaProperties();
            if (propertiesChain.length > 1) {
                List<String> basePropertiesList = Arrays.stream(propertiesChain)
                        .limit(propertiesChain.length - 1)
                        .map(MetadataObject::getName)
                        .collect(Collectors.toList());

                String basePropertyItem = StringUtils.join(basePropertiesList, '.');
                targetItem = datasource.getItem().getValueEx(basePropertyItem);
            }

            if (targetItem instanceof BaseGenericIdEntity) {
                String metaPropertyName = mpp.getMetaProperty().getName();
                Object value = targetItem.getValue(metaPropertyName);

                BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) targetItem;
                String[] filteredAttributes = getFilteredAttributes(baseGenericIdEntity);

                if (value == null && filteredAttributes != null
                        && ArrayUtils.contains(filteredAttributes, metaPropertyName)) {
                    component.setRequired(false);
                }
            }
        }
    }

    public static int findActionById(List<Action> actionList, String actionId) {
        int oldIndex = -1;
        for (int i = 0; i < actionList.size(); i++) {
            Action a = actionList.get(i);
            if (ObjectUtils.equals(a.getId(), actionId)) {
                oldIndex = i;
                break;
            }
        }
        return oldIndex;
    }
}