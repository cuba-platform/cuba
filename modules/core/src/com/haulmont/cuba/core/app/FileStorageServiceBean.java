/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(FileStorageService.NAME)
public class FileStorageServiceBean implements FileStorageService {

    @Inject
    FileStorageAPI fileStorageAPI;

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