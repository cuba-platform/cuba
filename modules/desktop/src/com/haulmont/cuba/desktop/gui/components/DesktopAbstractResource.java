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

package com.haulmont.cuba.desktop.gui.components;

import java.awt.image.BufferedImage;

public abstract class DesktopAbstractResource implements DesktopResource {

    protected BufferedImage resource;
    protected Runnable resourceUpdateHandler;

    protected boolean hasSource;

    @Override
    public BufferedImage getResource() {
        if (resource == null) {
            createResource();
        }
        return resource;
    }

    protected boolean hasSource() {
        return hasSource;
    }

    protected void fireResourceUpdateEvent() {
        resource = null;

        if (resourceUpdateHandler != null) {
            resourceUpdateHandler.run();
        }
    }

    protected void setResourceUpdatedHandler(Runnable resourceUpdated) {
        this.resourceUpdateHandler = resourceUpdated;
    }

    protected abstract void createResource();
}
