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
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.util.*;
import java.util.List;

public class MultiUploader extends AbstractEditor {

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

        TableActionsHelper helper = new TableActionsHelper(this, uploadsTable);
        helper.createRemoveAction();

        uploadField = getComponent("multiUpload");
        uploadField.setCaption(getMessage("upload"));
        uploadField.addListener(new FileMultiUploadField.UploadListener() {

            @Override
            public void queueUploadComplete() {
                needSave = true;
                okBtn.setEnabled(true);
                FileUploadService uploader = ServiceLocator.lookup(FileUploadService.NAME);
                Map<UUID, String> uploads = uploadField.getUploadsMap();
                Iterator<Map.Entry<UUID, String>> iterator = uploads.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, String> item = iterator.next();

                    FileDescriptor fDesc = uploader.getFileDescriptor(item.getKey(), item.getValue());

                    descriptors.put(fDesc, item.getKey());
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

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private void saveFile() {
        FileUploadService uploader = ServiceLocator.lookup(FileUploadService.NAME);
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.JNDI_NAME);
        try {
            // Relocate the file from temporary storage to permanent
            Collection ids = filesDs.getItemIds();
            Iterator iter = ids.iterator();
            while (iter.hasNext()) {
                FileDescriptor fDesc = (FileDescriptor) filesDs.getItem(iter.next());
                UUID fileId = descriptors.get(fDesc);
                fss.putFile(fDesc, uploader.getFile(fileId));
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
