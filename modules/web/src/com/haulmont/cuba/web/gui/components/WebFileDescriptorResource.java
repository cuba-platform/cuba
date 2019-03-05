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
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.FileDescriptorResource;
import com.vaadin.server.StreamResource;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class WebFileDescriptorResource extends WebAbstractStreamSettingsResource
        implements WebResource, FileDescriptorResource {

    protected static final String FILE_STORAGE_EXCEPTION_MESSAGE = "Can't create FileDescriptorResource. " +
            "An error occurred while obtaining a file from the storage";

    protected FileDescriptor fileDescriptor;

    protected String mimeType;

    @Override
    public FileDescriptorResource setFileDescriptor(FileDescriptor fileDescriptor) {
        Preconditions.checkNotNullArgument(fileDescriptor);

        this.fileDescriptor = fileDescriptor;
        hasSource = true;

        fireResourceUpdateEvent();

        return this;
    }

    @Override
    public FileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    @Override
    protected void createResource() {
        StringBuilder name = new StringBuilder();

        if (StringUtils.isNotEmpty(fileName)) {
            name.append(fileName)
                    .append('-');
        } else if (StringUtils.isNotEmpty(fileDescriptor.getName())) {
            name.append(fileDescriptor.getName())
                    .append('-');
        }

        name.append(UUID.randomUUID().toString());

        resource = new StreamResource(() -> {
            try {
                return AppBeans.get(FileLoader.class).openStream(fileDescriptor);
            } catch (FileStorageException e) {
                throw new RuntimeException(FILE_STORAGE_EXCEPTION_MESSAGE, e);
            }
        }, name.toString());

        StreamResource streamResource = (StreamResource) this.resource;

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
