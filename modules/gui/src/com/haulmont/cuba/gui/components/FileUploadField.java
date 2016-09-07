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

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.UUID;

public interface FileUploadField extends UploadField, Field {
    String NAME = "upload";

    /**
     * Defines when FileDescriptor will be commited.
     */
    enum FileStoragePutMode {
        /**
         * User have to put FileDescriptor into FileStorage and commit it to database manually.
         */
        MANUAL,
        /**
         * FileDescriptor will be placed into FileStorage and commited to database right after upload.
         */
        IMMEDIATE
    }

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
     * Get id for uploaded file in {@link com.haulmont.cuba.gui.upload.FileUploading}.
     * @return File Id.
     */
    UUID getFileId();
    String getFileName();
    FileDescriptor getFileDescriptor();

    /**
     * Get content bytes for uploaded file.
     * @return Bytes for uploaded file.
     * @deprecated Please use {@link FileUploadField#getFileId()} method and {@link com.haulmont.cuba.gui.upload.FileUploading}.
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

    /**
     * @return content of uploaded file.
     */
    @Nullable
    InputStream getFileContent();

    /**
     * Enable or disable displaying name of uploaded file next to upload button.
     */
    void setShowFileName(boolean showFileName);

    /**
     * @return true if name of uploaded file is shown.
     */
    boolean isShowFileName();
    /**
     * Setup caption of upload button.
     */
    void setUploadButtonCaption(String caption);

    /**
    * @return upload button caption.
    */
    String getUploadButtonCaption();

    /**
     * Setup upload button icon.
     */
    void setUploadButtonIcon(String icon);

    /**
     * @return upload button icon.
     */
    String getUploadButtonIcon();

    /**
     * Setup upload button description.
     */
    void setUploadButtonDescription(String description);

    /**
     * @return upload button description.
     */
    String getUploadButtonDescription();

    /**
     * Enable or disable displaying name of clear button.
     */
    void setShowClearButton(boolean showClearButton);

    /**
     * @return true if clear button is shown.
     */
    boolean isShowClearButton();

    /**
     * Setup clear button caption.
     */
    void setClearButtonCaption(String caption);

    /**
     * @return clear button caption.
     */
    String getClearButtonCaption();

    /**
     * Setup clear button icon.
     */
    void setClearButtonIcon(String icon);

    /**
     * @return clear button icon.
     */
    String getClearButtonIcon();

    /**
     * Setup clear button description.
     */
    void setClearButtonDescription(String description);

    /**
     * @return clear button description.
     */
    String getClearButtonDescription();

    /**
     * Set mode which determines when {@link FileDescriptor} will be commited.
     */
    void setMode(FileStoragePutMode mode);
    /**
     * @return mode which determines when {@link FileDescriptor} will be commited.
     */
    FileStoragePutMode getMode();
}