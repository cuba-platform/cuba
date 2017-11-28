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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.presentations.Presentations;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Root of the GenericUI components hierarchy.
 */
public interface Component {

    enum Alignment {
        TOP_RIGHT,
        TOP_LEFT,
        TOP_CENTER,
        MIDDLE_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER
    }

    int UNITS_PIXELS = 0;
    int UNITS_PERCENTAGE = 8;

    String AUTO_SIZE = "-1px";
    int AUTO_SIZE_PX = -1;

    /** Component ID as defined in {@code id} attribute */
    String getId();
    /** Set component ID */
    void setId(String id);

    /**
     * @return Parent of component.
     */
    Component getParent();
    /**
     * INTERNAL.<br>
     *
     * {@link Component.Container#add(Component)} is normally used for adding components
     * to a parent and the used method will call this method implicitly.
     *
     * @param parent Parent component
     */
    void setParent(Component parent);

    String getDebugId();
    /** INTERNAL. Managed by debug Id system. */
    void setDebugId(String id);

    /**
     * Are the component and its parent enabled?
     */
    boolean isEnabled();
    /** Set component enabled state */
    void setEnabled(boolean enabled);

    /**
     * Is the component responsive?
     */
    boolean isResponsive();
    /**
     * Set component to be responsive by width and height.
     *
     * If responsive flag is true then you can use conditional CSS rules that respond to size changes in the browser.
     * You can set specific rules using "width-range" or "height-range" properties in CSS files.
     */
    void setResponsive(boolean responsive);

    /**
     * Are the component and its parent visible?
     */
    boolean isVisible();
    /** Set component visibility */
    void setVisible(boolean visible);

    /**
     * Is the component visible regardless of the parent?
     */
    boolean isVisibleItself();

    /**
     * Is the component enabled regardless of the parent?
     */
    boolean isEnabledItself();

    /** Set focus to this component */
    void requestFocus();

    /** Get component height in {@link #getHeightUnits()} */
    float getHeight();

    /** Height units: {@link #UNITS_PIXELS}, {@link #UNITS_PERCENTAGE} */
    int getHeightUnits();

    /** Set component height in {@link #getHeightUnits()} */
    void setHeight(String height);

    /** Set component height to {@link #AUTO_SIZE} */
    default void setHeightAuto() {
        setHeight(AUTO_SIZE);
    }

    /** Set component height to 100% */
    default void setHeightFull() {
        setHeight("100%");
    }

    /** Get component width in {@link #getWidthUnits()} */
    float getWidth();

    /** Width units: {@link #UNITS_PIXELS}, {@link #UNITS_PERCENTAGE} */
    int getWidthUnits();

    /** Set component width in {@link #getWidthUnits()} */
    void setWidth(String width);

    /** Set component width to {@link #AUTO_SIZE} */
    default void setWidthAuto() {
        setWidth(AUTO_SIZE);
    }

    /** Set component width to 100% */
    default void setWidthFull() {
        setWidth("100%");
    }

    /** Set component width and height to 100% */
    default void setSizeFull() {
        setWidth("100%");
        setHeight("100%");
    }

    /** Set component width and height to {@link #AUTO_SIZE} */
    default void setSizeAuto() {
        setWidth(AUTO_SIZE);
        setHeight(AUTO_SIZE);
    }

    Alignment getAlignment();
    void setAlignment(Alignment alignment);

    /**
     * Styles implementation is client-type-specific.
     *
     * @return current style name.
     */
    String getStyleName();

    /**
     * Sets one or more style names of the component, replacing any
     * previous styles. Multiple styles can be specified as a
     * space-separated list of style names.
     *
     * Styles implementation is client-type-specific.
     *
     * @param styleName one or more style names separated by space.
     * */
    void setStyleName(String styleName);

    /**
     * Adds one or more style names to this component. Multiple styles can be
     * specified as a space-separated list of style names.
     *
     * @param styleName one or more style names separated by space.
     */
    void addStyleName(String styleName);

    /**
     * Removes one or more style names from component. Multiple styles can be
     * specified as a space-separated list of style names.
     *
     * @param styleName one or more style names separated by space.
     */
    void removeStyleName(String styleName);

