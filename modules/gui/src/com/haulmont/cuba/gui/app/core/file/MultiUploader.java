/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImpl;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class MultiUploader extends AbstractEditor {

    private FileMultiUploadField uploadField = null;
    private Button okBtn;
    private boolean needSave;
    private CollectionDatasource<FileDescriptor, UUID> filesDs = null;
    private Table uploadsTable = null;
    private List<FileDescriptor> files = new ArrayList<>();

    private Map<FileDescriptor, UUID> descriptors = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        uploadsTable = getComponent("uploadsTable");

        filesDs = uploadsTable.getDatasource();
        filesDs.refresh();

        okBtn = getComponent("windowActions.windowCommit");
        okBtn.setEnabled(false);

        uploadsTable.addAction(new RemoveAction(uploadsTable, true));

        uploadField = getComponent("multiUpload");
        uploadField.setCaption(getMessage("upload"));
        uploadField.addListener(new FileMultiUploadField.UploadListener() {

            @Override
            public void queueUploadComplete() {
                needSave = true;
                okBtn.setEnabled(true);
                FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
                Map<UUID, String> uploads = uploadField.getUploadsMap();
                for (Map.Entry<UUID, String> upload : uploads.entrySet()) {
                    FileDescriptor fDesc = fileUploading.getFileDescriptor(upload.getKey(), upload.getValue());

                    descriptors.put(fDesc, upload.getKey());
                    filesDs.addItem(fDesc);
                }
                uploads.clear();
                uploadsTable.refresh();
            }

            @Override
            public void fileUploadStart(String fileName) {
                okBtn.setEnabled(false);
            }
        });
    }

    @Override
    public void setItem(Entity item) {
        // Do nothing
        okBtn.setEnabled(false);
    }

    @Override
    public void commitAndClose() {
        ((DatasourceImpl) filesDs).setModified(false);
        if (commit()) {
            if (needSave) {
                saveFile();
            }
            close(COMMIT_ACTION_ID);
        }
    }

    @Override
    public boolean close(String actionId) {
        if (!COMMIT_ACTION_ID.equals(actionId)) {
            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
            for (Map.Entry<FileDescriptor, UUID> upload : descriptors.entrySet()) {
                try {
                    fileUploading.deleteFile(upload.getValue());
                } catch (FileStorageException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.close(actionId);
    }

    private void saveFile() {
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        try {
            // Relocate the file from temporary storage to permanent
            for (FileDescriptor fDesc : filesDs.getItems()) {
                fileUploading.putFileIntoStorage(descriptors.get(fDesc), fDesc);
                files.add(fDesc);
            }
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileDescriptor> getFiles() {
        return files;
    }
}