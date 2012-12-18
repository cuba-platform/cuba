/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.upload;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface FileUploadingAPI {

    String NAME = "cuba_FileUploading";

    interface UploadProgressListener {
        void progressChanged(UUID fileId, int receivedBytes);
    }

    interface UploadToStorageProgressListener {
        /**
         * @param fileId        file id
         * @param uploadedBytes current received byte count
         * @param totalBytes    total content length
         */
        void progressChanged(UUID fileId, long uploadedBytes, long totalBytes);
    }

    UUID saveFile(byte[] data) throws FileStorageException;

    UUID saveFile(InputStream stream, UploadProgressListener listener) throws FileStorageException;

    UUID createEmptyFile() throws FileStorageException;

    /**
     * Get new File() in temp directory without create them
     *
     * @return
     * @throws FileStorageException
     */
    UUID getNewDescriptor() throws FileStorageException;

    File getFile(UUID fileId);

    FileDescriptor getFileDescriptor(UUID fileId, String name);

    InputStream loadFile(UUID fileId) throws FileNotFoundException;

    void deleteFile(UUID fileId) throws FileStorageException;

    void deleteFileLink(String fileName);

    void putFileIntoStorage(UUID fileId, FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Upload file to middleware from {@link com.haulmont.cuba.gui.executors.BackgroundTask} <br/>
     * Use in {@link com.haulmont.cuba.gui.executors.BackgroundTask#run(com.haulmont.cuba.gui.executors.TaskLifeCycle)}
     * @param taskLifeCycle task life cycle with specified params: fileId and fileName
     * @return file descriptor
     * @throws FileStorageException
     */
    FileDescriptor putFileIntoStorage(TaskLifeCycle<Long> taskLifeCycle) throws FileStorageException;
}