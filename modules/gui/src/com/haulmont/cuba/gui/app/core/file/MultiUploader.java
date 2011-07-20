/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 23.11.2010 14:18:33
 *
 * $Id$
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import java.util.*;
import java.util.List;

public class MultiUploader extends AbstractEditor {

    private static final long serialVersionUID = 8050367710436521429L;

    private FileMultiUploadField uploadField = null;
    private Button okBtn;
    private boolean needSave;
    private CollectionDatasource filesDs = null;
    private Table uploadsTable = null;
    private List<FileDescriptor> files = new ArrayList<FileDescriptor>();

    private Map<FileDescriptor, UUID> descriptors = new HashMap<FileDescriptor, UUID>();

    public MultiUploader(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
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
                FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
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

            @Override
            public void errorNotify(String fileName, String message, int errorCode) {
                if (errorCode == FileMultiUploadField.FILE_EXCEEDS_SIZE_LIMIT) {
                    String locMessage = MessageProvider.getMessage(getClass(), "fileExceedsSizeLimit") + ":" + fileName;
                    MultiUploader.this.showNotification(locMessage, NotificationType.WARNING);
                } else {
                    String locMessage = MessageProvider.getMessage(getClass(), "fileUploadError") + ":" + fileName;
                    MultiUploader.this.showNotification(locMessage, NotificationType.ERROR);
                }
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
        ((MultiuploadsDatasource) filesDs).setModified(false);
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
            FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
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
        FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);
        try {
            // Relocate the file from temporary storage to permanent
            Collection ids = filesDs.getItemIds();
            for (Object id : ids) {
                FileDescriptor fDesc = (FileDescriptor) filesDs.getItem(id);
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
