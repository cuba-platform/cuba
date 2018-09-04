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
import com.haulmont.cuba.gui.components.sys.EventHubOwner;

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

    default Subscription addQueueUploadCompleteListener(Consumer<QueueUploadCompleteEvent> listener) {
        return ((EventHubOwner) this).getEventHub().subscribe(QueueUploadCompleteEvent.class, listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeQueueUploadCompleteListener(Consumer<QueueUploadCompleteEvent> listener) {
        ((EventHubOwner) this).getEventHub().unsubscribe(QueueUploadCompleteEvent.class, listener);
    }

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