    /**
     * Get client specific component instance. Can be used in client module to simplify invocation of underlying API.
     * <br>
     * Example:
     * <pre>
     * com.vaadin.ui.TextField vTextField = textField.unwrap(com.vaadin.ui.TextField.class);
     * </pre>
     *
     * @param internalComponentClass class of underlying component implementation based on Vaadin or Swing
     * @param <X> type of internal class
     * @return internal client specific component
     */
    <X> X unwrap(Class<X> internalComponentClass);

    /**
     * Get the outmost external container of client specific component instance. Can be used in client module to simplify invocation of underlying API.
     * <br>
     * Example:
     * <pre>
     * com.vaadin.ui.Layout vLayout = table.unwrapComposition(com.vaadin.ui.Layout.class);
     * </pre>
     *
     * @param internalCompositionClass class of underlying composition implementation based on Vaadin or Swing
     * @param <X> type of internal class
     * @return internal client specific component
     */
    <X> X unwrapComposition(Class<X> internalCompositionClass);

    /**
     * Component which can contain other components
     */
    interface Container extends Component {
        void add(Component childComponent);
        void remove(Component childComponent);

        void removeAll();

        /**
         * Get component directly owned by this container.
         * @return component or null if not found
         */
        @Nullable
        Component getOwnComponent(String id);

        /**
         * Get component belonging to the whole components tree below this container.
         * @return component or null if not found
         */
        @Nullable
        Component getComponent(String id);

        /**
         * Get component belonging to the whole components tree below this container.
         *
         * @return component. Throws exception if not found.
         */
        @Nonnull
        default Component getComponentNN(String id) {
            Component component = getComponent(id);
            if (component == null) {
                throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
            }
            return component;
        }

        /** Get all components directly owned by this container */
        Collection<Component> getOwnComponents();

        /** Get all components belonging to the whole components tree below this container */
        Collection<Component> getComponents();
    }

    /**
     * Component which can contain other components and provides indexed access to children.
     */
    interface OrderedContainer extends Container {
        void add(Component childComponent, int index);
        int indexOf(Component component);

        /**
         * Returns the component at the given position.
         *
         * @param index component index
         * @return the component at the given index or null.
         */
        @Nullable
        Component getComponent(int index);

        /**
         * Returns the component at the given position.
         *
         * @param index component index
         * @return the component at the given index. Throws exception if not found.
         */
        @Nonnull
        default Component getComponentNN(int index) {
            Component component = getComponent(index);
            if (component == null) {
                throw new IllegalArgumentException(
                        String.format("Not found component by index %s", index)
                );
            }

            return component;
        }
    }

    /**
     * Component which can contain other components and provides access "by-name" to children.
     */
    interface HasNamedComponents {
        /**
         * Get subcomponent by name.
         * @return component or null if not found
         */
        @Nullable
        Component getComponent(String id);
    }

    /**
     * Component delegating work to some "wrapped" client-specific implementation
     */
    interface Wrapper extends Component {
        Object getComponent();
        Object getComposition();
    }

    /**
     * Component belonging to a frame
     */
    interface BelongToFrame extends Component {
        Frame getFrame();
        void setFrame(Frame frame);
    }

    /**
     * Object having a caption
     */
    interface HasCaption {
        String getCaption();
        void setCaption(String caption);

        String getDescription();
        void setDescription(String description);
    }

    /**
     * Layout having a mouse click listener.
     */
    interface LayoutClickNotifier {
        void addLayoutClickListener(LayoutClickListener listener);
        void removeLayoutClickListener(LayoutClickListener listener);
    }

    /**
     * Listener fired when user clicks inside the layout at any place.
     */
    @FunctionalInterface
    interface LayoutClickListener {
        void layoutClick(LayoutClickEvent event);
    }

    /**
     * Describes layout click event.
     * Event contains a data about layout, nested component and mouse event.
     */
    class LayoutClickEvent extends EventObject {
        private final Component childComponent;
        private final MouseEventDetails mouseEventDetails;

        public LayoutClickEvent(Container layout,
                                Component childComponent, MouseEventDetails mouseEventDetails) {
            super(layout);
            this.childComponent = childComponent;
            this.mouseEventDetails = mouseEventDetails;
        }

