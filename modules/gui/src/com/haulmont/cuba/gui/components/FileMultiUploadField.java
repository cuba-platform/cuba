/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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

    int QUEUE_LIMIT_EXCEEDED = -100;
    int FILE_EXCEEDS_SIZE_LIMIT = -110;
    int ZERO_BYTE_FILE = -120;
    int INVALID_FILETYPE = -130;

    public abstract class UploadListener {
        public void progressChanged(String fileName, int totalBytes, int contentLength) {
        }

        public void fileUploaded(String fileName) {
        }

        public void fileUploadStart(String fileName) {
        }

        public void queueUploadComplete() {
        }

        public void errorNotify(String fileName, String message, int errorCode) {

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
}
