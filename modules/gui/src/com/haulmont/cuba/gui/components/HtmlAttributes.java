/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

/**
 * Sets DOM and CSS attributes to UI component widgets.
 *
 * @see CSS
 * @see DOM
 */
public interface HtmlAttributes {
    String NAME = "cuba_HtmlAttributes";

    /**
     * Sets DOM attribute on the top most element of UI component.
     *
     * @param component UI component
     * @param attributeName DOM attribute name, e.g. "title"
     * @param value attribute value
     * @see DOM
     */
    void setDomAttribute(Component component, String attributeName, String value);
    /**
     * Gets DOM attribute value assigned using {@link HtmlAttributes}. Does not reflect a real value from DOM.
     *
     * @param component UI component
     * @param attributeName DOM attribute name
     * @return previously assigned DOM attribute value
     */
    String getDomAttribute(Component component, String attributeName);

    /**
     * Removes DOM attribute from the top most element of UI component.
     *
     * @param component UI component
     * @param attributeName DOM attribute name
     */
    void removeDomAttribute(Component component, String attributeName);

    /**
     * Sets CSS property value on the top most element of UI component.
     *
     * @param component UI component
     * @param propertyName CSS property name, e.g. "border-color"
     * @param value property value
     * @see CSS
     */
    void setCssProperty(Component component, String propertyName, String value);
    /**
     * Gets CSS property value assigned using {@link HtmlAttributes}. Does not reflect a real value from DOM.
     *
     * @param component UI component
     * @param propertyName CSS property name
     * @return previously assigned CSS property value
     */
    String getCssProperty(Component component, String propertyName);

    /**
     * Clears CSS property value from the top most element of UI component.
     *
     * @param component UI component
     * @param propertyName CSS property name
     */
    void removeCssProperty(Component component, String propertyName);

    /**
     * Common CSS property names.
     */
    final class CSS {
        private CSS() {
        }

        public static final String ANIMATION = "animation";
        public static final String ANIMATION_NAME = "animation-name";
        public static final String ANIMATION_DURATION = "animation-duration";
        public static final String ANIMATION_DELAY = "animation-delay";
        public static final String ANIMATION_DIRECTION = "animation-direction";
        public static final String ANIMATION_TIMING_FUNCTION = "animation-timing-function";
        public static final String ANIMATION_ITERATION_COUNT = "animation-iteration-count";

        public static final String TRANSFORM = "transform";
        public static final String TRANSITION = "transition";
        public static final String TRANSITION_DELAY = "transition-delay";
        public static final String TRANSITION_PROPERTY = "transition-property";
        public static final String TRANSITION_DURATION = "transition-duration";
        public static final String TRANSITION_TIMING_FUNCTION = "transition-timing-function";

        public static final String BACKGROUND = "background";
        public static final String BACKGROUND_ATTACHMENT = "background-attachment";
        public static final String BACKGROUND_COLOR = "background-color";
        public static final String BACKGROUND_IMAGE = "background-image";
        public static final String BACKGROUND_POSITION = "background-position";
        public static final String BACKGROUND_REPEAT = "background-repeat";

        public static final String BORDER = "border";
        public static final String BORDER_COLOR = "border-color";
        public static final String BORDER_STYLE = "border-style";
        public static final String BORDER_RADIUS = "border-radius";

        public static final String BORDER_BOTTOM = "border-bottom";
        public static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
        public static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
        public static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";

        public static final String BORDER_LEFT = "border-left";
        public static final String BORDER_LEFT_COLOR = "border-left-color";
        public static final String BORDER_LEFT_STYLE = "border-left-style";
        public static final String BORDER_LEFT_WIDTH = "border-left-width";

        public static final String BORDER_RIGHT = "border-right";
        public static final String BORDER_RIGHT_COLOR = "border-right-color";
        public static final String BORDER_RIGHT_STYLE = "border-right-style";
        public static final String BORDER_RIGHT_WIDTH = "border-right-width";

        public static final String BORDER_TOP = "border-top";
        public static final String BORDER_TOP_COLOR = "border-top-color";
        public static final String BORDER_TOP_STYLE = "border-top-style";
        public static final String BORDER_TOP_WIDTH = "border-top-width";

        public static final String CURSOR = "cursor";
        public static final String CLEAR = "clear";
        public static final String DISPLAY = "display";
        public static final String FLOAT = "float";
        public static final String POSITION = "position";
        public static final String VISIBILITY = "visibility";

        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String LINE_HEIGHT = "line-height";

        public static final String MAX_HEIGHT = "max-height";
        public static final String MAX_WIDTH = "max-width";
        public static final String MIN_HEIGHT = "min-height";
        public static final String MIN_WIDTH = "min-width";

        public static final String FONT = "font";
        public static final String FONT_FAMILY = "font-family";
        public static final String FONT_SIZE = "font-size";
        public static final String FONT_SIZE_ADJUST = "font-size-adjust";
        public static final String FONT_STRETCH = "font-stretch";
        public static final String FONT_STYLE = "font-style";
        public static final String FONT_VARIANT = "font-variant";
        public static final String FONT_WEIGHT = "font-weight";

