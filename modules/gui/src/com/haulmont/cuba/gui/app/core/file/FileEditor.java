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

import java.io.File;
import java.util.Map;

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
                    extLabel.setValue(getFileExt(uploadField.getFileName()));

                    FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
                    File file = fileUploading.getFile(uploadField.getFileId());
                    Integer size = (int)file.length();
                    sizeLab.setValue(size);

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
        FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
        try {
            fileUploading.putFileIntoStorage(uploadField.getFileId(), ds.getItem());
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }
}
