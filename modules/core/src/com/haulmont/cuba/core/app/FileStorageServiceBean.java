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
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 */
@Service(FileStorageService.NAME)
public class FileStorageServiceBean implements FileStorageService {

    @Inject
    protected FileStorageAPI fileStorageAPI;

    @Override
    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        fileStorageAPI.saveFile(fileDescr, data);
    }

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        fileStorageAPI.removeFile(fileDescr);
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        return fileStorageAPI.loadFile(fileDescr);
    }

    @Override
    public boolean fileExists(FileDescriptor fileDescr) {
        return fileStorageAPI.fileExists(fileDescr);
    }
}