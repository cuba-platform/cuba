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
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class FileBrowser extends AbstractLookup {

    @Inject
    protected Table<FileDescriptor> filesTable;

    @Inject
    protected CollectionDatasource<FileDescriptor, UUID> filesDs;

    @Inject
    protected Button multiUploadBtn;

    @Inject
    protected ExportDisplay exportDisplay;

    @Inject
    protected Security security;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        filesTable.addAction(new ItemTrackingAction("download")
                .withCaption(getMessage("download"))
                .withHandler(event -> {
                    FileDescriptor fileDescriptor = filesTable.getSingleSelected();
                    if (fileDescriptor != null) {
                        exportDisplay.show(fileDescriptor, null);
                    }
                }));

        BaseAction multiUploadAction = new BaseAction("multiupload")
                .withCaption(getMessage("multiupload"))
                .withHandler(event -> {
                    Window window = openWindow("multiuploadDialog", OpenType.DIALOG);
                    window.addCloseListener(actionId -> {
                        if (COMMIT_ACTION_ID.equals(actionId)) {
                            Collection<FileDescriptor> items = ((MultiUploader) window).getFiles();
                            for (FileDescriptor fdesc : items) {
                                boolean modified = filesDs.isModified();
                                filesDs.addItem(fdesc);
                                ((DatasourceImplementation) filesDs).setModified(modified);
                            }

                            filesTable.requestFocus();
                        }
                    });
                });

        multiUploadAction.setEnabled(security.isEntityOpPermitted(FileDescriptor.class, EntityOp.CREATE));
        multiUploadBtn.setAction(multiUploadAction);
    }
}