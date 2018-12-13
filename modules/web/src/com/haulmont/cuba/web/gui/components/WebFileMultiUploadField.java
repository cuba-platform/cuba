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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.widgets.CubaFileUpload;
import com.vaadin.ui.Component;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.cuba.gui.ComponentsHelper.getScreenContext;
import static com.haulmont.cuba.web.gui.FileUploadTypesHelper.convertToMIME;

public class WebFileMultiUploadField extends WebAbstractUploadComponent<CubaFileUpload>
        implements FileMultiUploadField, InitializingBean {

    protected final Map<UUID, String> files = new LinkedHashMap<>();

    protected FileUploadingAPI fileUploading;
    protected UUID tempFileId;
    protected String accept;

    public WebFileMultiUploadField() {
        component = createComponent();
    }

    @Inject
    public void setFileUploading(FileUploadingAPI fileUploading) {
        this.fileUploading = fileUploading;
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    protected void initComponent(CubaFileUpload impl) {
        impl.setMultiSelect(true);

        Messages messages = beanLocator.get(Messages.NAME);
        impl.setProgressWindowCaption(messages.getMainMessage("upload.uploadingProgressTitle"));
        impl.setUnableToUploadFileMessage(messages.getMainMessage("upload.unableToUploadFile"));
        impl.setCancelButtonCaption(messages.getMainMessage("upload.cancel"));
        impl.setCaption(messages.getMainMessage("upload.submit"));
        impl.setDropZonePrompt(messages.getMainMessage("upload.dropZonePrompt"));
        impl.setDescription(null);

        Configuration configuration = beanLocator.get(Configuration.NAME);
        int maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();
        int maxSizeBytes = maxUploadSizeMb * BYTES_IN_MEGABYTE;

        impl.setFileSizeLimit(maxSizeBytes);

        impl.setReceiver((fileName, MIMEType) -> {
            FileOutputStream outputStream;
            try {
                FileUploadingAPI.FileInfo fileInfo = fileUploading.createFile();
                tempFileId = fileInfo.getId();
                File tmpFile = fileInfo.getFile();
                outputStream = new FileOutputStream(tmpFile);
            } catch (Exception e) {
                throw new RuntimeException("Unable to receive file", e);
            }
            return outputStream;
        });

        impl.addStartedListener(event -> fireFileUploadStart(event.getFileName(), event.getContentLength()));

        impl.addQueueUploadFinishedListener(event -> fireQueueUploadComplete());

        impl.addSucceededListener(event -> {
            files.put(tempFileId, event.getFileName());

            fireFileUploadFinish(event.getFileName(), event.getContentLength());
        });
        impl.addFailedListener(event -> {
            try {
                // close and remove temp file
                fileUploading.deleteFile(tempFileId);
                tempFileId = null;
            } catch (Exception e) {
                if (e instanceof FileStorageException) {
                    FileStorageException fse = (FileStorageException) e;
                    if (fse.getType() != FileStorageException.Type.FILE_NOT_FOUND) {
                        LoggerFactory.getLogger(WebFileMultiUploadField.class)
                                .warn("Could not remove temp file {} after broken uploading", tempFileId);
                    }
                }
                LoggerFactory.getLogger(WebFileMultiUploadField.class)
                        .warn("Error while delete temp file {}", tempFileId);
            }

            fireFileUploadError(event.getFileName(), event.getContentLength(), event.getReason());
        });
        impl.addFileSizeLimitExceededListener(e -> {
            Notifications notifications = getScreenContext(this).getNotifications();

            notifications.create(NotificationType.WARNING)
                    .withCaption(
                            messages.formatMainMessage("multiupload.filesizeLimitExceed",
                                    e.getFileName(), getFileSizeLimitString())
                    )
                    .show();
        });
        impl.addFileExtensionNotAllowedListener(e -> {
            Notifications notifications = getScreenContext(this).getNotifications();

            notifications.create(NotificationType.WARNING)
                    .withCaption(messages.formatMainMessage("upload.fileIncorrectExtension.message", e.getFileName()))
                    .show();
        });
    }

    protected CubaFileUpload createComponent() {
        return new CubaFileUpload();
    }

    /**
     * Get uploads map
     *
     * @return Map (UUID - Id of file in FileUploadService, String - FileName )
     */
    @Override
    public Map<UUID, String> getUploadsMap() {
        return Collections.unmodifiableMap(files);
    }

    @Override
    public void clearUploads() {
        files.clear();
    }

    @Override
    public Subscription addQueueUploadCompleteListener(Consumer<QueueUploadCompleteEvent> listener) {
        return getEventHub().subscribe(QueueUploadCompleteEvent.class, listener);
    }

    @Override
    public void removeQueueUploadCompleteListener(Consumer<QueueUploadCompleteEvent> listener) {
        unsubscribe(QueueUploadCompleteEvent.class, listener);
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;

        if (!StringUtils.isEmpty(icon)) {
            component.setIcon(getIconResource(icon));
        } else {
            component.setIcon(null);
        }
    }

    @Override
    public String getAccept() {
        return accept;
    }

    @Override
    public void setAccept(String accept) {
        if (!Objects.equals(accept, getAccept())) {
            this.accept = accept;
            component.setAccept(convertToMIME(accept));
        }
    }

    @Override
    public void setDropZone(DropZone dropZone) {
        super.setDropZone(dropZone);

        if (dropZone == null) {
            component.setDropZone(null);
        } else {
            com.haulmont.cuba.gui.components.Component target = dropZone.getTarget();
            if (target instanceof Window.Wrapper) {
                target = ((Window.Wrapper) target).getWrappedWindow();
            }

            Component vComponent = target.unwrapComposition(Component.class);
            this.component.setDropZone(vComponent);
        }
    }

    @Override
    public void setPasteZone(ComponentContainer pasteZone) {
        super.setPasteZone(pasteZone);

        if (pasteZone == null) {
            component.setPasteZone(null);
        } else {
            Component vComponent = pasteZone.unwrapComposition(Component.class);
            component.setPasteZone(vComponent);
        }
    }

    @Override
    public void setDropZonePrompt(String dropZonePrompt) {
        super.setDropZonePrompt(dropZonePrompt);

        component.setDropZonePrompt(dropZonePrompt);
    }

    protected void fireFileUploadStart(String fileName, long contentLength) {
        FileUploadStartEvent event = new FileUploadStartEvent(this, fileName, contentLength);
        publish(FileUploadStartEvent.class, event);
    }

    protected void fireFileUploadFinish(String fileName, long contentLength) {
        FileUploadFinishEvent event = new FileUploadFinishEvent(this, fileName, contentLength);
        publish(FileUploadFinishEvent.class, event);
    }

    protected void fireFileUploadError(String fileName, long contentLength, Exception cause) {
        FileUploadErrorEvent event = new FileUploadErrorEvent(this, fileName, contentLength, cause);
        publish(FileUploadErrorEvent.class, event);
    }

    protected void fireQueueUploadComplete() {
        QueueUploadCompleteEvent event = new QueueUploadCompleteEvent(this);
        publish(QueueUploadCompleteEvent.class, event);
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;

        this.component.setFileSizeLimit(fileSizeLimit);
    }

    @Override
    public void setPermittedExtensions(Set<String> permittedExtensions) {
        if (permittedExtensions != null) {
            this.permittedExtensions = permittedExtensions.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        } else {
            this.permittedExtensions = null;
        }

        this.component.setPermittedExtensions(this.permittedExtensions);
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }
}