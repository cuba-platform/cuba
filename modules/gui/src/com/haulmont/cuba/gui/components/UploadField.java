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

import java.util.EventObject;
import java.util.Set;
import java.util.function.Consumer;

public interface UploadField extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon,
                                     Component.Focusable {

    /**
     * Base class for UploadField events.
     */
    abstract class FileUploadEvent extends EventObject {
        private final String fileName;
        private final long contentLength;

        protected FileUploadEvent(UploadField source, String fileName, long contentLength) {
            super(source);
            this.fileName = fileName;
            this.contentLength = contentLength;
        }

        public long getContentLength() {
            return contentLength;
        }

        public String getFileName() {
            return fileName;
        }

        @Override
        public UploadField getSource() {
            return (UploadField) super.getSource();
        }
    }

    /**
     * Describes file upload start event.
     */
    class FileUploadStartEvent extends FileUploadEvent {
        public FileUploadStartEvent(UploadField source, String fileName, long contentLength) {
            super(source, fileName, contentLength);
        }
    }

    /**
     * Describes file upload finish event.
     */
    class FileUploadFinishEvent extends FileUploadEvent {
        public FileUploadFinishEvent(UploadField source, String fileName, long contentLength) {
            super(source, fileName, contentLength);
        }
    }

    /**
     * Describes file upload error event. When the uploads are finished, but unsuccessful.
     */
    class FileUploadErrorEvent extends FileUploadEvent {

        private final Exception cause;

        public FileUploadErrorEvent(UploadField source, String fileName, long contentLength, Exception cause) {
            super(source, fileName, contentLength);

            this.cause = cause;
        }

        public Exception getCause() {
            return cause;
        }
    }

    /**
     * Adds file upload start listener. It is invoked when start uploading the file.
     *
     * @param listener a listener to add
     * @return subscription
     */
    Subscription addFileUploadStartListener(Consumer<FileUploadStartEvent> listener);

    /**
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeFileUploadStartListener(Consumer<FileUploadStartEvent> listener);

    /**
     * Adds file upload finish listener. It is invoked when file is uploaded.
     *
     * @param listener a listener to add
     * @return subscription
     */
    Subscription addFileUploadFinishListener(Consumer<FileUploadFinishEvent> listener);

    /**
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeFileUploadFinishListener(Consumer<FileUploadFinishEvent> listener);

    /**
     * Adds file upload error listener. It is invoked when the uploads are finished, but unsuccessful.
     *
     * @param listener a listener to add
     * @return subscription
     */
    Subscription addFileUploadErrorListener(Consumer<FileUploadErrorEvent> listener);

    /**
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeFileUploadErrorListener(Consumer<FileUploadErrorEvent> listener);

    /**
     * Returns maximum allowed file size in bytes.
     */
    long getFileSizeLimit();

    /**
     * Sets maximum allowed file size in bytes.
     * Default value is 0. In this case component uses system value.
     */
    void setFileSizeLimit(long fileSizeLimit);

    /**
     * Returns comma separated types of files.
     *
     * @return comma separated types of files
     */
    String getAccept();

    /**
     * Sets the mask for files to filter them in the file selection dialog.<br>
     * Example: <pre>{@code fileUpload.setAccept(".png,.jpeg")}</pre>
     *
     * @param accept comma separated types of files
     */
    void setAccept(String accept);

    /**
     * Set white list of file extensions. Each extension should start with dot symbol, e.g. ".png".
     * <pre>{@code
     *    private FileUploadField uploadField;
     *    ...
     *    uploadField.setPermittedExtensions(Sets.newHashSet(".png", ".jpg"));
     * }</pre>
     *
     * @param permittedExtensions permitted extensions.
     */
    void setPermittedExtensions(Set<String> permittedExtensions);

    /**
     * Return white list of file extensions.
     *
     * @return set of file extensions.
     */
    Set<String> getPermittedExtensions();

    /**
     * @return current drop zone
     */
    DropZone getDropZone();

    /**
     * Set drop zone reference to this upload component. Files can be dropped to component of the drop zone
     * to be uploaded by this upload component.
     *
     * @param dropZone drop zone descriptor
     */
    void setDropZone(DropZone dropZone);

    /**
     * Set paste zone reference to this upload component. PasteZone handles paste shortcut when a text input field
     * in the container is focused.
     * <br>
     * It is supported by Chromium-based browsers.
     *
     * @param pasteZone paste zone container
     */
    void setPasteZone(ComponentContainer pasteZone);

    /**
     * @return current paste zone container
     */
    ComponentContainer getPasteZone();

    /**
     * @return current drop zone prompt
     */
    String getDropZonePrompt();

    /**
     * Set drop zone prompt that will be shown on drag over window with file.
     *
     * @param dropZonePrompt drop zone prompt
     */
    void setDropZonePrompt(String dropZonePrompt);

    /**
     * Drop zone descriptor. BoxLayout or Window can be used as drop zone for an upload component.
     */
    class DropZone {
        protected BoxLayout layout;

        protected Window window;

        public DropZone(BoxLayout targetLayout) {
            this.layout = targetLayout;
        }

        public DropZone(Window window) {
            this.window = window;
        }

        public BoxLayout getTargetLayout() {
            return layout;
        }

        public Window getTargetWindow() {
            return window;
        }

        public Component getTarget() {
            if (window != null) {
                return window;
            } else {
                return layout;
            }
        }
    }
}