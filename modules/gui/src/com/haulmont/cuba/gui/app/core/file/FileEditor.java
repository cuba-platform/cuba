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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

public class FileEditor extends AbstractEditor<FileDescriptor> {

    private static final Logger log = LoggerFactory.getLogger(FileEditor.class);

    @Inject
    protected Datasource<FileDescriptor> fileDs;

    @Resource(name = "windowActions.windowCommit")
    protected Button okBtn;

    @Inject
    protected TextField<String> nameField;
    @Inject
    protected Label<String> extLabel;
    @Inject
    protected Label<String> sizeLabel;
    @Inject
    protected Label<String> createDateLabel;

    @Inject
    protected FileUploadField uploadField;
    @Inject
    protected FileUploadingAPI fileUploadingAPI;

    protected boolean needSave;

    @Inject
    protected TimeSource timeSource;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions().setWidthAuto();

        uploadField.setDropZone(new UploadField.DropZone(this));
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        boolean isNew = PersistenceHelper.isNew(fileDs.getItem());

        if (isNew) {
            okBtn.setEnabled(false);
            uploadField.addFileUploadSucceedListener(new FileUploadListener());
            uploadField.focus();
        } else {
            uploadField.setVisible(false);
            nameField.focus();
        }
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed && needSave) {
            saveFile();
        }

        return super.postCommit(committed, close);
    }

    protected void saveFile() {
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        try {
            fileUploading.putFileIntoStorage(uploadField.getFileId(), fileDs.getItem());
        } catch (FileStorageException e) {
            showNotification(getMessage("fileEditor.unableToSaveFile"), NotificationType.ERROR);
            log.error("Unable to save file to middleware", e);
        }
    }

    protected class FileUploadListener implements Consumer<FileUploadField.FileUploadSucceedEvent> {

        @Override
        public void accept(FileUploadField.FileUploadSucceedEvent e) {
            FileDescriptor fd = getItem();

            fd.setName(uploadField.getFileName());
            fd.setCreateDate(timeSource.currentTimestamp());
            fd.setExtension(FilenameUtils.getExtension(uploadField.getFileName()));

            File file = fileUploadingAPI.getFile(uploadField.getFileId());

            fd.setSize(file.length());

            okBtn.setEnabled(true);

            nameField.focus();

            needSave = true;
        }
    }
}