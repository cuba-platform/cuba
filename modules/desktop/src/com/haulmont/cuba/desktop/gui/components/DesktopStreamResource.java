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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.StreamResource;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public class DesktopStreamResource extends DesktopAbstractStreamSettingsResource
        implements DesktopResource, StreamResource {

    protected Supplier<InputStream> streamSupplier;
    // just stub
    protected String mimeType;

    @Override
    public StreamResource setStreamSupplier(Supplier<InputStream> streamSupplier) {
        Preconditions.checkNotNullArgument(streamSupplier);

        this.streamSupplier = streamSupplier;
        hasSource = true;

        fireResourceUpdateEvent();

        return this;
    }

    @Override
    public Supplier<InputStream> getStreamSupplier() {
        return streamSupplier;
    }

    @Override
    protected void createResource() {
        try {
            resource = ImageIO.read(streamSupplier.get());
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading an image.", e);
        }
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }
}
