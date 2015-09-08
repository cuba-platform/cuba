/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author devyatkin
 * @version $Id$
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