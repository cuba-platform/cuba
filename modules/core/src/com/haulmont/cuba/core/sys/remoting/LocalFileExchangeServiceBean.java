/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.app.FileStorage;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.InputStream;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Service(LocalFileExchangeService.NAME)
public class LocalFileExchangeServiceBean implements LocalFileExchangeService {
    @Inject
    protected FileStorage fileStorage;

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
