/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 20.11.2009 14:10:02
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.frame;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.app.FileUploadService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.app.FileDownloadHelper;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class FileFrameController extends AbstractWindow {

    private CollectionDatasource ds;

    private FileUploadField uploadField;

    private FileDescriptor fd;

    private Table filesTable;

    public FileFrameController(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        uploadField = getComponent("uploadField");
        filesTable = getComponent("files");
        initGeneratedColumn();
        ds = getDsContext().get("filesDs");
        TableActionsHelper helper = new TableActionsHelper(this, filesTable);
        Button remove = getComponent("remove");
        remove.setAction(helper.createRemoveAction(false));

        uploadField.addListener(new FileUploadField.Listener() {
            public void uploadStarted(Event event) {
                uploadField.setEnabled(false);
            }

            public void uploadFinished(Event event) {
                uploadField.setEnabled(true);
            }

            public void uploadSucceeded(Event event) {
                fd = new FileDescriptor();
                fd.setName(uploadField.getFileName());
                fd.setExtension(FileDownloadHelper.getFileExt(uploadField.getFileName()));

                FileUploadService uploadService = ServiceLocator.lookup(FileUploadService.NAME);
                File file = uploadService.getFile(uploadField.getFileId());
                fd.setSize((int)file.length());

                fd.setCreateDate(TimeProvider.currentTimestamp());
                saveFile();
                ds.addItem(fd);
                showNotification(MessageProvider.getMessage(getClass(), "uploadSuccess"), NotificationType.HUMANIZED);
            }

            public void uploadFailed(Event event) {
                showNotification(MessageProvider.getMessage(getClass(), "uploadUnsuccess"), NotificationType.HUMANIZED);
            }

            public void updateProgress(long readBytes, long contentLength) {
            }
        });
    }

    public void initGeneratedColumn() {
        if (filesTable.getDatasource().getState().equals(Datasource.State.VALID)) {
            FileDownloadHelper.initGeneratedColumn(filesTable);
        }
    }

    private void saveFile() {
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.NAME);
        FileUploadService uploadService = ServiceLocator.lookup(FileUploadService.NAME);
        try {
            UUID fileId = uploadField.getFileId();
            File file = uploadService.getFile(fileId);
            fss.putFile(fd, file);
            uploadService.deleteFile(fileId);
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }
}
