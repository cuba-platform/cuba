/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 15:51:19
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.file;

import com.haulmont.cuba.core.app.FileUploadService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.web.app.FileDownloadHelper;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class FileEditor extends AbstractEditor {

    private Datasource<FileDescriptor> ds;
    private Button okBtn;
    private TextField nameText;
    private Label extLabel;
    private Label sizeLab;
    private Label createDateLab;
    private FileUploadField uploadField;

    private boolean needSave;

    public FileEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        ds = getDsContext().get("fileDs");

        okBtn = getComponent("windowActions.windowCommit");

        uploadField = getComponent("uploadField");
        nameText = getComponent("name");
        extLabel = getComponent("extension");
        sizeLab = getComponent("size");
        createDateLab = getComponent("createDate");
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        boolean isNew = PersistenceHelper.isNew(ds.getItem());

        if (isNew) {
            okBtn.setEnabled(false);

            uploadField.addListener(new FileUploadField.Listener() {
                public void uploadStarted(Event event) {
                }

                public void uploadFinished(Event event) {
                }

                public void uploadSucceeded(Event event) {
                    nameText.setValue(uploadField.getFileName());
                    extLabel.setValue(FileDownloadHelper.getFileExt(uploadField.getFileName()));

                    FileUploadService uploadService = ServiceLocator.lookup(FileUploadService.NAME);
                    File file = uploadService.getFile(uploadField.getFileId());
                    sizeLab.setValue(file.length());

                    createDateLab.setValue(TimeProvider.currentTimestamp());
                    okBtn.setEnabled(true);

                    needSave = true;
                }

                public void uploadFailed(Event event) {
                }

                public void updateProgress(long readBytes, long contentLength) {
                }
            });
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
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.NAME);
        FileUploadService uploadService = ServiceLocator.lookup(FileUploadService.NAME);
        try {
            UUID fileId = uploadField.getFileId();
            File file = uploadService.getFile(fileId);
            fss.putFile(ds.getItem(), file);
            uploadService.deleteFile(fileId);
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }
}
