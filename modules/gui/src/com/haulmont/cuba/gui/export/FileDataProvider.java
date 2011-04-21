/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.ServiceLocator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Data provider for FileDescriptor
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class FileDataProvider implements ExportDataProvider {

    private FileDescriptor fileDescriptor;
    private InputStream inputStream;

    public FileDataProvider(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public InputStream provide() {
        FileStorageService storageService = ServiceLocator.lookup(FileStorageService.NAME);
        try {
            inputStream = new ByteArrayInputStream(storageService.loadFile(fileDescriptor));
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputStream = null;
        } else
            throw new RuntimeException("DataProvider is closed");
    }
}
