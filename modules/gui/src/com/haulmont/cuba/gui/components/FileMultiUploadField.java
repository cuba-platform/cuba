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
public interface FileMultiUploadField extends Component, Component.HasCaption, Component.BelongToFrame {

    String NAME = "multiUpload";

    public abstract class UploadListener {

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
        public boolean uploadError(String fileName) {
            return false;
        }
    }

    void addListener(UploadListener listener);

    void removeListener(UploadListener listener);

    /**
     * Get uploads map
     *
     * @return Map ( UUID - Id of file in FileUploadService, String - FileName )
     */
    Map<UUID, String> getUploadsMap();

    /**
     * Clear uploads list
     */
    void clearUploads();
}