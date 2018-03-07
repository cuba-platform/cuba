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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.compatibility.MultiUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.gui.FileUploadTypesHelper;
import com.haulmont.cuba.web.widgets.CubaFileUpload;
import com.vaadin.ui.Component;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class WebFileMultiUploadField extends WebAbstractUploadComponent<CubaFileUpload>
        implements FileMultiUploadField {

    protected final Map<UUID, String> files = new LinkedHashMap<>();

    protected FileUploadingAPI fileUploading;
    protected UUID tempFileId;
    protected String accept;

    protected List<QueueUploadCompleteListener> queueUploadCompleteListeners; // lazily initialized list

    public WebFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);

        initComponent();
    }

    protected void initComponent() {
        CubaFileUpload impl = createComponent();
        impl.setMultiSelect(true);

        Messages messages = AppBeans.get(Messages.NAME);
        impl.setProgressWindowCaption(messages.getMainMessage("upload.uploadingProgressTitle"));
        impl.setUnableToUploadFileMessage(messages.getMainMessage("upload.unableToUploadFile"));
        impl.setCancelButtonCaption(messages.getMainMessage("upload.cancel"));
        impl.setCaption(messages.getMainMessage("upload.submit"));
        impl.setDropZonePrompt(messages.getMainMessage("upload.dropZonePrompt"));
        impl.setDescription(null);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        final int maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();
        final int maxSizeBytes = maxUploadSizeMb * BYTES_IN_MEGABYTE;

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
            String warningMsg = messages.formatMessage(WebFileMultiUploadField.class, "multiupload.filesizeLimitExceed", e.getFileName(), getFileSizeLimitString());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        });
        impl.addFileExtensionNotAllowedListener(e -> {
            String warningMsg = messages.formatMainMessage("upload.fileIncorrectExtension.message", e.getFileName());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        });

        component = impl;
    }

    protected CubaFileUpload createComponent() {
        return new CubaFileUpload();
    }

    @Override
    public void addListener(UploadListener listener) {
        MultiUploadFieldListenerWrapper wrapper = new MultiUploadFieldListenerWrapper(listener);

        addFileUploadStartListener(wrapper);
        addFileUploadFinishListener(wrapper);
        addFileUploadErrorListener(wrapper);
        addQueueUploadCompleteListener(wrapper);
    }

    @Override
    public void removeListener(UploadListener listener) {
        MultiUploadFieldListenerWrapper wrapper = new MultiUploadFieldListenerWrapper(listener);

        removeFileUploadStartListener(wrapper);
        removeFileUploadFinishListener(wrapper);
        removeFileUploadErrorListener(wrapper);
        removeQueueUploadCompleteListener(wrapper);
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
    public void setIcon(String icon) {
        this.icon = icon;

        if (component instanceof CubaFileUpload) {
            if (!StringUtils.isEmpty(icon)) {
                component.setIcon(AppBeans.get(IconResolver.class).getIconResource(icon));
            } else {
                component.setIcon(null);
            }
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
            component.setAccept(FileUploadTypesHelper.convertToMIME(accept));
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
    public void setPasteZone(Container pasteZone) {
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
        FileUploadStartEvent event = new FileUploadStartEvent(fileName, contentLength);
        getEventRouter().fireEvent(FileUploadStartListener.class, FileUploadStartListener::fileUploadStart, event);
    }

    protected void fireFileUploadFinish(String fileName, long contentLength) {
        FileUploadFinishEvent event = new FileUploadFinishEvent(fileName, contentLength);
        getEventRouter().fireEvent(FileUploadFinishListener.class, FileUploadFinishListener::fileUploadFinish, event);
    }

    protected void fireFileUploadError(String fileName, long contentLength, Exception cause) {
        FileUploadErrorEvent event = new FileUploadErrorEvent(fileName, contentLength, cause);
        getEventRouter().fireEvent(FileUploadErrorListener.class, FileUploadErrorListener::fileUploadError, event);
    }

    protected void fireQueueUploadComplete() {
        if (queueUploadCompleteListeners != null) {
            for (QueueUploadCompleteListener listener : new ArrayList<>(queueUploadCompleteListeners)) {
                listener.queueUploadComplete();
            }
        }
    }

    @Override
    public void addFileUploadStartListener(FileUploadStartListener listener) {
        getEventRouter().addListener(FileUploadStartListener.class, listener);
    }

    @Override
    public void removeFileUploadStartListener(FileUploadStartListener listener) {
        getEventRouter().removeListener(FileUploadStartListener.class, listener);
    }

    @Override
    public void addFileUploadFinishListener(FileUploadFinishListener listener) {
        getEventRouter().addListener(FileUploadFinishListener.class, listener);
    }

    @Override
    public void removeFileUploadFinishListener(FileUploadFinishListener listener) {
        getEventRouter().removeListener(FileUploadFinishListener.class, listener);
    }

    @Override
    public void addFileUploadErrorListener(FileUploadErrorListener listener) {
        getEventRouter().removeListener(FileUploadErrorListener.class, listener);
    }

    @Override
    public void removeFileUploadErrorListener(FileUploadErrorListener listener) {
        getEventRouter().removeListener(FileUploadErrorListener.class, listener);
    }

    @Override
    public void addQueueUploadCompleteListener(QueueUploadCompleteListener listener) {
        if (queueUploadCompleteListeners == null) {
            queueUploadCompleteListeners = new ArrayList<>();
        }
        if (!queueUploadCompleteListeners.contains(listener)) {
            queueUploadCompleteListeners.add(listener);
        }
    }

    @Override
    public void removeQueueUploadCompleteListener(QueueUploadCompleteListener listener) {
        if (queueUploadCompleteListeners != null) {
            queueUploadCompleteListeners.remove(listener);
        }
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;

        this.component.setFileSizeLimit(fileSizeLimit);

    }

    @Override
    public void setPermittedExtensions(Set<String> permittedExtensions) {
        if (permittedExtensions != null) {
            this.permittedExtensions = permittedExtensions.stream().map(String::toLowerCase).collect(Collectors.toSet());
        } else {
            this.permittedExtensions = null;
        }

        this.component.setPermittedExtensions(this.permittedExtensions);
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