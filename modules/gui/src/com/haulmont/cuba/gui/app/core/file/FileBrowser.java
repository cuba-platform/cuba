/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FileBrowser extends AbstractWindow {

    @Inject
    protected Table filesTable;

    @Inject
    protected CollectionDatasource<FileDescriptor, UUID> filesDs;

    @Resource(name = "multiupload")
    protected Button uploadBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        filesTable.addAction(new ItemTrackingAction("download") {
            @Override
            public void actionPerform(Component component) {
                FileDescriptor fileDescriptor = filesTable.getSingleSelected();
                if (fileDescriptor != null) {
                    AppConfig.createExportDisplay(FileBrowser.this).show(fileDescriptor, null);
                }
            }
        });

        uploadBtn.setAction(new AbstractAction("multiupload") {
            @Override
            public void actionPerform(Component component) {
                Map<String, Object> params = Collections.<String, Object>emptyMap();

                final Window window = frame.openEditor("multiuploadDialog", null,
                        WindowManager.OpenType.DIALOG,
                        params, null);

                window.addListener(new Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId) && window instanceof Window.Editor) {
                            List<FileDescriptor> items = ((MultiUploader) window).getFiles();
                            for (FileDescriptor fdesc : items) {
                                filesDs.addItem(fdesc);
                            }
                            if (items.size() > 0)
                                filesDs.commit();
                            filesTable.refresh();
                        }
                    }
                });
            }
        });

//        TODO generated column with download link and formatted file size
//        FileDownloadHelper.initGeneratedColumn(filesTable);
    }
}