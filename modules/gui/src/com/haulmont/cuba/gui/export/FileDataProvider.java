/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.RuntimeFileStorageException;

import java.io.InputStream;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Data provider for FileDescriptor
 */
public class FileDataProvider implements ExportDataProvider {

    protected FileDescriptor fileDescriptor;
    protected FileLoader fileLoader = AppBeans.get(FileLoader.NAME);

    public FileDataProvider(FileDescriptor fileDescriptor) {
        checkNotNullArgument(fileDescriptor, "Null file descriptor");

        this.fileDescriptor = fileDescriptor;
    }

    @Override
    public InputStream provide() {
        try {
            return fileLoader.openStream(fileDescriptor);
        } catch (FileStorageException e) {
            throw new RuntimeFileStorageException(e);
        }
    }
}