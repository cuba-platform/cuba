/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
 */
public interface FileStorageService {

    String NAME = "cuba_FileStorageService";

    void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException;

    void removeFile(FileDescriptor fileDescr) throws FileStorageException;

    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;

    boolean fileExists(FileDescriptor fileDescr);
}
