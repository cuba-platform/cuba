/*
 * Copyright 2017 Nikita Petunin, Yuriy Artamonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;

/**
 * Drag caption definition: icon and caption.
 */
public class DragCaption {
    protected String caption;
    protected Resource icon;
    protected ContentMode contentMode = ContentMode.TEXT;

    public DragCaption(String caption, Resource icon) {
        this.caption = caption;
        this.icon = icon;
    }

    public DragCaption(String caption, Resource icon, ContentMode contentMode) {
        this.caption = caption;
        this.icon = icon;
        this.contentMode = contentMode;
    }

    public Resource getIcon() {
        return icon;
    }

    public String getCaption() {
        return caption;
    }

    public void setIcon(Resource icon) {
        this.icon = icon;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ContentMode getContentMode() {
        return contentMode;
    }
}