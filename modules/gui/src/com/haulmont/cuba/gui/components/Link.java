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

/**
 * HTML link component
 *
 */
public interface Link extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon,
        HasHtmlCaption, HasHtmlDescription {

    String NAME = "link";

    void setUrl(String url);
    String getUrl();

    void setTarget(String target);
    String getTarget();

    /**
     * Sets the relation between current document and the target document.
     * Default value is "noopener noreferrer".
     *
     * @param rel string value of relation (e.g. noreferrer)
     */
    void setRel(String rel);

    /**
     * @return string value of relation
     */
    String getRel();
}