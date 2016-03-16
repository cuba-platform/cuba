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

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.InputStream;

/**
 */
@Service(LocalFileExchangeService.NAME)
public class LocalFileExchangeServiceBean implements LocalFileExchangeService {
    @Inject
    protected FileStorageAPI fileStorage;

    @Override
    public void uploadFile(InputStream inputStream, FileDescriptor fileDescriptor) {
        try {
            fileStorage.saveStream(fileDescriptor, inputStream);
        } catch (FileStorageException e) {
            throw new RuntimeException("An error occurred while saving file", e);
        }
    }

    @Override
    public InputStream downloadFile(FileDescriptor fileDescriptor) {
        try {
            InputStream inputStream = fileStorage.openStream(fileDescriptor);
            return inputStream;
        } catch (FileStorageException e) {
            throw new RuntimeException("An error occurred while loading file", e);
        }
    }
}
