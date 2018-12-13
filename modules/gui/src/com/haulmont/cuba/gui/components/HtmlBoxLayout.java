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

public interface HtmlBoxLayout extends ComponentContainer, Component.BelongToFrame, Component.HasIcon,
        Component.HasCaption, HasContextHelp, HasHtmlCaption, HasHtmlDescription {

    String NAME = "htmlBox";

    /**
     * Return filename of the related HTML template.
     */
    String getTemplateName();

    /**
     * Set filename of the related HTML template inside theme/layouts directory.
     */
    void setTemplateName(String templateName);

    /**
     * @return the contents of the template
     */
    String getTemplateContents();

    /**
     * Set the contents of the template used to draw the custom layout.
     */
    void setTemplateContents(String templateContents);
}