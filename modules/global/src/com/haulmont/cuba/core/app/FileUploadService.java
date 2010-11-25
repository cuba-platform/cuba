/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 18.11.2010 14:47:24
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Service for temporary file storage
 */
public interface FileUploadService {
    String NAME = "cuba_FileUploadService";

    public interface UploadProgressListener {
        void progressChanged(UUID fileId, int receivedBytes);
    }

    /**
     * Save file to temp dir
     *
     * @param data File content
     * @return Id of file
     */
    UUID saveFile(byte[] data) throws FileStorageException;

    /**
     * Save file from stream to temp dir
     *
     * @param stream Input stream
     * @return Id of file
     */
    UUID saveFile(InputStream stream, UploadProgressListener listener)
            throws FileStorageException;

    /**
     * Get file by Id
     *
     * @param fileId File identity
     * @return File
     */
    File getFile(UUID fileId);

    /**
     * Get FileDescriptor by id
     * @param fileId Id of temporary file
     * @return FileDescriptor
     */
    FileDescriptor getFileDescriptor(UUID fileId, String name);

    /**
     * Get input stream for file
     *
     * @param fileId File identity
     * @return Input stream
     */
    InputStream loadFile(UUID fileId) throws FileNotFoundException;

    /**
     * Delete temp file
     *
     * @param fileId File identity
     */
    void deleteFile(UUID fileId) throws FileStorageException;
}
