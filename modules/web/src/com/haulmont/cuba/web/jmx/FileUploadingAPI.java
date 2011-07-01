/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 04.02.11 15:26
 *
 * $Id$
 */
package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public interface FileUploadingAPI {

    String NAME = "cuba_web_FileUploading";

    interface UploadProgressListener {
        void progressChanged(UUID fileId, int receivedBytes);
    }

    UUID saveFile(byte[] data) throws FileStorageException;

    UUID saveFile(InputStream stream, UploadProgressListener listener) throws FileStorageException;

    UUID createEmptyFile() throws FileStorageException;

    /**
     * Get new File() in temp directory without create them
     * @return
     * @throws FileStorageException
     */
    UUID getNewDescriptor() throws FileStorageException;

    File getFile(UUID fileId) ;

    FileDescriptor getFileDescriptor(UUID fileId, String name);

    InputStream loadFile(UUID fileId) throws FileNotFoundException;

    void deleteFile(UUID fileId) throws FileStorageException;

    void deleteFileLink(String fileName);

    void putFileIntoStorage(UUID fileId, FileDescriptor fileDescr) throws FileStorageException;
}