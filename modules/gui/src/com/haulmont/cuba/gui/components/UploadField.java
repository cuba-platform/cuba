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

import java.util.Set;

public interface UploadField extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon {

    abstract class FileUploadEvent {
        private final String fileName;
        private final long contentLength;

        protected FileUploadEvent(String fileName, long contentLength) {
            this.fileName = fileName;
            this.contentLength = contentLength;
        }

        public long getContentLength() {
            return contentLength;
        }

        public String getFileName() {
            return fileName;
        }
    }

    class FileUploadStartEvent extends FileUploadEvent {
        public FileUploadStartEvent(String fileName, long contentLength) {
            super(fileName, contentLength);
        }
    }

    interface FileUploadStartListener {
        void fileUploadStart(FileUploadStartEvent e);
    }

    class FileUploadFinishEvent extends FileUploadEvent {
        public FileUploadFinishEvent(String fileName, long contentLength) {
            super(fileName, contentLength);
        }
    }

    interface FileUploadFinishListener {
        void fileUploadFinish(FileUploadFinishEvent e);
    }

    class FileUploadErrorEvent extends FileUploadEvent {

        private final Exception cause;

        public FileUploadErrorEvent(String fileName, long contentLength, Exception cause) {
            super(fileName, contentLength);

            this.cause = cause;
        }

        public Exception getCause() {
            return cause;
        }
    }

    interface FileUploadErrorListener {
        void fileUploadError(FileUploadErrorEvent e);
    }

    void addFileUploadStartListener(FileUploadStartListener listener);
    void removeFileUploadStartListener(FileUploadStartListener listener);

    void addFileUploadFinishListener(FileUploadFinishListener listener);
    void removeFileUploadFinishListener(FileUploadFinishListener listener);

    void addFileUploadErrorListener(FileUploadErrorListener listener);
    void removeFileUploadErrorListener(FileUploadErrorListener listener);

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
     * @return comma separated types of files
     */
    String getAccept();

    /**
     * Sets the mask for files to filter them in the file selection dialog.<br/>
     * Example: <pre>{@code fileUpload.setAccept(".png,.jpeg")}</pre>
     * @param accept comma separated types of files
     */
    void setAccept(String accept);

    /**
     * Set white list of comma separated file extensions.
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