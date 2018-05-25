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

package com.haulmont.cuba.gui.app.core.sendingmessage.browse;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.SendingAttachment;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.export.FileDataProvider;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.haulmont.cuba.gui.WindowManager.OpenType;

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

    @Inject
    protected ExportDisplay exportDisplay;

    @Named("fg.bodyContentType")
    protected TextField bodyContentTypeField;

    @Inject
    protected FileLoader fileLoader;

    protected Button showContentButton;
    protected TextArea contentTextArea;

    @Override
    public void init(Map<String, Object> params) {
        fg.addCustomField(CONTENT_TEXT, new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                VBoxLayout contentArea = factory.createComponent(VBoxLayout.class);
                contentArea.setSpacing(true);

                contentTextArea = factory.createComponent(TextArea.class);
                contentTextArea.setWidth("100%");
                contentTextArea.setEditable(false);
                contentTextArea.setHeight(themeConstants.get("cuba.gui.SendingMessageBrowser.contentTextArea.height"));

                showContentButton = factory.createComponent(Button.class);
                showContentButton.setAction(new AbstractAction("") {
                    @Override
                    public void actionPerform(Component component) {
                        String textAreaValue = contentTextArea.getValue();
                        if (textAreaValue != null) {
                            ByteArrayDataProvider dataProvider = new ByteArrayDataProvider(textAreaValue.getBytes(StandardCharsets.UTF_8));

                            String type = bodyContentTypeField.getRawValue();
                            if (StringUtils.containsIgnoreCase(type, ExportFormat.HTML.getContentType())) {
                                exportDisplay.show(dataProvider, "email-preview.html", ExportFormat.HTML);
                            } else {
                                exportDisplay.show(dataProvider, "email-preview.txt", ExportFormat.TEXT);
                            }
                        }
                    }
                });
                showContentButton.setEnabled(false);
                showContentButton.setCaption(messages.getMessage(getClass(), "sendingMessage.showContent"));

                contentArea.add(contentTextArea);
                contentArea.add(showContentButton);

                return contentArea;
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

        if (StringUtils.isNotEmpty(contentText)) {
            showContentButton.setEnabled(true);
        } else {
            showContentButton.setEnabled(false);
        }

        contentTextArea.setValue(contentText);
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
        openLookup("sys$SendingMessage.attachments", items -> {
            if (items.size() == 1) {
                exportFile((SendingAttachment) IterableUtils.get(items, 0));
            }
        }, OpenType.DIALOG, ParamsMap.of("message", message));
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
            FileDescriptor fd;

            if (emailService.isFileStorageUsed()
                    && attachment.getContentFile() != null
                    && fileLoader.fileExists(attachment.getContentFile())) {
                fd = attachment.getContentFile();
            } else {
                fd = getFileDescriptor(attachment);
            }

            AppConfig.createExportDisplay(this)
                    .show(new FileDataProvider(fd), fd.getName(), ExportFormat.OCTET_STREAM);
        } catch (FileStorageException e) {
            throw new RuntimeException("File export failed", e);
        }
    }
}