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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.gui.components.ClasspathResource;
import com.vaadin.server.StreamResource;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class WebClasspathResource extends WebAbstractStreamSettingsResource implements WebResource, ClasspathResource {

    protected String path;

    protected String mimeType;

    @Override
    public ClasspathResource setPath(String path) {
        Preconditions.checkNotNullArgument(path);

        this.path = path;
        hasSource = true;

        fireResourceUpdateEvent();

        return this;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    protected void createResource() {
        StringBuilder name = new StringBuilder();

        if (StringUtils.isNotEmpty(fileName)) {
            name.append(fileName)
                    .append('-');
        } else {
            String nameByPath = FilenameUtils.getName(path);
            if (StringUtils.isNotEmpty(nameByPath)) {
                name.append(nameByPath)
                        .append('-');
            }
        }
        name.append(UUID.randomUUID().toString());

        resource = new StreamResource(() ->
                AppBeans.get(Resources.class).getResourceAsStream(path), name.toString());

        StreamResource streamResource = (StreamResource) this.resource;

        streamResource.setMIMEType(mimeType);
        streamResource.setCacheTime(cacheTime);
        streamResource.setBufferSize(bufferSize);
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;

        if (resource != null) {
            ((StreamResource) resource).setMIMEType(mimeType);
        }
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }
}