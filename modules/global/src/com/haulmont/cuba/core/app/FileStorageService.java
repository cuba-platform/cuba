/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

public interface FileStorageService {

    String NAME = "cuba_FileStorageService";

    void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException;

    void removeFile(FileDescriptor fileDescr) throws FileStorageException;

    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;
}
