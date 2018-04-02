/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets.renderers;

import com.haulmont.cuba.web.widgets.EnhancedUI;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.v7.ui.renderers.ClickableRenderer;
import elemental.json.JsonValue;

import java.net.MalformedURLException;
import java.net.URL;

public class CubaImageRenderer extends ClickableRenderer<String> {

    public CubaImageRenderer() {
        super(String.class, null);
    }

    /**
     * Creates a new image renderer and adds the given click listener to it.
     *
     * @param listener the click listener to register
     */
    public CubaImageRenderer(RendererClickListener listener) {
        this();
        addClickListener(listener);
    }

    @Override
    public JsonValue encode(String value) {
        Resource resource = null;
        if (value != null) {
            if (value.startsWith("file:") || value.startsWith("jar:") || value.contains("icon:")) {
                throw new IllegalArgumentException("ImageRenderer only supports ExternalResource and ThemeResource");
            }

            if (value.startsWith("http") || value.startsWith("https")) {
                try {
                    resource = new ExternalResource(new URL(value));
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Unable to parse url for value", e);
                }
            } else {
                if (value.startsWith("theme://")) {
                    value = value.substring("theme://".length());
                }
                resource = ((EnhancedUI) getUI()).createVersionedResource(value);
            }
        }

        return encode(ResourceReference.create(resource, this, null), URLReference.class);
    }
}