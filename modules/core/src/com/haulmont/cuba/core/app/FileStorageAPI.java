/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.10.2009 14:13:25
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.Date;

public interface FileStorageAPI {

    String NAME = "cuba_FileStorage";
    
    void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException;

    void removeFile(FileDescriptor fileDescr) throws FileStorageException;    

    InputStream openFileInputStream(FileDescriptor fileDescr) throws FileStorageException;

    OutputStream openFileOutputStream(FileDescriptor fileDescr) throws FileStorageException;

    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Relocate existing file to storage
     *
     * @param fileDescr
     * @param file
     */
    void putFile(FileDescriptor fileDescr, File file) throws FileStorageException;
}
