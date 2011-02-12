/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 04.02.11 15:26
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface FileUploadingApi {
    String NAME = "cuba_FileUploading";

    public UUID saveFile(byte[] data) throws FileStorageException;

    public UUID saveFile(InputStream stream, FileUploadService.UploadProgressListener listener)
            throws FileStorageException ;

    public UUID createEmptyFile() throws FileStorageException;

    /**
     * Get new File() in temp directory without create them
     * @return
     * @throws FileStorageException
     */
    public UUID getNewDescriptor() throws FileStorageException;

    public File getFile(UUID fileId) ;

    public FileDescriptor getFileDescriptor(UUID fileId, String name);

    public InputStream loadFile(UUID fileId) throws FileNotFoundException;

    public void deleteFile(UUID fileId) throws FileStorageException;

    public void deleteFileLink(String fileName);
}