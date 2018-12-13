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

public interface HasHtmlCaption extends Component.HasCaption {
    /**
     * @return {@code true} if the caption is rendered as HTML,
     * {@code false} if rendered as plain text
     */
    boolean isCaptionAsHtml();

    /**
     * Sets whether the caption is rendered as HTML.
     *
     * @param captionAsHtml {@code true} if the caption is rendered as HTML,
     *                      {@code false} if rendered as plain text
     * @see #setCaption(String)
     */
    void setCaptionAsHtml(boolean captionAsHtml);
}