        @Override
        public Container getSource() {
            return (Container) super.getSource();
        }

        public Component getChildComponent() {
            return childComponent;
        }

        public MouseEventDetails getMouseEventDetails() {
            return mouseEventDetails;
        }
    }

    /**
     * Component having a shortcut listener.
     */
    interface ShortcutNotifier {
        void addShortcutAction(ShortcutAction action);
        void removeShortcutAction(ShortcutAction action);
    }

    /**
     * The ShortcutAction is triggered when the user presses a given key combination.
     */
    class ShortcutAction {
        protected final KeyCombination shortcut;
        protected final Consumer<ShortcutTriggeredEvent> handler;

        public ShortcutAction(String shortcut, Consumer<ShortcutTriggeredEvent> handler) {
            this(KeyCombination.create(shortcut), handler);
        }

        public ShortcutAction(KeyCombination shortcut, Consumer<ShortcutTriggeredEvent> handler) {
            this.shortcut = shortcut;
            this.handler = handler;
        }

        /**
         * @return the key combination that the shortcut reacts to
         */
        public KeyCombination getShortcutCombination() {
            return shortcut;
        }

        /**
         * @return the handler invoked when the shortcut is triggered
         */
        public Consumer<ShortcutTriggeredEvent> getHandler() {
            return handler;
        }
    }

    /**
     * Describes shortcut triggered event.
     * The event contains a data about source component and target component.
     */
    class ShortcutTriggeredEvent extends EventObject {
        private final Component target;

        /**
         * Constructs a shortcut triggered event.
         *
         * @param source the component on which the Event initially occurred
         * @param target the component which was focused when the Event occurred
         * @throws IllegalArgumentException if source is null
         */
        public ShortcutTriggeredEvent(Component source, Component target) {
            super(source);
            this.target = target;
        }

        @Override
        public Component getSource() {
            return (Component) super.getSource();
        }

        /**
         * @return the component which was focused when the Event occurred
         */
        public Component getTarget() {
            return target;
        }
    }

    /**
     * Object having a border
     */
    interface HasBorder {
        boolean isBorderVisible();
        void setBorderVisible(boolean borderVisible);
    }

    /**
     * Describes value change event.
     */
    class ValueChangeEvent {
        private final Component.HasValue component;
        private final Object prevValue;
        private final Object value;

        public ValueChangeEvent(Component.HasValue component, Object prevValue, Object value) {
            this.component = component;
            this.prevValue = prevValue;
            this.value = value;
        }

        /**
         * @return component
         */
        public Component.HasValue getComponent() {
            return component;
        }

        /**
         * @return previous value
         */
        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * @return current value
         */
        @Nullable
        public Object getValue() {
            return value;
        }
    }

    /**
     * Listener to value change events.
     */
    @FunctionalInterface
    interface ValueChangeListener {
        /**
         * Called when value of Component changed.
         *
         * @param e event object
         */
        void valueChanged(ValueChangeEvent e);
    }

    /**
     * Object having a value.
     */
    interface HasValue extends Editable, BelongToFrame {
        <T> T getValue();

        void setValue(Object value);

        /**
         * @deprecated Use {@link #addValueChangeListener(ValueChangeListener)}
         */
        @Deprecated
        void addListener(ValueListener listener);
        @Deprecated
        void removeListener(ValueListener listener);

        void addValueChangeListener(ValueChangeListener listener);
        void removeValueChangeListener(ValueChangeListener listener);
    }

    /**
     * Object having a formatter
     */
    interface HasFormatter {
        Formatter getFormatter();
        void setFormatter(Formatter formatter);
    }

    /**
     * Object having an XML descriptor attached
     */
    interface HasXmlDescriptor {
        Element getXmlDescriptor();
        void setXmlDescriptor(Element element);
    }

    /**
     * A component containing {@link Action}s
     */
    interface ActionsHolder extends Component {
        /**
         * Add an action to the component
         */
        void addAction(Action action);

        /**
         * Add an action to the component with index.
         */
        void addAction(Action action, int index);

        /**
         * Remove the action from the component
         */
        void removeAction(@Nullable Action action);

        /**
         * Remove the action by its ID. If there is no action with that ID, nothing happens.
         */
        void removeAction(@Nullable String id);

