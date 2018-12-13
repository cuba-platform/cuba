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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.compatibility.FileMultiUploadFieldQueueUploadCompleteListener;

import java.util.EventObject;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface FileMultiUploadField extends UploadField {

    String NAME = "multiUpload";

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

    /**
     * @see QueueUploadCompleteEvent
     * @deprecated Use {@link #addQueueUploadCompleteListener(Consumer)} instead.
     */
    @Deprecated
    @FunctionalInterface
    interface QueueUploadCompleteListener {
        void queueUploadComplete();
    }

    /**
     * @deprecated Use {@link #addQueueUploadCompleteListener(Consumer)} instead
     */
    @Deprecated
    default void addQueueUploadCompleteListener(QueueUploadCompleteListener listener) {
        addQueueUploadCompleteListener(new FileMultiUploadFieldQueueUploadCompleteListener(listener));
    }

    /**
     * @deprecated Use {@link #removeFileUploadErrorListener(Consumer)} instead
     */
    @Deprecated
    default void removeQueueUploadCompleteListener(QueueUploadCompleteListener listener) {
        removeQueueUploadCompleteListener(new FileMultiUploadFieldQueueUploadCompleteListener(listener));
    }

    /**
     * Adds queue upload complete listener. It is invoked when all selected files are uploaded to the temporary storage.
     *
     * @param listener a listener to add
     * @return subscription
     */
    Subscription addQueueUploadCompleteListener(Consumer<QueueUploadCompleteEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeQueueUploadCompleteListener(Consumer<QueueUploadCompleteEvent> listener);

    /**
     *  Describes queue upload complete event.
     */
    class QueueUploadCompleteEvent extends EventObject {

        public QueueUploadCompleteEvent(FileMultiUploadField source) {
            super(source);
        }

        @Override
        public FileMultiUploadField getSource() {
            return (FileMultiUploadField) super.getSource();
        }
    }
}