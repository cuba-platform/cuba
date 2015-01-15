/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FileEditor extends AbstractEditor<FileDescriptor> {

    protected final Log log = LogFactory.getLog(getClass());

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
        super.init(params);

        getDialogParams().setWidthAuto();
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        boolean isNew = PersistenceHelper.isNew(fileDs.getItem());

        if (isNew) {
            okBtn.setEnabled(false);
            uploadField.addListener(new FileUploadListener());
            uploadField.requestFocus();
        } else {
            uploadField.setVisible(false);
            nameField.requestFocus();
        }
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed && needSave) {
            saveFile();
        }

        return super.postCommit(committed, close);
    }

    protected void saveFile() {
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        try {
            fileUploading.putFileIntoStorage(uploadField.getFileId(), fileDs.getItem());
        } catch (FileStorageException e) {
            showNotification(getMessage("fileEditor.unableToSaveFile"), NotificationType.ERROR);
            log.error("Unable to save file to middleware", e);
        }
    }

    protected class FileUploadListener extends FileUploadField.ListenerAdapter {

        @Override
        public void uploadSucceeded(Event event) {
            FileDescriptor fd = getItem();

            fd.setName(uploadField.getFileName());
            fd.setCreateDate(timeSource.currentTimestamp());
            fd.setExtension(FilenameUtils.getExtension(uploadField.getFileName()));

            FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
            File file = fileUploading.getFile(uploadField.getFileId());

            fd.setSize(file.length());

            okBtn.setEnabled(true);

            nameField.requestFocus();

            needSave = true;
        }
    }
}