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

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioProperty;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Root of the Generic UI components hierarchy.
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

    /**
     * @deprecated Use {@link SizeUnit} instead.
     */
    @Deprecated
    int UNITS_PIXELS = 0;
    /**
     * @deprecated @deprecated Use {@link SizeUnit} instead.
     */
    @Deprecated
    int UNITS_PERCENTAGE = 8;

    String AUTO_SIZE = "-1px";
    int AUTO_SIZE_PX = -1;

    String FULL_SIZE = "100%";

    /** Component ID as defined in {@code id} attribute */
    @Nullable
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    String getId();
    /** Set component ID */
    void setId(String id);

    /**
     * @return parent of component.
     */
    @Nullable
    Component getParent();
    /**
     * INTERNAL.<br>
     *
     * {@link ComponentContainer#add(Component)} is normally used for adding components
     * to a parent and the used method will call this method implicitly.
     *
     * @param parent Parent component
     */
    void setParent(@Nullable Component parent);

    /**
     * Is the component enabled?
     * <br>
     * Note that this method only returns the status of the component and does not take parents into account.
     * Even though this method returns true the component can be disabled to the user if a parent is disabled.
     *
     * @return true if the component enabled flag is set to true
     */
    boolean isEnabled();
    /**
     * Sets the component enabled state.
     * <br>
     * The user can not interact with disabled components, which are shown with a style that indicates the status.
     * Components are enabled by default.
     *
     * @param enabled enabled flag
     */
    @StudioProperty(name = "enable", defaultValue = "true")
    void setEnabled(boolean enabled);

    /**
     * Is the component responsive?
     *
     * @return true if the component applies conditional CSS rules for width / height sizes.
     */
    boolean isResponsive();
    /**
     * Sets component to be responsive by width and height.
     * <br>
     * If responsive flag is true then you can use conditional CSS rules that respond to size changes in the browser.
     * You can set specific rules using "width-range" or "height-range" properties in CSS files.
     *
     * @param responsive responsive flag
     */
    @StudioProperty(defaultValue = "false")
    void setResponsive(boolean responsive);

    /**
     * Is the component visible?
     * <br>
     * A component is visible only if all its parents are also visible. This is not checked by this method though,
     * so even if this method returns true, the component can be hidden from the user because a parent is set to invisible.
     *
     * @return true if the component visibility is set to true
     */
    boolean isVisible();
    /**
     * Sets visibility value for the component.
     * <br>
     * Visible components are drawn in the user interface, while invisible ones are not. The effect is not a cosmetic
     * CSS change - no information about an invisible component will be sent to the client. The effect is thus the same
     * as removing the component from its parent.
     *
     * @param visible visible flag
     */
    @StudioProperty(defaultValue = "true")
    void setVisible(boolean visible);

    /**
     * @return true if the component and all its parent components are visible
     */
    boolean isVisibleRecursive();

    /**
     * @return true if the component and all its parent components are enabled
     */
    boolean isEnabledRecursive();

    /**
     * Is the component visible regardless of the parent?
     *
     * @deprecated Use {{@link #isVisible()} instead.
     */
    @Deprecated
    default boolean isVisibleItself() {
        return isVisible();
    }

    /**
     * Is the component enabled regardless of the parent?
     *
     * @deprecated Use {{@link #isEnabled()} instead.
     */
    @Deprecated
    default boolean isEnabledItself() {
        return isEnabled();
    }

    /** Get component height in {@link #getHeightSizeUnit()} */
    float getHeight();

    /** Height units: {@link #UNITS_PIXELS}, {@link #UNITS_PERCENTAGE} */
    @Deprecated
    default int getHeightUnits() {
        return ComponentsHelper.convertFromSizeUnit(getHeightSizeUnit());
    }

    /**
     * Gets the height property units.
     *
     * @return units used in height property.
     */
    SizeUnit getHeightSizeUnit();

    /** Set component height in {@link #getHeightUnits()} */
    @StudioProperty(type = PropertyType.SIZE, defaultValue = "-1px")
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
    @Deprecated
    default int getWidthUnits() {
        return ComponentsHelper.convertFromSizeUnit(getWidthSizeUnit());
    }

    /**
     * Set focus to this component
     *
     * @deprecated Use {@link Focusable#focus()} instead.
     */
    @Deprecated
    default void requestFocus() {
        if (this instanceof Focusable) {
            ((Focusable) this).focus();
        }
    }

    /**
     * Gets the width property units.
     *
     * @return units used in the width property.
     */
    SizeUnit getWidthSizeUnit();

    /** Set component width in {@link #getWidthSizeUnit()}} */
    @StudioProperty(type = PropertyType.SIZE, defaultValue = "-1px")
    void setWidth(String width);

    /** Set component width to {@link #AUTO_SIZE} */
    default void setWidthAuto() {
        setWidth(AUTO_SIZE);
    }

    /** Set component width to 100% */
    default void setWidthFull() {
        setWidth(FULL_SIZE);
    }

    /** Set component width and height to 100% */
    default void setSizeFull() {
        setWidth(FULL_SIZE);
        setHeight(FULL_SIZE);
    }

    /** Set component width and height to {@link #AUTO_SIZE} */
    default void setSizeAuto() {
        setWidth(AUTO_SIZE);
        setHeight(AUTO_SIZE);
    }

    Alignment getAlignment();
    @StudioProperty(name = "align", type = PropertyType.ENUMERATION, defaultValue = "TOP_LEFT", required = true)
    void setAlignment(Alignment alignment);

    /**
     * Styles implementation is client-type-specific.
     *
     * @return current style name.
     */
    @Nullable
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
    @StudioProperty(name = "stylename", type = PropertyType.CSS_CLASSNAME_LIST)
    void setStyleName(@Nullable String styleName);

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
     * Get client specific component instance. Can be used in client module to simplify invocation of underlying API.
     * <br>
     * Returns {@code null} if underlying component cannot be casted to the given {@code internalComponentClass}.
     * Example:
     * <pre>
     * com.vaadin.ui.TextField vTextField = textField.unwrapOrNull(com.vaadin.ui.TextField.class);
     * </pre>
     *
     * @param internalComponentClass class of underlying component implementation based on Vaadin or Swing
     * @param <X> type of internal class
     * @return internal client specific component or null if it cannot be casted to given class
     */
    @Nullable
    <X> X unwrapOrNull(Class<X> internalComponentClass);

    /**
     * Performs the given {@code action} with underlying component if it can be casted to the given
     * {@code internalComponentClass}.
     * Example:
     * <pre>
     * textField.withUnwrapped(com.vaadin.ui.TextField.class, vTextField -&gt; {
     *     // do something
     * });
     * </pre>
     *
     * @param internalComponentClass class of underlying component implementation based on Vaadin or Swing
     * @param <X> type of internal class
     * @param action action to perform if underlying component can be casted to given class
     */
    <X> void withUnwrapped(Class<X> internalComponentClass, Consumer<X> action);

    /**
     * Get the outmost external container of client specific component instance.
     * Can be used in client module to simplify invocation of underlying API.
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
     * Get the outmost external container of client specific component instance.
     * Can be used in client module to simplify invocation of underlying API.
     * <br>
     * Returns null if composition cannot be casted to given {@code internalCompositionClass}.
     * Example:
     * <pre>
     * com.vaadin.ui.Layout vLayout = table.unwrapCompositionOrNull(com.vaadin.ui.Layout.class);
     * </pre>
     *
     * @param internalCompositionClass class of underlying composition implementation based on Vaadin or Swing
     * @param <X> type of internal class
     * @return internal client specific component or null if cannot be casted to given class
     */
    @Nullable
    <X> X unwrapCompositionOrNull(Class<X> internalCompositionClass);

    /**
     * Get the outmost external container of client specific component instance and performs the given {@code action}.
     * <br>
     * Can be used in client module to simplify invocation of underlying API.
     * <br>
     * Example:
     * <pre>
     * table.withUnwrappedComposition(com.vaadin.ui.Layout.class, vLayout -&gt; {
     *     // do something
     * });
     * </pre>
     *
     * @param internalCompositionClass class of underlying composition implementation based on Vaadin or Swing
     * @param <X> type of internal class
     * @param action to perform if underlying composition can be casted to given class
     */
    <X> void withUnwrappedComposition(Class<X> internalCompositionClass, Consumer<X> action);

    /**
     * Component delegating work to some "wrapped" client-specific implementation.
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
     * Object having a description.
     */
    interface HasDescription {

        /**
         * @return the components description, used in tooltips
         */
        @Nullable
        String getDescription();

        /**
         * Sets the component's description.
         *
         * @param description the new description to set
         */
        @StudioProperty(type = PropertyType.LOCALIZED_STRING)
        void setDescription(@Nullable String description);
    }

    /**
     * Object having a caption.
     */
    interface HasCaption extends HasDescription {
        /**
         * @return the caption of the component
         */
        @Nullable
        String getCaption();

        /**
         * Sets the component's caption.
         *
         * @param caption the new component's caption
         */
        @StudioProperty(type = PropertyType.LOCALIZED_STRING)
        void setCaption(@Nullable String caption);
    }

    /**
     * Object having an XML descriptor attached.
     */
    interface HasXmlDescriptor {
        Element getXmlDescriptor();
        void setXmlDescriptor(Element element);
    }

    /**
     * Component supporting "editable" state.
     * Editable means not read-only, so user can view a value but can not edit it. Not editable value can be copied to
     * clipboard.
     */
    interface Editable extends Component {
        boolean isEditable();
        @StudioProperty(defaultValue = "true")
        void setEditable(boolean editable);

        default boolean isEditableWithParent() {
            if (getParent() instanceof ChildEditableController) {
                return isEditable() && ((ChildEditableController) getParent()).isEditable();
            }
            return isEditable();
        }
    }

    /**
     * Component supporting "focusable" state.
     * Focusable means that component can be focused by TAB button.
     */
    interface Focusable extends Component {
        /** Set focus to this component */
        void focus();

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
        @StudioProperty
        void setTabIndex(int tabIndex);
    }

    interface Disposable {
        void dispose();
        boolean isDisposed();
    }

    /**
     * Component having an icon.
     */
    interface HasIcon {
        /**
         * Get icon source: "font-icon:ADD", "icons/myicon.png", "theme://createIcon", etc.
         */
        @Nullable
        String getIcon();

        /**
         * Set an icon by its source: "font-icon:ADD", "icons/myicon.png", "theme://createIcon", etc.
         */
        @StudioProperty(type = PropertyType.ICON_ID)
        void setIcon(@Nullable String icon);

        /**
         * Set an icon from an icon set.
         */
        void setIconFromSet(Icons.Icon icon);
    }

    /**
     * An object that returns stylename for the given {@code item} (option) that is displayed by the given
     * {@code component}.
     *
     * @deprecated Use {@link HasOptionsStyleProvider#setOptionStyleProvider(Function)} instead.
     */
    @FunctionalInterface
    @Deprecated
    interface OptionsStyleProvider {
        String getItemStyleName(Component component, Object item);
    }
}