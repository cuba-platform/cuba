/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.sendingmessage.browse;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.collections.CollectionUtils;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ovchinnikov
 * @version $Id$
 */
public class SendingMessageBrowser extends AbstractWindow {

    protected static final String CONTENT_TEXT = "contentText";

    @Inject
    protected CollectionDatasource<SendingMessage, UUID> sendingMessageDs;

    @Inject
    protected EmailService emailService;

    @Inject
    protected FieldGroup fg;

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected Table<SendingMessage> table;

    @Inject
    protected FileUploadingAPI fileUploading;

    @Inject
    protected DataSupplier dataSupplier;

    @Override
    public void init(Map<String, Object> params) {
        fg.addCustomField(CONTENT_TEXT, new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                TextArea contentTextArea = factory.createComponent(TextArea.class);
                contentTextArea.setRows(20);
                contentTextArea.setHeight(themeConstants.get("cuba.gui.SendingMessageBrowser.contentTextArea.height"));
                return contentTextArea;
            }
        });
        fg.setEditable(CONTENT_TEXT, false);

        sendingMessageDs.addItemChangeListener(e -> selectedItemChanged(e.getItem()));
    }

    protected void selectedItemChanged(SendingMessage item) {
        String contentText = null;
        if (item != null) {
            contentText = emailService.loadContentText(item);
        }
        fg.setEditable(CONTENT_TEXT, true);
        fg.setFieldValue(CONTENT_TEXT, contentText);
        fg.setEditable(CONTENT_TEXT, false);
    }

    public void download() {
        SendingMessage message = table.getSingleSelected();
        if (message != null) {
            List<SendingAttachment> attachments = getAttachments(message);
            if (CollectionUtils.isNotEmpty(attachments)) {
                if (attachments.size() == 1) {
                    exportFile(attachments.get(0));
                } else {
                    selectAttachmentDialog(message);
                }
            } else {
                showNotification(messages.getMessage(getClass(), "sendingMessage.noAttachments"), NotificationType.HUMANIZED);
            }
        }
    }

    protected void selectAttachmentDialog(SendingMessage message) {
        openLookup("sys$SendingMessage.attachments",
                new Lookup.Handler() {
                    @Override
                    public void handleLookup(Collection items) {
                        if (items.size() == 1) {
                            exportFile((SendingAttachment) CollectionUtils.get(items, 0));
                        }
                    }
                },
                WindowManager.OpenType.DIALOG,
                ParamsMap.of("message", message));
    }

    protected List<SendingAttachment> getAttachments(SendingMessage message) {
        return dataSupplier.reload(message, "sendingMessage.loadFromQueue").getAttachments();
    }

    protected FileDescriptor getFileDescriptor(SendingAttachment attachment) throws FileStorageException {
        UUID uuid = fileUploading.saveFile(attachment.getContent());
        FileDescriptor fileDescriptor = fileUploading.getFileDescriptor(uuid, attachment.getName());
        fileUploading.putFileIntoStorage(uuid, fileDescriptor);
        return dataSupplier.commit(fileDescriptor);
    }

    protected void exportFile(SendingAttachment attachment) {
        try {
            FileDescriptor fileDescriptor = getFileDescriptor(attachment);
            AppConfig.createExportDisplay(this).show(fileDescriptor, ExportFormat.OCTET_STREAM);
        } catch (FileStorageException e) {
            throw new RuntimeException("File export filed", e);
        }
    }
}