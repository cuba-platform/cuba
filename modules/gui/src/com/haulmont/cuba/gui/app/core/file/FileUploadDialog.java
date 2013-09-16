/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Window;

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

    @Inject
    private FileUploadField fileUpload;

    private UUID fileId;

    private String fileName;

    public UUID getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        fileUpload.addListener(new FileUploadField.ListenerAdapter() {
            @Override
            public void uploadSucceeded(Event event) {
                fileId = fileUpload.getFileId();
                fileName = fileUpload.getFileName();
                close(Window.COMMIT_ACTION_ID);
            }
        });
    }
}
