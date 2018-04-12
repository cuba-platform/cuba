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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetadataObject;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.getFilteredAttributes;

/**
 * Utility class working with GenericUI components.
 */
public abstract class ComponentsHelper {
    @Deprecated
    public static final String[] UNIT_SYMBOLS = { "px", "pt", "pc", "em", "ex", "mm", "cm", "in", "%" };

    /**
     * Returns the collection of components within the specified container and all of its children.
     *
     * @param container container to start from
     * @return          collection of components
     */
    public static Collection<Component> getComponents(ComponentContainer container) {
        // do not return LinkedHashSet, it uses much more memory than ArrayList
        Collection<Component> res = new ArrayList<>();

        fillChildComponents(container, res);

        if (res.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(res);
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
            if (innerComponent instanceof FieldGroup) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));

                FieldGroup fieldGroup = (FieldGroup) innerComponent;
                FieldGroup.FieldConfig field = fieldGroup.getField(subPath);

                return field != null ? field.getComponent() : null;
            } else if (innerComponent instanceof ComponentContainer) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((ComponentContainer) innerComponent).getComponent(subPath);
            } else if (innerComponent instanceof HasNamedComponents) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((HasNamedComponents) innerComponent).getComponent(subPath);
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
            if (innerComponent instanceof FieldGroup) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));

                FieldGroup fieldGroup = (FieldGroup) innerComponent;
                FieldGroup.FieldConfig field = fieldGroup.getField(subPath);

                return field != null ? field.getComponent() : null;
            } else if (innerComponent instanceof ComponentContainer) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((ComponentContainer) innerComponent).getComponent(subPath);
            } else if (innerComponent instanceof HasNamedComponents) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return ((HasNamedComponents) innerComponent).getComponent(subPath);
            }

            return null;
        }
    }

    @Nullable
    public static Component getComponent(ComponentContainer container, String id) {
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
                if (innerComponent instanceof FieldGroup) {
                    final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                    String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));

                    FieldGroup fieldGroup = (FieldGroup) innerComponent;
                    FieldGroup.FieldConfig field = fieldGroup.getField(subPath);

                    return field != null ? field.getComponent() : null;
                } else if (innerComponent instanceof ComponentContainer) {
                    final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                    String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                    return ((com.haulmont.cuba.gui.components.ComponentContainer) innerComponent).getComponent(subPath);
                } else if (innerComponent instanceof HasNamedComponents) {
                    final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                    String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                    return ((HasNamedComponents) innerComponent).getComponent(subPath);
                }

                return null;
            }
        }
    }

    @Nullable
    private static Component getComponentByIteration(ComponentContainer container, String id) {
        for (Component component : container.getOwnComponents()) {
            if (id.equals(component.getId())) {
                return component;
            } else if (component instanceof ComponentContainer) {
                Component innerComponent = getComponentByIteration((ComponentContainer) component, id);
                if (innerComponent != null) {
                    return innerComponent;
                }
            }
        }
        return null;
    }

    private static void fillChildComponents(ComponentContainer container, Collection<Component> components) {
        final Collection<Component> ownComponents = container.getOwnComponents();
        components.addAll(ownComponents);

        for (Component component : ownComponents) {
            if (component instanceof ComponentContainer) {
                fillChildComponents((ComponentContainer) component, components);
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
                        return findComponent((Frame) c, id);
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
            com.haulmont.cuba.gui.components.ComponentContainer container,
            ComponentVisitor visitor
    ) {
        __walkComponents(container, visitor, "");
    }

    private static void __walkComponents(
            com.haulmont.cuba.gui.components.ComponentContainer container,
            ComponentVisitor visitor,
            String path
    ) {
        for (com.haulmont.cuba.gui.components.Component component : container.getOwnComponents()) {
            String id = component.getId();
            if (id == null && component instanceof ActionOwner
                    && ((ActionOwner) component).getAction() != null) {
                id = ((ActionOwner) component).getAction().getId();
            }
            if (id == null) {
                id = component.getClass().getSimpleName();
            }
            visitor.visit(component, path + id);

            if (component instanceof com.haulmont.cuba.gui.components.ComponentContainer) {
                String p = component instanceof Frame ?
                        path + id + "." :
                        path;
                __walkComponents(((com.haulmont.cuba.gui.components.ComponentContainer) component), visitor, p);
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
    public static boolean walkComponents(com.haulmont.cuba.gui.components.ComponentContainer container,
                                      ComponentFinder finder) {
        return __walkComponents(container, finder);
    }

    private static boolean __walkComponents(com.haulmont.cuba.gui.components.ComponentContainer container,
                                            ComponentFinder finder) {
        for (com.haulmont.cuba.gui.components.Component component : container.getOwnComponents()) {
            if (finder.visit(component)) {
                return true;
            }

            if (component instanceof com.haulmont.cuba.gui.components.ComponentContainer) {
                if (__walkComponents(((com.haulmont.cuba.gui.components.ComponentContainer) component), finder)) {
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
     * <br>If the window has a controller class, an instance of the controller is returned.
     * <br>Can be null only if the component wasn't properly initialized.
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
     * <br>Can be null only if the component wasn't properly initialized.
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
     * @param actionName    action name, can be a path to an action contained in some {@link ActionsHolder}
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
                if (component instanceof ActionsHolder) {
                    return ((ActionsHolder) component).getAction(id);
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
        SizeUnit widthUnit = c.getWidthSizeUnit();
        return width + widthUnit.getSymbol();
    }

    // FIXME: gg, fix typo in the method name?
    public static String getComponentHeigth(Component c) {
        float height = c.getHeight();
        SizeUnit heightUnit = c.getHeightSizeUnit();
        return height + heightUnit.getSymbol();
    }

    public static boolean hasFullWidth(Component c) {
        return (int) c.getWidth() == 100 && c.getWidthSizeUnit() == SizeUnit.PERCENTAGE;
    }

    public static boolean hasFullHeight(Component c) {
        return (int) c.getHeight() == 100 && c.getHeightSizeUnit() == SizeUnit.PERCENTAGE;
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
            owner.addAction(CreateAction.create(owner));

        if (actions.contains(ListActionType.EDIT))
            owner.addAction(EditAction.create(owner));

        if (actions.contains(ListActionType.REMOVE))
            owner.addAction(RemoveAction.create(owner));

        if (actions.contains(ListActionType.REFRESH))
            owner.addAction(RefreshAction.create(owner));
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
    public static void fillErrorMessages(Validatable component, ValidationException e,
                                         ValidationErrors errors) {
        if (e instanceof ValidationException.HasRelatedComponent) {
            errors.add(((ValidationException.HasRelatedComponent) e).getComponent(), e.getMessage());
        } else if (e instanceof CompositeValidationException) {
            for (CompositeValidationException.ViolationCause cause : ((CompositeValidationException) e).getCauses()) {
                errors.add((Component) component, cause.getMessage());
            }
        } else if (e instanceof FieldGroup.FieldsValidationException) {
            FieldGroup.FieldsValidationException fve = (FieldGroup.FieldsValidationException) e;
            Map<Validatable, ValidationException> fields = fve.getProblemFields();
            for (Map.Entry<Validatable, ValidationException> problem : fields.entrySet()) {
                ValidationException exception = problem.getValue();

                fillErrorMessages(problem.getKey(), exception, errors);
            }
        } else {
            errors.add((Component) component, e.getMessage());
        }
    }

    /**
     * @deprecated Use guava {@link Iterables#indexOf(Iterable, Predicate)}
     */
    @Deprecated
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
                String basePropertyItem = Arrays.stream(propertiesChain)
                        .limit(propertiesChain.length - 1)
                        .map(MetadataObject::getName)
                        .collect(Collectors.joining("."));

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
            if (Objects.equals(a.getId(), actionId)) {
                oldIndex = i;
                break;
            }
        }
        return oldIndex;
    }

    /**
     * INTERNAL.
     * Adds actions specified in {@link Lookup} annotation on entity attribute to the given PickerField.
     */
    public static boolean createActionsByMetaAnnotations(PickerField pickerField) {
        MetaPropertyPath mpp = pickerField.getMetaPropertyPath();
        if (mpp == null)
            return false;

        String[] actions = (String[]) AppBeans.get(MetadataTools.class)
                .getMetaAnnotationAttributes(mpp.getMetaProperty().getAnnotations(), Lookup.class)
                .get("actions");
        if (actions != null && actions.length > 0) {
            for (String actionId : actions) {
                for (PickerField.ActionType actionType : PickerField.ActionType.values()) {
                    if (actionType.getId().equals(actionId.trim())) {
                        pickerField.addAction(actionType.createAction(pickerField));
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static SizeUnit convertToSizeUnit(int unit) {
        switch (unit) {
            case Component.UNITS_PIXELS:
                return SizeUnit.PIXELS;
            case Component.UNITS_PERCENTAGE:
                return SizeUnit.PERCENTAGE;
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }

    public static int convertFromSizeUnit(SizeUnit unit) {
        switch (unit) {
            case PIXELS:
                return Component.UNITS_PIXELS;
            case PERCENTAGE:
                return Component.UNITS_PERCENTAGE;
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }
}