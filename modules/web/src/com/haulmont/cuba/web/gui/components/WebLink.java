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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Link;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import org.apache.commons.lang.StringUtils;

public class WebLink extends WebAbstractComponent<com.vaadin.ui.Link> implements Link {

    public WebLink() {
        component = new com.vaadin.ui.Link();
        component.setDescription(null);
    }

    @Override
    public void setUrl(String url) {
        component.setResource(new ExternalResource(url));
    }

    @Override
    public String getUrl() {
        Resource resource = component.getResource();
        if (resource instanceof ExternalResource)
            return ((ExternalResource) resource).getURL();

        return null;
    }

    @Override
    public void setTarget(String target) {
        component.setTargetName(target);
    }

    @Override
    public String getTarget() {
        return component.getTargetName();
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }
}