        /**
         * Remove all actions from the component
         */
        void removeAllActions();

        /**
         * @return unmodifiable collection of actions
         */
        Collection<Action> getActions();

        /**
         * @return an action by its ID, or null if not found
         */
        @Nullable
        Action getAction(String id);

        /**
         * @return an action by its ID
         * @throws java.lang.IllegalArgumentException if not found
         */
        @Nonnull
        default Action getActionNN(String id) {
            Action action = getAction(id);
            if (action == null) {
                throw new IllegalStateException("Unable to find action with id " + id);
            }
            return action;
        }
    }

    /**
     * An {@link ActionsHolder} component that loads and controls permissions on owned actions.
     */
    interface SecuredActionsHolder extends ActionsHolder {
        /**
         * @return permissions container
         */
        ActionsPermissions getActionsPermissions();
    }

    /**
     * Component supporting "editable" state.
     * Editable means not read-only, so user can view a value but can not edit it. Not editable value can be copied to
     * clipboard.
     */
    interface Editable extends Component {
        boolean isEditable();
        void setEditable(boolean editable);

        default boolean isEditableWithParent() {
            if (getParent() instanceof ChildEditableController) {
                return isEditable() && ((ChildEditableController) getParent()).isEditable();
            }
            return isEditable();
        }
    }

    /**
     * Component that manages editable property of child components.
     */
    interface ChildEditableController extends Editable {
    }

    /**
     * Event that is fired when "editable" property of Editable component has been changed.
     */
    class EditableChangeEvent extends EventObject {
        public EditableChangeEvent(Component.Editable source) {
            super(source);
        }

        @Override
        public Component.Editable getSource() {
            return (Editable) super.getSource();
        }
    }

    @FunctionalInterface
    interface EditableChangeListener {
        void editableChanged(EditableChangeEvent event);
    }

    /**
     * Component that fires EditableChangeEvent events.
     */
    interface EditableChangeNotifier {
        void addEditableChangeListener(EditableChangeListener listener);
        void removeEditableChangeListener(EditableChangeListener listener);
    }

    /**
     * Component supporting "focusable" state.
     * Focusable means that component can be focused by TAB button.
     */
    interface Focusable extends Component {
        /**
         * Is component focusable?
         */
        default boolean isFocusable() {
            return getTabIndex() >= 0;
        }
        /**
         * Set component focusability
         */
        default void setFocusable(boolean focusable) {
            setTabIndex(-1);
        }

        /**
         * Gets the <i>tabulator index</i> of the {@code HasTabIndex} component.
         *
         * @return tab index set for the {@code HasTabIndex} component
         */
        int getTabIndex();

        /**
         * Sets the <i>tabulator index</i> of the {@code Focusable} component.
         * The tab index property is used to specify the order in which the
         * fields are focused when the user presses the Tab key. Components with
         * a defined tab index are focused sequentially first, and then the
         * components with no tab index.
         *
         * @param tabIndex tab index
         */
        void setTabIndex(int tabIndex);
    }

    /**
     * Object supporting save/restore of user settings.
     * @see com.haulmont.cuba.security.app.UserSettingService
     */
    interface HasSettings {
        void applySettings(Element element);
        boolean saveSettings(Element element);

        boolean isSettingsEnabled();
        void setSettingsEnabled(boolean settingsEnabled);
    }

    /**
     * Describes expanded state change event of {@link com.haulmont.cuba.gui.components.Component.Collapsable}.
     */
    class ExpandedStateChangeEvent {
        private final Component.Collapsable component;
        private final boolean expanded;

        public ExpandedStateChangeEvent(Collapsable component, boolean expanded) {
            this.component = component;
            this.expanded = expanded;
        }

        public Collapsable getComponent() {
            return component;
        }

        /**
         * @return true if Component has been expanded.
         */
        public boolean isExpanded() {
            return expanded;
        }
    }

    /**
     * Listener to expanded state change events.
     */
    @FunctionalInterface
    interface ExpandedStateChangeListener {
        /**
         * Called when expanded state of {@link com.haulmont.cuba.gui.components.Component.Collapsable} changed.
         *
         * @param e event object
         */
        void expandedStateChanged(ExpandedStateChangeEvent e);
    }

