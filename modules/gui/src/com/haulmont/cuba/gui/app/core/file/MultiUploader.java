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

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 */
public class MultiUploader extends AbstractWindow {

    protected Map<FileDescriptor, UUID> tmpFileDescriptors = new HashMap<>();

    @Inject
    protected CollectionDatasource<FileDescriptor, UUID> filesDs = null;

    @Inject
    protected FileMultiUploadField multiUpload;

    @Named("windowActions.windowCommit")
    protected Button okBtn;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions()
                .setHeight(themeConstants.getInt("cuba.gui.multiupload.height"))
                .setResizable(true);

        filesDs.refresh();

        okBtn.setEnabled(false);

        multiUpload.setCaption(getMessage("upload"));
        multiUpload.addQueueUploadCompleteListener(() -> {
            okBtn.setEnabled(true);

            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
            Map<UUID, String> uploads = multiUpload.getUploadsMap();
            for (Map.Entry<UUID, String> upload : uploads.entrySet()) {
                FileDescriptor fDesc = fileUploading.getFileDescriptor(upload.getKey(), upload.getValue());

                tmpFileDescriptors.put(fDesc, upload.getKey());
                filesDs.addItem(fDesc);
            }
            multiUpload.clearUploads();
        });

        multiUpload.addFileUploadStartListener(e -> okBtn.setEnabled(false));
    }

    @Override
    public void ready() {
        // skip set item
        okBtn.setEnabled(false);
    }

    @Override
    public boolean close(String actionId) {
        if (!COMMIT_ACTION_ID.equals(actionId)) {
            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
            for (Map.Entry<FileDescriptor, UUID> upload : tmpFileDescriptors.entrySet()) {
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
                fileUploading.putFileIntoStorage(tmpFileDescriptors.get(fDesc), fDesc);
            }
        } catch (FileStorageException e) {
            throw new RuntimeException("Unable to put files into storage", e);
        }
    }

    public Collection<FileDescriptor> getFiles() {
        return filesDs.getItems();
    }

    public void commitAndClose() {
        if (getDsContext().commit()) {
            saveFiles();

            close(COMMIT_ACTION_ID);
        }
    }

    public void close() {
        close(CLOSE_ACTION_ID);
    }
}