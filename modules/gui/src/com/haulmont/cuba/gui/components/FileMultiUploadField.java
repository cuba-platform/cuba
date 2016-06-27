/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.components;

import java.util.Map;
import java.util.UUID;

public interface FileMultiUploadField extends UploadField {

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
}