    /**
     * Is able to collapse (folding)
     */
    interface Collapsable extends Component {
        boolean isExpanded();
        void setExpanded(boolean expanded);

        boolean isCollapsable();
        void setCollapsable(boolean collapsable);

        @Deprecated
        void addListener(ExpandListener listener);
        @Deprecated
        void removeListener(ExpandListener listener);

        @Deprecated
        void addListener(CollapseListener listener);
        @Deprecated
        void removeListener(CollapseListener listener);

        @Deprecated
        interface ExpandListener {
            void onExpand(Collapsable component);
        }

        @Deprecated
        interface CollapseListener {
            void onCollapse(Collapsable component);
        }

        void addExpandedStateChangeListener(ExpandedStateChangeListener listener);
        void removeExpandedStateChangeListener(ExpandedStateChangeListener listener);
    }

    interface Disposable {
        void dispose();
        boolean isDisposed();
    }

    /**
     * Component supporting an action
     */
    interface ActionOwner {
        Action getAction();
        void setAction(Action action);
    }

    /**
     * Component having an icon.
     */
    interface HasIcon {
        String getIcon();
        void setIcon(String icon);

        /**
         * Sets the given <code>icon</code> to the component.
         *
         * @param icon {@link Icons.Icon} instance
         */
        void setIconByName(Icons.Icon icon);
    }

    /**
     * Component having a buttons pancel.
     */
    interface HasButtonsPanel {
        ButtonsPanel getButtonsPanel();
        void setButtonsPanel(ButtonsPanel panel);
    }

    /**
     * Component having a {@link RowsCount} component.
     */
    interface HasRowsCount {
        RowsCount getRowsCount();
        void setRowsCount(RowsCount rowsCount);
    }

    /**
     * A component which can be validated
     */
    interface Validatable {
        boolean isValid();
        void validate() throws ValidationException;

        /**
         * Enable/disable component validation on window commit for methods
         * {@link Window#validateAll}, {@link Frame#validate},
         * {@link Frame#validateAll}, {@link Frame#isValid}
         * By default is true and component is validated on window commit.
         * For FieldGroup is false.
         */
        default boolean isValidateOnCommit() {
            return true;
        }
    }

    /**
     * Data aware component that supports buffered write mode.
     */
    interface Buffered {
        /**
         * Updates all changes since the previous commit to the data source.
         */
        void commit();

        /**
         * Discards all changes since last commit. The object updates its value from the data source.
         */
        void discard();

        /**
         * @return {@code true} if buffered mode is on, {@code false} otherwise
         */
        boolean isBuffered();

        /**
         * Sets the buffered mode.
         * <p>
         * When in buffered mode, an internal buffer will be used to store changes
         * until {@link #commit()} is called. Calling {@link #discard()} will revert
         * the internal buffer to the value of the data source.
         * <p>
         * When in non-buffered mode both read and write operations will be done
         * directly on the data source. In this mode the {@link #commit()} and
         * {@link #discard()} methods serve no purpose.
         *
         * @param buffered {@code true} if buffered mode should be turned on, {@code false} otherwise
         */
        void setBuffered(boolean buffered);

        /**
         * Tests if the value stored in the object has been modified since it was
         * last updated from the data source.
         *
         * @return {@code true} if the value in the object has been modified
         *         since the last data source update, {@code false} if not.
         */
        boolean isModified();
    }

    /**
     * Component having presentations.
     */
    interface HasPresentations extends HasSettings {
        void usePresentations(boolean b);
        boolean isUsePresentations();

        void resetPresentation();
        void loadPresentations();

        Presentations getPresentations();

        void applyPresentation(Object id);
        void applyPresentationAsDefault(Object id);

        Object getDefaultPresentationId();
    }

    /**
     * A class that implements this interface can have space between child components.
     */
    interface Spacing {
        void setSpacing(boolean enabled);
        boolean getSpacing();
    }

    /**
     * A class that implements this interface can have indentation between the outer borders and the container content.
     */
    interface Margin {
        default void setMargin(boolean enable) {
            setMargin(new MarginInfo(enable, enable, enable, enable));
        }

