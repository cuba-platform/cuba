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
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class MultiUploader extends AbstractEditor {

    protected boolean needSave;
    protected CollectionDatasource<FileDescriptor, UUID> filesDs = null;
    protected List<FileDescriptor> files = new ArrayList<>();

    protected Map<FileDescriptor, UUID> descriptors = new HashMap<>();

    @Inject
    protected FileMultiUploadField multiUpload;

    @Inject
    protected Table uploadsTable;

    @Named("windowActions.windowCommit")
    protected Button okBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setResizable(true);

        filesDs = uploadsTable.getDatasource();
        filesDs.refresh();

        okBtn.setEnabled(false);

        uploadsTable.addAction(new RemoveAction(uploadsTable, true));

        multiUpload.setCaption(getMessage("upload"));
        multiUpload.addListener(new FileMultiUploadField.UploadListener() {

            @Override
            public void queueUploadComplete() {
                needSave = true;
                okBtn.setEnabled(true);
                FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
                Map<UUID, String> uploads = multiUpload.getUploadsMap();
                for (Map.Entry<UUID, String> upload : uploads.entrySet()) {
                    FileDescriptor fDesc = fileUploading.getFileDescriptor(upload.getKey(), upload.getValue());

                    descriptors.put(fDesc, upload.getKey());
                    filesDs.addItem(fDesc);
                }
                multiUpload.clearUploads();
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
        // skip set item
        okBtn.setEnabled(false);
    }

    @Override
    public void commitAndClose() {
        if (commit()) {
            if (needSave) {
                saveFiles();
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
                    throw new RuntimeException("Unable to delete file from temp storage", e);
                }
            }
        }
        return super.close(actionId);
    }

    protected void saveFiles() {
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        try {
            // Relocate the file from temporary storage to permanent
            for (FileDescriptor fDesc : filesDs.getItems()) {
                fileUploading.putFileIntoStorage(descriptors.get(fDesc), fDesc);
                files.add(fDesc);
            }
        } catch (FileStorageException e) {
            throw new RuntimeException("Unable to put files into storage", e);
        }
    }

    public List<FileDescriptor> getFiles() {
        return files;
    }
}