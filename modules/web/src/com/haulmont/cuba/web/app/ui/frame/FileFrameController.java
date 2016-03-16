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
package com.haulmont.cuba.web.app.ui.frame;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.app.core.file.FileDownloadHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.Map;

/**
 */
public class FileFrameController extends AbstractWindow {

    private CollectionDatasource ds;

    private FileUploadField uploadField;

    private FileDescriptor fd;

    private Table filesTable;

    @Inject
    private Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        uploadField = (FileUploadField) getComponent("uploadField");
        filesTable = (Table) getComponent("files");
        initGeneratedColumn();
        ds = (CollectionDatasource) getDsContext().get("filesDs");
        Button remove = (Button) getComponentNN("remove");
        remove.setAction(new RemoveAction(filesTable, false));

        uploadField.addFileUploadSucceedListener(e -> {
            fd = metadata.create(FileDescriptor.class);
            fd.setName(uploadField.getFileName());
            fd.setExtension(FilenameUtils.getExtension(uploadField.getFileName()));

            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
            File file = fileUploading.getFile(uploadField.getFileId());
            fd.setSize(file.length());

            fd.setCreateDate(AppBeans.get(TimeSource.class).currentTimestamp());
            saveFile();
            ds.addItem(fd);
            showNotification(getMessage("uploadSuccess"), NotificationType.HUMANIZED);
        });

        uploadField.addFileUploadErrorListener(e -> showNotification(getMessage("uploadUnsuccess"), NotificationType.HUMANIZED));
    }

    public void initGeneratedColumn() {
        if (filesTable.getDatasource().getState().equals(Datasource.State.VALID)) {
            FileDownloadHelper.initGeneratedColumn(filesTable);
        }
    }

    protected void saveFile() {
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        try {
            fileUploading.putFileIntoStorage(uploadField.getFileId(), fd);
        } catch (FileStorageException e) {
            throw new RuntimeException("Unable to put file to storage", e);
        }
    }
}