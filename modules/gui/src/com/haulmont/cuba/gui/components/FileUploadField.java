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

import com.haulmont.cuba.core.entity.FileDescriptor;

import java.util.UUID;

public interface FileUploadField extends UploadField {
    String NAME = "upload";

    @Deprecated
    interface Listener {
        class Event {
            String filename;
            Exception exception;

            public Event(String filename) {
                this.filename = filename;
            }

            public Event(String filename, Exception exception) {
                this.filename = filename;
                this.exception = exception;
            }

            public String getFilename() {
                return filename;
            }

            public Exception getException() {
                return exception;
            }
        }

        void uploadStarted(Event event);
        void uploadFinished(Event event);

        void uploadSucceeded(Event event);
        void uploadFailed(Event event);
    }

    @Deprecated
    class ListenerAdapter implements Listener {
        @Override
        public void uploadStarted(Event event) {
        }

        @Override
        public void uploadFinished(Event event) {
        }

        @Override
        public void uploadSucceeded(Event event) {
        }

        @Override
        public void uploadFailed(Event event) {
        }
    }

    /**
     * Get id for uploaded file in {@link com.haulmont.cuba.gui.upload.FileUploading}
     * @return File Id
     */
    UUID getFileId();
    String getFileName();
    FileDescriptor getFileDescriptor();

    /**
     * Get content bytes for uploaded file
     * @return Bytes for uploaded file
     * @deprecated Please use {@link FileUploadField#getFileId()} method and {@link com.haulmont.cuba.gui.upload.FileUploading}
     */
    byte[] getBytes();

    @Deprecated
    void addListener(Listener listener);
    @Deprecated
    void removeListener(Listener listener);

    class FileUploadSucceedEvent extends FileUploadEvent {
        public FileUploadSucceedEvent(String fileName, long contentLength) {
            super(fileName, contentLength);
        }
    }

    interface FileUploadSucceedListener {
        void fileUploadSucceed(FileUploadSucceedEvent e);
    }

    void addFileUploadSucceedListener(FileUploadSucceedListener listener);
    void removeFileUploadSucceedListener(FileUploadSucceedListener listener);
}