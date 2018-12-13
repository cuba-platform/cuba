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

public interface ScrollBoxLayout
        extends OrderedContainer, Component.BelongToFrame, HasMargin, HasSpacing, HasOrientation,
                Component.HasIcon, Component.HasCaption, ShortcutNotifier, HasContextHelp,
                HasHtmlCaption, HasHtmlDescription {

    String NAME = "scrollBox";

    ScrollBarPolicy getScrollBarPolicy();
    void setScrollBarPolicy(ScrollBarPolicy scrollBarPolicy);

    /**
     * Sets content width.
     *
     * @param width width
     */
    void setContentWidth(String width);
    /**
     * @return content width value
     */
    float getContentWidth();
    /**
     * @return content width size unit
     */
    SizeUnit getContentWidthSizeUnit();

    /**
     * Sets content height.
     *
     * @param height height
     */
    void setContentHeight(String height);
    /**
     * @return content height value
     */
    float getContentHeight();
    /**
     * @return content height size unit
     */
    SizeUnit getContentHeightSizeUnit();

    /**
     * Sets minimum CSS width for content. Examples: "640px", "auto".
     *
     * @param minWidth minimum width
     */
    void setContentMinWidth(String minWidth);
    /**
     * @return minimal content width
     */
    String getContentMinWidth();

    /**
     * Sets maximum CSS width for content. Examples: "640px", "100%".
     *
     * @param maxWidth maximum width
     */
    void setContentMaxWidth(String maxWidth);
    /**
     * @return maximum content width
     */
    String getContentMaxWidth();

    /**
     * Sets minimum CSS height for content. Examples: "640px", "auto".
     *
     * @param minHeight minimum height
     */
    void setContentMinHeight(String minHeight);
    /**
     * @return minimum content width
     */
    String getContentMinHeight();

    /**
     * Sets maximum CSS height for content. Examples: "640px", "100%".
     *
     * @param maxHeight maximum height
     */
    void setContentMaxHeight(String maxHeight);
    /**
     * @return maximum content width
     */
    String getContentMaxHeight();

    enum ScrollBarPolicy {
        VERTICAL,
        HORIZONTAL,
        BOTH,
        NONE
    }
}