/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author artamonov
 * @version $Id$
 */
public interface UploadComponentSupport extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon {

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
}