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

import com.vaadin.v7.ui.renderers.ClickableRenderer;

public class CubaClickableTextRenderer extends ClickableRenderer<String> {

    /**
     * Creates a new clickable text renderer.
     *
     * @param nullRepresentation the textual representation of {@code null} value
     */
    public CubaClickableTextRenderer(String nullRepresentation) {
        super(String.class, nullRepresentation);
    }

    /**
     * Creates a new clickable text renderer and adds the given click listener to it.
     *
     * @param listener           the click listener to register
     * @param nullRepresentation the textual representation of {@code null} value
     */
    public CubaClickableTextRenderer(RendererClickListener listener, String nullRepresentation) {
        this(nullRepresentation);
        addClickListener(listener);
    }

    /**
     * Creates a new clickable text renderer.
     */
    public CubaClickableTextRenderer() {
        this("");
    }

    /**
     * Creates a new clickable text renderer and adds the given click listener to it.
     *
     * @param listener the click listener to register
     */
    public CubaClickableTextRenderer(RendererClickListener listener) {
        this(listener, "");
    }
}