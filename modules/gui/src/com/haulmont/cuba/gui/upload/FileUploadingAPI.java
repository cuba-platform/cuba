/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.upload;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.UUID;

/**
 * Client API for uploading files and transfer them to the middleware FileStorage.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface FileUploadingAPI {

    String NAME = "cuba_FileUploading";

    /**
     * Listener to be notified about the progress of uploading file into the temporary storage.
     */
    interface UploadProgressListener {
        /**
         * @param fileId        temporary file ID
         * @param receivedBytes current uploaded bytes count
         */
        void progressChanged(UUID fileId, int receivedBytes);
    }

    /**
     * Listener to be notified about the progress of uploading file from the temporary storage
     * into middleware FileStorage.
     */
    interface UploadToStorageProgressListener {
        /**
         * @param fileId        temporary file ID
         * @param uploadedBytes current uploaded bytes count
         * @param totalBytes    total contents size
         */
        void progressChanged(UUID fileId, long uploadedBytes, long totalBytes);
    }

    /**
     * Store the byte array in a new temporary file.
     *
     * @param data  file contents
     * @return      temporary file ID. This ID is cached in memory and can be used for subsequent operations.
     * @throws FileStorageException
     */
    UUID saveFile(byte[] data) throws FileStorageException;

    /**
     * Store the content of stream in a new temporary file.
     *
     * @param stream    stream which content is to be stored
     * @param listener  optional listener to be notified about storing progress
     * @return          temporary file ID. This ID is cached in memory and can be used for subsequent operations.
     * @throws FileStorageException
     */
    UUID saveFile(InputStream stream, @Nullable UploadProgressListener listener) throws FileStorageException;

    /**
     * Create a new empty temporary file and cache its ID for subsequent operations.
     *
     * @return the new temporary file ID
     * @throws FileStorageException
     */
    UUID createEmptyFile() throws FileStorageException;

    /**
     * Create a new empty temporary file and cache its ID for subsequent operations.
     *
     * @return the new temporary file ID
     * @throws FileStorageException
     */
    FileInfo createFile() throws FileStorageException;

    /**
     * Create and cache a new temporary file ID. Doesn't create any real file on disk.
     *
     * @return temporary file ID
     * @throws FileStorageException
     */
    UUID createNewFileId() throws FileStorageException;

    /**
     * Return a previously registered temporary file by its ID.
     *
     * @param fileId    temporary file ID
     * @return          temporary file object or null if no file registered under this ID
     */
    @Nullable
    File getFile(UUID fileId);

    /**
     * Construct a new FileDescriptor for a temporary file to store it in the middleware FileStorage.
     *
     * @param fileId    temporary file ID
     * @param name      file name to set in the FileDescriptor
     * @return          the new FileDescriptor instance
     */
    @Nullable
    FileDescriptor getFileDescriptor(UUID fileId, String name);

    /**
     * Remove a file from the temporary storage.
     * <p/>
     * This method is automatically called from putFileIntoStorage() when the file is succesfully stored on the
     * middleware.
     *
     * @param fileId temporary file ID
     * @throws FileStorageException
     */
    void deleteFile(UUID fileId) throws FileStorageException;

    /**
     * Remove an entry from the list of currently cached temporary file IDs, if such exists.
     * This method is used by the framework when cleaning up the temp folder.
     *
     * @param fileName absolute path to the temporary file
     */
    void deleteFileLink(String fileName);

    /**
     * Upload a file from the client's temporary storage to the middleware FileStorage.
     *
     * @param fileId    file ID in the temporary storage
     * @param fileDescr corresponding file descriptor entity. <b>Attention:</b> the FileDescriptor instance must be
     *                  stored in the database separately.
     * @throws FileStorageException
     */
    void putFileIntoStorage(UUID fileId, FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Upload a file from the client's temporary storage into the middleware FileStorage.
     * <p/>
     * This method is intended for integration with BackgroundTasks and should be called from
     * {@link com.haulmont.cuba.gui.executors.BackgroundTask#run(com.haulmont.cuba.gui.executors.TaskLifeCycle)}
     * <p/>
     * {@link com.haulmont.cuba.gui.executors.TaskLifeCycle#getParams()} map must contain the following entries with
     * String keys:
     * <ul>
     *     <li/><code>fileId</code> - file ID in the temporary storage
     *     <li/><code>fileName</code> - file name to set in the returned {@link FileDescriptor}
     * </ul>
     *
     * <p>Usage: </p>
     * <pre>
     * BackgroundTask&lt;Long, FileDescriptor&gt; uploadProgress = new BackgroundTask&lt;Long, FileDescriptor&gt;(2400, ownerWindow) {
     *     &#64;Override
     *     public Map&lt;String, Object&gt; getParams() {
     *         // file parameters
     *         Map&lt;String, Object&gt; params = new HashMap&lt;&gt;();
     *         params.put("fileId", fileUpload.getFileId());
     *         params.put("fileName", fileUpload.getFileName());
     *         return params;
     *     }
     *
     *     &#64;Override
     *     public FileDescriptor run(final TaskLifeCycle&lt;Long&gt; taskLifeCycle) throws Exception {
     *         // upload file to middleware and return FileDescriptor
     *         return fileUploading.putFileIntoStorage(taskLifeCycle);
     *     }
     *
     *     &#64;Override
     *     public void done(FileDescriptor result) {
     *         // commit FileDescriptor to DB
     *         dataService.commit(new CommitContext(result));
     *     }
     * };
     * </pre>
     *
     * <p>And then we can show upload progress window:</p>
     *
     * <pre>
     *     long fileSize = fileUploading.getFile(fileUpload.getFileId()).length();
           BackgroundWorkProgressWindow.show(uploadProgress, getMessage("uploadingFile"), null, fileSize, true, true);
     * </pre>
     *
     * @param taskLifeCycle task life cycle with specified params: fileId and fileName
     * @return a new file descriptor. <b>Attention:</b> the returned {@link FileDescriptor} instance must be
     *         stored in the database separately.
     * @throws FileStorageException
     */
    FileDescriptor putFileIntoStorage(TaskLifeCycle<Long> taskLifeCycle)
            throws FileStorageException, InterruptedIOException;

    class FileInfo {
        private UUID id;
        private File file;

        public FileInfo(File file, UUID id) {
            this.file = file;
            this.id = id;
        }

        public File getFile() {
            return file;
        }

        public UUID getId() {
            return id;
        }
    }
}