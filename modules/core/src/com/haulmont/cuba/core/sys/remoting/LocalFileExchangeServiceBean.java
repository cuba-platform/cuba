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
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityAccessException;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.InputStream;

@Service(LocalFileExchangeService.NAME)
public class LocalFileExchangeServiceBean implements LocalFileExchangeService {
    @Inject
    protected FileStorageAPI fileStorage;
    @Inject
    protected DataManager dataManager;

    @Override
    public void uploadFile(InputStream inputStream, FileDescriptor fileDescriptor) throws FileStorageException {
        fileStorage.saveStream(fileDescriptor, inputStream);
    }

    @Override
    public InputStream downloadFile(FileDescriptor fileDescriptor) throws FileStorageException {
        FileDescriptor descriptor;
        try {
            // FileDescriptor must be available for the current user and be non deleted
            descriptor = dataManager.secure().reload(fileDescriptor, View.LOCAL);
        } catch (EntityAccessException e) {
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescriptor.getName(), e);
        }

        return fileStorage.openStream(descriptor);
    }
}