/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public interface FileMultiUploadField extends UploadComponentSupport {

    String NAME = "multiUpload";

    @Deprecated
    abstract class UploadListener {
        /**
         * File upload finished. Executed in uploading thread. <br/>
         * <b>Do not perform progress indication here!</b>
         *
         * @param fileName file name
         */
        public void fileUploaded(String fileName) {
        }

        /**
         * File uploading started. Executed in uploading thread. <br/>
         * <b>Do not perform progress indication here!</b>
         *
         * @param fileName file name
         */
        public void fileUploadStart(String fileName) {
        }

        /**
         * Queue upload completed
         */
        public void queueUploadComplete() {
        }

        /**
         * Handle uploading error.
         *
         * @param fileName file name
         * @return true if error handled by listener.
         *         If returned false then component shows default upload error notification.
         */
        @Deprecated
        public boolean uploadError(String fileName) {
            return false;
        }
    }

    @Deprecated
    void addListener(UploadListener listener);

    @Deprecated
    void removeListener(UploadListener listener);

    /**
     * Get uploads map
     *
     * @return Map ( UUID - Id of file in FileUploadingAPI, String - FileName )
     */
    Map<UUID, String> getUploadsMap();

    /**
     * Clear uploads list
     */
    void clearUploads();

    interface QueueUploadCompleteListener {
        void queueUploadComplete();
    }

    void addQueueUploadCompleteListener(QueueUploadCompleteListener listener);
    void removeQueueUploadCompleteListener(QueueUploadCompleteListener listener);

    /**
     * Returns comma separated types of files.
     * @return comma separated types of files
     */
    String getAccept();

    /**
     * Sets the types of files that the server accepts (that can be submitted through a file upload).
     * @param accept comma separated types of files; possible options to specify the file types are {@code *.png, .png}
     */
    void setAccept(String accept);
}