        public static final String CONTENT = "content";
        public static final String COUNTER_INCREMENT = "counter-increment";
        public static final String COUNTER_RESET = "counter-reset";
        public static final String QUOTES = "quotes";

        public static final String LIST_STYLE = "list-style";
        public static final String LIST_STYLE_IMAGE = "list-style-image";
        public static final String LIST_STYLE_POSITION = "list-style-position";
        public static final String LIST_STYLE_TYPE = "list-style-type";
        public static final String MARKER_OFFSET = "marker-offset";

        public static final String MARGIN = "margin";
        public static final String MARGIN_BOTTOM = "margin-bottom";
        public static final String MARGIN_LEFT = "margin-left";
        public static final String MARGIN_RIGHT = "margin-right";
        public static final String MARGIN_TOP = "margin-top";

        public static final String OUTLINE = "outline";
        public static final String OUTLINE_COLOR = "outline-color";
        public static final String OUTLINE_STYLE = "outline-style";
        public static final String OUTLINE_WIDTH = "outline-width";

        public static final String PADDING = "padding";
        public static final String PADDING_BOTTOM = "padding-bottom";
        public static final String PADDING_LEFT = "padding-left";
        public static final String PADDING_RIGHT = "padding-right";
        public static final String PADDING_TOP = "padding-top";

        public static final String BOTTOM = "bottom";
        public static final String LEFT = "left";
        public static final String TOP = "top";
        public static final String RIGHT = "right";

        public static final String BOX_SHADOW = "box-shadow";

        public static final String CLIP = "clip";
        public static final String OVERFLOW = "overflow";
        public static final String VERTICAL_ALIGN = "vertical-align";
        public static final String Z_INDEX = "z-index";
        public static final String ORDER = "order";

        public static final String BORDER_COLLAPSE = "border-collapse";
        public static final String BORDER_SPACING = "border-spacing";

        public static final String COLOR = "color";
        public static final String DIRECTION = "direction";
        public static final String LETTER_SPACING = "letter-spacing";
        public static final String TEXT_ALIGN = "text-align";
        public static final String TEXT_DECORATION = "text-decoration";
        public static final String TEXT_INDENT = "text-indent";
        public static final String TEXT_SHADOW = "text-shadow";
        public static final String TEXT_TRANSFORM = "text-transform";
        public static final String WHITE_SPACE = "white-space";
        public static final String WORD_SPACING = "word-spacing";

        public static final String FLEX = "flex";
        public static final String FLEX_GROW = "flex-grow";
        public static final String FLEX_SHRINK = "flex-shrink";
        public static final String FLEX_BASIS = "flex-basis";
        public static final String FLEX_DIRECTION = "flex-direction";
        public static final String FLEX_WRAP = "flex-wrap";
        public static final String FLEX_FLOW = "flex-flow";

        public static final String JUSTIFY_SELF = "justify-self";
        public static final String JUSTIFY_CONTENT = "justify-content";
        public static final String JUSTIFY_ITEMS = "justify-items";
        public static final String PLACE_SELF = "place-self";
        public static final String PLACE_ITEMS = "place-items";
        public static final String PLACE_CONTENT = "place-content";
        public static final String ALIGN_SELF = "align-self";
        public static final String ALIGN_ITEMS = "align-items";
        public static final String ALIGN_CONTENT = "align-content";

        public static final String GRID = "grid";
        public static final String GRID_TEMPLATE_ROWS = "grid-template-rows";
        public static final String GRID_TEMPLATE_COLUMNS = "grid-template-columns";
        public static final String GRID_TEMPLATE_AREAS = "grid-template-areas";
        public static final String GRID_AUTO_ROWS = "grid-auto-rows";
        public static final String GRID_AUTO_COLUMNS = "grid-auto-columns";
        public static final String GRID_AUTO_FLOW = "grid-auto-flow";
        public static final String GRID_AREA = "grid-area";
        public static final String GRID_GAP = "grid-gap";
        public static final String GRID_ROW_GAP = "grid-row-gap";
        public static final String GRID_COLUMN_GAP = "grid-column-gap";
        public static final String GRID_COLUMN_START = "grid-column-start";
        public static final String GRID_COLUMN_END = "grid-column-end";
        public static final String GRID_ROW_START = "grid-row-start";
        public static final String GRID_ROW_END = "grid-row-end";
        public static final String GRID_COLUMN = "grid-column";
        public static final String GRID_ROW = "grid-row";
    }

    /**
     * Common DOM attribute names.
     */
    final class DOM {
        private DOM() {
        }

        public static final String TYPE = "type";
        public static final String VALUE = "value";
        public static final String ID = "id";
        public static final String CLASS = "class";
        public static final String HREF = "href";
        public static final String ALT = "alt";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String STYLE = "style";
    }
}