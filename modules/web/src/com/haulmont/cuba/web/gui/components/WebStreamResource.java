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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.StreamResource;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.UUID;
import java.util.function.Supplier;

public class WebStreamResource extends WebAbstractStreamSettingsResource implements WebResource, StreamResource {

    protected Supplier<InputStream> streamSupplier;

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
        StringBuilder name = new StringBuilder();

        if (StringUtils.isNotEmpty(fileName)) {
            name.append(fileName)
                    .append('-');
        }
        name.append(UUID.randomUUID().toString());

        resource = new com.vaadin.server.StreamResource(() ->
                streamSupplier.get(), name.toString());

        com.vaadin.server.StreamResource vStreamResource = (com.vaadin.server.StreamResource) this.resource;

        vStreamResource.setCacheTime(cacheTime);
        vStreamResource.setBufferSize(bufferSize);
        vStreamResource.setMIMEType(mimeType);
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;

        if (resource != null) {
            ((com.vaadin.server.StreamResource) resource).setMIMEType(mimeType);
        }
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }
}