        default void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
            setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
        }

        void setMargin(MarginInfo marginInfo);
        MarginInfo getMargin();
    }

    /**
     * A class that implements this interface can have indentation outside the border.
     */
    interface OuterMargin {
        /**
         * Enables or disables margins on all sides simultaneously.
         *
         * @param enable if true, enables margins on all sides. If false, disables margins on all sides.
         */
        default void setOuterMargin(boolean enable) {
            setOuterMargin(new MarginInfo(enable, enable, enable, enable));
        }

        /**
         * Sets margins on all sides individually.
         *
         * @param top    enable or disable top margin
         * @param right  enable or disable right margin
         * @param bottom enable or disable bottom margin
         * @param left   enable or disable left margin
         */
        default void setOuterMargin(boolean top, boolean right, boolean bottom, boolean left) {
            setOuterMargin(new MarginInfo(top, right, bottom, left));
        }

        /**
         * Sets margins on all sides according to the passed {@link MarginInfo} object.
         *
         * @param marginInfo the {@link MarginInfo} object that describes the
         *                   margin settings for each side of a Component.
         */
        void setOuterMargin(MarginInfo marginInfo);

        /**
         * @return the {@link MarginInfo} object that describes the
         * margin settings for each side of a Component.
         */
        MarginInfo getOuterMargin();
    }

    interface HasInputPrompt {
        /**
         * @return current input prompt.
         */
        String getInputPrompt();

        /**
         * Sets the input prompt - a textual prompt that is displayed when the field
         * would otherwise be empty, to prompt the user for input.
         *
         * @param inputPrompt input prompt
         */
        void setInputPrompt(String inputPrompt);
    }

    /**
     * State of subcomponents can be managed by UI permissions.
     */
    interface UiPermissionAware {

        /**
         * Change state of subcomponent according to the {@code permissionValue}.
         *
         * @param permissionDescriptor descriptor which contains id of subcomponent and UI permission value
         *                             which will be applied to this subcomponent or ids of subcomponent and its action
         *                             and UI permission value which will be applied to subcomponent's action
         */
        void applyPermission(UiPermissionDescriptor permissionDescriptor);
    }

    /**
     * Class to store mouse event details.
     */
    class MouseEventDetails {

        /**
         * Constants for mouse buttons.
         */
        public enum MouseButton {
            LEFT,
            RIGHT,
            MIDDLE
        }

        protected MouseButton button;
        protected int clientX;
        protected int clientY;
        protected boolean altKey;
        protected boolean ctrlKey;
        protected boolean metaKey;
        protected boolean shiftKey;
        protected boolean doubleClick;
        protected int relativeX = -1;
        protected int relativeY = -1;

        public MouseEventDetails() {
        }

        public MouseButton getButton() {
            return button;
        }

        public void setButton(MouseButton button) {
            this.button = button;
        }

        public int getClientX() {
            return clientX;
        }

        public void setClientX(int clientX) {
            this.clientX = clientX;
        }

        public int getClientY() {
            return clientY;
        }

        public void setClientY(int clientY) {
            this.clientY = clientY;
        }

        public boolean isAltKey() {
            return altKey;
        }

        public void setAltKey(boolean altKey) {
            this.altKey = altKey;
        }

        public boolean isCtrlKey() {
            return ctrlKey;
        }

        public void setCtrlKey(boolean ctrlKey) {
            this.ctrlKey = ctrlKey;
        }

        public boolean isMetaKey() {
            return metaKey;
        }

        public void setMetaKey(boolean metaKey) {
            this.metaKey = metaKey;
        }

        public boolean isShiftKey() {
            return shiftKey;
        }

        public void setShiftKey(boolean shiftKey) {
            this.shiftKey = shiftKey;
        }

        public boolean isDoubleClick() {
            return doubleClick;
        }

        public void setDoubleClick(boolean doubleClick) {
            this.doubleClick = doubleClick;
        }

        public int getRelativeX() {
            return relativeX;
        }

        public void setRelativeX(int relativeX) {
            this.relativeX = relativeX;
        }

        public int getRelativeY() {
            return relativeY;
        }

        public void setRelativeY(int relativeY) {
            this.relativeY = relativeY;
        }
    }
}