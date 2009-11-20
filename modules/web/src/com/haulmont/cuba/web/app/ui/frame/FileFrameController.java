/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 20.11.2009 14:10:02
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.frame;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import java.util.Date;
import java.util.Map;

public class FileFrameController extends AbstractWindow {

    private CollectionDatasource ds;

    private TextField nameText;

    private com.haulmont.cuba.gui.components.Label extLabel;

    private com.haulmont.cuba.gui.components.Label sizeLab;

    private com.haulmont.cuba.gui.components.Label createDateLab;

    private FileUploadField uploadField;

    private FileDescriptor fd;

    public FileFrameController(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        uploadField = getComponent("uploadField");
        nameText = getComponent("name");
        extLabel = getComponent("extension");
        sizeLab = getComponent("size");
        createDateLab = getComponent("createDate");
        Table filesTable = getComponent("files");

        ds = filesTable.getDatasource();
        MetaPropertyPath nameProperty = ds.getMetaClass().getPropertyEx("name");
        final com.vaadin.ui.Table table = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(filesTable);

        table.addGeneratedColumn(nameProperty, new com.vaadin.ui.Table.ColumnGenerator() {
            public Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                final FileDescriptor fd = (FileDescriptor) ds.getItem(itemId);

                String link = FileDownloadHelper.makeLink(fd, true, false);
                Label label = new Label(link);
                label.setContentMode(Label.CONTENT_XHTML);

                return label;
            }
        });

        uploadField.addListener(new FileUploadField.Listener() {
            public void uploadStarted(Event event) {
                uploadField.setEnabled(false);
            }

            public void uploadFinished(Event event) {
                uploadField.setEnabled(true);
            }

            public void uploadSucceeded(Event event) {
                nameText.setValue(uploadField.getFileName());
                String value = FileDownloadHelper.getFileExt(uploadField.getFileName());
                extLabel.setValue(value);
                sizeLab.setValue(uploadField.getBytes().length);
                Date date = TimeProvider.currentTimestamp();
                createDateLab.setValue(date);

                fd = new FileDescriptor();
                fd.setName(uploadField.getFileName());
                fd.setExtension(value);
                fd.setSize(uploadField.getBytes().length);
                fd.setCreateDate(date);
                saveFile();
                ds.addItem(fd);
                showNotification(MessageProvider.getMessage(getClass(), "uploadSuccess"), NotificationType.HUMANIZED);
            }

            public void uploadFailed(Event event) {
                showNotification(MessageProvider.getMessage(getClass(), "uploadUnsuccess"), NotificationType.HUMANIZED);
            }

            public void updateProgress(long readBytes, long contentLength) {
            }
        });
    }

    private void saveFile() {
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.JNDI_NAME);
        try {
            fss.saveFile(fd, uploadField.getBytes());
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }
}
