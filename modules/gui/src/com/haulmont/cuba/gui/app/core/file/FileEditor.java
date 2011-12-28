/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 15:51:19
 *
 * $Id$
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;

public class FileEditor extends AbstractEditor {

    @Inject
    private Datasource<FileDescriptor> fileDs;

    @Resource(name = "windowActions.windowCommit")
    private Button okBtn;

    @Inject
    private TextField nameField;

    @Inject
    private Label extLabel;

    @Inject
    private Label sizeLabel;

    @Inject
    private Label createDateLabel;

    @Inject
    private FileUploadField uploadField;

    private boolean needSave;

    public FileEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
    }

    private String getFileExt(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > -1)
            return StringUtils.substring(fileName, i + 1, i + 20);
        else
            return "";
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        boolean isNew = PersistenceHelper.isNew(fileDs.getItem());

        if (isNew) {
            okBtn.setEnabled(false);
            uploadField.addListener(new FileUploadListener());
        } else {
            uploadField.setEnabled(false);
        }
    }

    @Override
    public void commitAndClose() {
        if (needSave) {
            saveFile();
        }
        super.commitAndClose();
    }

    private void saveFile() {
        FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
        try {
            fileUploading.putFileIntoStorage(uploadField.getFileId(), fileDs.getItem());
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    private class FileUploadListener implements FileUploadField.Listener {
        @Override
        public void uploadStarted(Event event) {
        }

        @Override
        public void uploadFinished(Event event) {
        }

        @Override
        public void uploadSucceeded(Event event) {
            nameField.setValue(uploadField.getFileName());
            extLabel.setValue(getFileExt(uploadField.getFileName()));

            FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
            File file = fileUploading.getFile(uploadField.getFileId());
            Integer size = (int) file.length();
            sizeLabel.setValue(size);

            createDateLabel.setValue(TimeProvider.currentTimestamp());
            okBtn.setEnabled(true);

            needSave = true;
        }

        @Override
        public void uploadFailed(Event event) {
        }

        @Override
        public void updateProgress(long readBytes, long contentLength) {
        }
    }
}
