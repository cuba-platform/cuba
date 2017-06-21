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

package com.haulmont.cuba.web.gui.components.imageresources;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.web.gui.components.WebImage;
import com.vaadin.server.StreamResource;
import org.apache.commons.lang.RandomStringUtils;

import java.io.InputStream;
import java.util.function.Supplier;

public class WebStreamImageResource extends WebImage.WebAbstractImageResource implements WebImageResource, Image.StreamImageResource {

    protected Supplier<InputStream> streamSupplier;

    @Override
    public Image.StreamImageResource setStreamSupplier(Supplier<InputStream> streamSupplier) {
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
        resource = new StreamResource(() ->
                streamSupplier.get(), RandomStringUtils.random(16, true, true));
    }
}
