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
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.FileDescriptorResource;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class DesktopFileDescriptorResource extends DesktopAbstractStreamSettingsResource
        implements DesktopResource, FileDescriptorResource {

    protected static final String FILE_STORAGE_EXCEPTION_MESSAGE = "Can't create FileDescriptorResource. " +
            "An error occurred while obtaining a file from the storage";

    protected FileDescriptor fileDescriptor;

    // just stub
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
        try {
            InputStream stream = AppBeans.get(FileLoader.class)
                    .openStream(fileDescriptor);

            resource = ImageIO.read(stream);
        } catch (FileStorageException e) {
            throw new RuntimeException(FILE_STORAGE_EXCEPTION_MESSAGE, e);
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
