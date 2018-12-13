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

public interface HasHtmlDescription extends Component.HasDescription {

    /**
     * @return {@code true} if the description is rendered as HTML,
     * {@code false} if rendered as plain text
     */
    boolean isDescriptionAsHtml();

    /**
     * Sets whether the description is rendered as HTML.
     *
     * @param descriptionAsHtml {@code true} if the description is rendered as HTML,
     *                          {@code false} if rendered as plain text
     * @see #setDescription(String)
     */
    void setDescriptionAsHtml(boolean descriptionAsHtml);
}
