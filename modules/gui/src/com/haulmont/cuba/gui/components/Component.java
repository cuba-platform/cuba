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

import com.haulmont.cuba.gui.icons.Icons;
import org.dom4j.Element;

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

    // vaadin8 JavaDoc for deprecated
    @Deprecated
    int UNITS_PIXELS = 0;
    @Deprecated
    int UNITS_PERCENTAGE = 8;

    String AUTO_SIZE = "-1px";
    int AUTO_SIZE_PX = -1;

    /** Component ID as defined in {@code id} attribute */
    String getId();
    /** Set component ID */
    void setId(String id);

    /**
     * @return parent of component.
     */
    Component getParent();
    /**
     * INTERNAL.<br>
     *
     * {@link ComponentContainer#add(Component)} is normally used for adding components
     * to a parent and the used method will call this method implicitly.
     *
     * @param parent Parent component
     */
    void setParent(Component parent);

    String getDebugId();
    /** INTERNAL. Managed by debug Id system. */
    void setDebugId(String id);

    /**
     * Is the component enabled?
     * vaadin8 add JavaDoc
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
     * Is the component visible?
     * vaadin8 add JavaDoc
     */
    boolean isVisible();
    /** Set component visibility */
    void setVisible(boolean visible);

    /**
     * @return
     */
    boolean isVisibleRecursive();

    /**
     * @return
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

    /** Set focus to this component */
    // vaadin8 move to Focusable
    void requestFocus();

    /** Get component height in {@link #getHeightUnits()} */
    float getHeight();

    /** Height units: {@link #UNITS_PIXELS}, {@link #UNITS_PERCENTAGE} */
    @Deprecated
    int getHeightUnits();

    /**
     * Gets the height property units.
     *
     * @return units used in height property.
     */
    SizeUnit getHeightSizeUnit();

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
    @Deprecated
    int getWidthUnits();

    /**
     * Gets the width property units.
     *
     * @return units used in the width property.
     */
    SizeUnit getWidthSizeUnit();

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
     * todo
     */
    interface HasDescription {
        String getDescription();
        void setDescription(String description);
    }

    /**
     * Object having a caption
     */
    interface HasCaption extends HasDescription {
        String getCaption();
        void setCaption(String caption);
    }

    /**
     * Object having an XML descriptor attached
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
        String getIcon();

        /**
         * Set an icon by its source: "font-icon:ADD", "icons/myicon.png", "theme://createIcon", etc.
         */
        void setIcon(String icon);

        /**
         * Set an icon from an icon set.
         */
        void setIconFromSet(Icons.Icon icon);
    }
}