/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 17:34:49
 *
 * $Id$
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
