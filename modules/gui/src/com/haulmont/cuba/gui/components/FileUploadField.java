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
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.components.sys.EventTarget;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface FileUploadField extends UploadField, Field<FileDescriptor>, Component.Focusable, Buffered {
    String NAME = "upload";

    /**
     * Defines when FileDescriptor will be committed.
     */
    enum FileStoragePutMode {
        /**
         * User have to put FileDescriptor into FileStorage and commit it to database manually.
         */
        MANUAL,
        /**
         * FileDescriptor will be placed into FileStorage and committed to database right after upload.
         */
        IMMEDIATE
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

    class FileUploadSucceedEvent extends FileUploadEvent {
        public FileUploadSucceedEvent(String fileName, long contentLength) {
            super(fileName, contentLength);
        }
    }

    default Subscription addFileUploadSucceedListener(Consumer<FileUploadSucceedEvent> listener) {
        return ((EventTarget) this).addListener(FileUploadSucceedEvent.class, listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeFileUploadSucceedListener(Consumer<FileUploadSucceedEvent> listener) {
        ((EventTarget) this).removeListener(FileUploadSucceedEvent.class, listener);
    }

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

    class BeforeValueClearEvent {
        private FileUploadField target;
        private boolean clearPrevented = false;

        public BeforeValueClearEvent(FileUploadField target) {
            this.target = target;
        }

        public boolean isClearPrevented() {
            return clearPrevented;
        }

        public void preventClearAction() {
            this.clearPrevented = true;
        }

        public FileUploadField getTarget() {
            return target;
        }
    }

    /**
     * Sets a callback interface which is invoked by the {@link FileUploadField} before value
     * clearing when user use clear button.
     * <p>
     * Listener can prevent value clearing using {@link BeforeValueClearEvent#preventClearAction()}.
     *
     * @param listener a listener to add
     * @see #setShowClearButton(boolean)
     */
    default Subscription addBeforeValueClearListener(Consumer<BeforeValueClearEvent> listener) {
        return ((EventTarget) this).addListener(BeforeValueClearEvent.class, listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeBeforeValueClearListener(Consumer<BeforeValueClearEvent> listener) {
        ((EventTarget) this).removeListener(BeforeValueClearEvent.class, listener);
    }

    class AfterValueClearEvent {
        private FileUploadField target;
        private boolean valueCleared;

        public AfterValueClearEvent(FileUploadField target, boolean valueCleared) {
            this.target = target;
            this.valueCleared = valueCleared;
        }

        public FileUploadField getTarget() {
            return target;
        }

        public boolean isValueCleared() {
            return valueCleared;
        }
    }

    /**
     * Adds a callback interface which is invoked by the {@link FileUploadField} after value
     * has been cleared using clear button.
     *
     * @param listener a listener to add
     * @see #setShowClearButton(boolean)
     */
    default Subscription addAfterValueClearListener(Consumer<AfterValueClearEvent> listener) {
        return ((EventTarget) this).addListener(AfterValueClearEvent.class, listener);
    }

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    default void removeAfterValueClearListener(Consumer<AfterValueClearEvent> listener) {
        ((EventTarget) this).addListener(AfterValueClearEvent.class, listener);
    }

    /**
     * Set mode which determines when {@link FileDescriptor} will be committed.
     */
    void setMode(FileStoragePutMode mode);
    /**
     * @return mode which determines when {@link FileDescriptor} will be committed.
     */
    FileStoragePutMode getMode();

    /**
     * Set content provider which contains file data.
     * <p>Passed content provider will be used for downloading by clicking the link with file name
     * or as source for {@link FileUploadField#getFileContent()} method.</p>
     *
     * @param contentProvider content provider
     */
    void setContentProvider(Supplier<InputStream> contentProvider);
    /**
     * @return FileContentProvider which can be used to read data from field
     */
    Supplier<InputStream> getContentProvider();

    @Deprecated
    interface FileContentProvider extends Supplier<InputStream> {
        @Override
        default InputStream get() {
            return provide();
        }

        InputStream provide();
    }
}