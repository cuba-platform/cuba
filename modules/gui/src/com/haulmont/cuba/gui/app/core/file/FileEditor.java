/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FileEditor extends AbstractEditor<FileDescriptor> {

    @Inject
    protected Datasource<FileDescriptor> fileDs;

    @Resource(name = "windowActions.windowCommit")
    protected Button okBtn;

    @Inject
    protected TextField nameField;

    @Inject
    protected Label extLabel;

    @Inject
    protected Label sizeLabel;

    @Inject
    protected Label createDateLabel;

    @Inject
    protected FileUploadField uploadField;

    protected boolean needSave;

    @Inject
    protected TimeSource timeSource;

    @Override
    public void init(Map<String, Object> params) {
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        boolean isNew = PersistenceHelper.isNew(fileDs.getItem());

        if (isNew) {
            okBtn.setEnabled(false);
            uploadField.addListener(new FileUploadListener());
        } else {
            uploadField.setEnabled(false);
        }
    }

    @Override
    public void commitAndClose() {
        if (needSave) {
            saveFile();
        }
        super.commitAndClose();
    }

    private void saveFile() {
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        try {
            fileUploading.putFileIntoStorage(uploadField.getFileId(), fileDs.getItem());
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    private class FileUploadListener extends FileUploadField.ListenerAdapter {
        @Override
        public void uploadSucceeded(Event event) {
            getItem().setName(uploadField.getFileName());
            getItem().setCreateDate(timeSource.currentTimestamp());
            getItem().setExtension(FilenameUtils.getExtension(uploadField.getFileName()));

            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
            File file = fileUploading.getFile(uploadField.getFileId());
            Integer size = (int) file.length();

            getItem().setSize(size);

            okBtn.setEnabled(true);

            needSave = true;
        }
    }
}
