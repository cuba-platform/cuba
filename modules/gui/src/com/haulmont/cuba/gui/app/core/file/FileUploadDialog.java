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

package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * Standard file upload dialog
 *
 */
public class FileUploadDialog extends AbstractWindow {
    private final Logger log = LoggerFactory.getLogger(FileUploadDialog.class);

    @Inject
    protected FileUploadField fileUpload;

    protected UUID fileId;

    protected String fileName;

    public UUID getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        fileUpload.addFileUploadSucceedListener(e -> {
            fileId = fileUpload.getFileId();
            fileName = fileUpload.getFileName();
            close(Window.COMMIT_ACTION_ID);
        });

        fileUpload.addFileUploadErrorListener(e -> {
            showNotification(getMessage("notification.uploadUnsuccessful"), NotificationType.WARNING);
            if (e.getCause() != null) {
                log.error("An error occurred while uploading", e.getCause());
            }
        });
    }
}