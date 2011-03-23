/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 23.11.2010 14:18:33
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.file;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.app.FileUploadService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

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
                FileUploadService uploader = ServiceLocator.lookup(FileUploadService.NAME);
                Map<UUID, String> uploads = uploadField.getUploadsMap();
                for (Map.Entry<UUID, String> upload : uploads.entrySet()) {
                    FileDescriptor fDesc = uploader.getFileDescriptor(upload.getKey(), upload.getValue());

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
            FileUploadService uploadService = ServiceLocator.lookup(FileUploadService.NAME);
            for (Map.Entry<FileDescriptor, UUID> upload : descriptors.entrySet()) {
                try {
                    uploadService.deleteFile(upload.getValue());
                } catch (FileStorageException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.close(actionId);
    }

    private void saveFile() {
        FileUploadService uploader = ServiceLocator.lookup(FileUploadService.NAME);
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.NAME);
        try {
            // Relocate the file from temporary storage to permanent
            Collection ids = filesDs.getItemIds();
            for (Object id : ids) {
                FileDescriptor fDesc = (FileDescriptor) filesDs.getItem(id);
                UUID fileId = descriptors.get(fDesc);
                fss.putFile(fDesc, uploader.getFile(fileId));
                uploader.deleteFile(fileId);
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
