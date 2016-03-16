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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

/**
 * Provides simple means to work with the file storage from the client tier.
 * <p/>
 * Warning: files content is passed in byte arrays, which is acceptable only for relatively small files. Preferred
 * way to work with file storage is through {@code FileUploadingAPI} and {@code FileDataProvider}.
 *
 */
public interface FileStorageService {

    String NAME = "cuba_FileStorageService";

    void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException;

    void removeFile(FileDescriptor fileDescr) throws FileStorageException;

    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;

    boolean fileExists(FileDescriptor fileDescr);
}
