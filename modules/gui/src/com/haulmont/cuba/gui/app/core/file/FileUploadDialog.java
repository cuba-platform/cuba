/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import javax.inject.Inject;
import java.io.File;
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

    @Inject
    private FileUploadingAPI fileUploading;

    private File file;

    public File getFile() {
        return file;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        fileUpload.addListener(new FileUploadField.ListenerAdapter() {
            @Override
            public void uploadSucceeded(Event event) {
                UUID fileId = fileUpload.getFileId();
                file = fileUploading.getFile(fileId);
                close(Window.COMMIT_ACTION_ID);
            }
        });
    }
}
