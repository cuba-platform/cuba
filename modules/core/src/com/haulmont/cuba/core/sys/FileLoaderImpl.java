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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component(FileLoader.NAME)
public class FileLoaderImpl implements FileLoader {
    @Inject
    protected FileStorageAPI fileStorageAPI;

    @Override
    public void saveStream(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier) throws FileStorageException {
        fileStorageAPI.saveStream(fd, inputStreamSupplier.get());
    }

    @Override
    public void saveStream(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier,
                           @Nullable Consumer<StreamingProgressEvent> streamingListener)
            throws FileStorageException, InterruptedException {
        fileStorageAPI.saveStream(fd, inputStreamSupplier.get());
    }

    @Override
    public InputStream openStream(FileDescriptor fd) throws FileStorageException {
        return fileStorageAPI.openStream(fd);
    }

    @Override
    public void removeFile(FileDescriptor fd) throws FileStorageException {
        fileStorageAPI.removeFile(fd);
    }

    @Override
    public boolean fileExists(FileDescriptor fd) throws FileStorageException {
        return fileStorageAPI.fileExists(fd);
    }
}