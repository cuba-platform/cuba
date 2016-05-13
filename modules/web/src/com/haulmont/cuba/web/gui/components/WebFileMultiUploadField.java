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
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.compatibility.MultiUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.toolkit.FileUploadTypesHelper;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaFileUpload;
import com.haulmont.cuba.web.toolkit.ui.CubaMultiUpload;
import com.haulmont.cuba.web.toolkit.ui.UploadComponent;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 */
public class WebFileMultiUploadField extends WebAbstractUploadComponent<UploadComponent> implements FileMultiUploadField {

    private final Logger log = LoggerFactory.getLogger(WebFileMultiUploadField.class);

    protected final Map<UUID, String> files = new HashMap<>();
    protected FileUploadingAPI fileUploading;
    protected UUID tempFileId;
    protected String accept;

    protected List<FileUploadStartListener> fileUploadStartListeners;         // lazily initialized list
    protected List<FileUploadFinishListener> fileUploadFinishListeners;       // lazily initialized list
    protected List<FileUploadErrorListener> fileUploadErrorListeners;         // lazily initialized list
    protected List<QueueUploadCompleteListener> queueUploadCompleteListeners; // lazily initialized list

    public WebFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);

        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        if ((webBrowser.isIE() && !webBrowser.isEdge()) && webBrowser.getBrowserMajorVersion() < 10) {
            initOldComponent();
        } else {
            initComponent();
        }
    }

    protected void initOldComponent() {
        CubaMultiUpload impl = createOldComponent();

        ThemeConstants theme = App.getInstance().getThemeConstants();

        String width = theme.get("cuba.web.WebFileMultiUploadField.upload.width");
        String height = theme.get("cuba.web.WebFileMultiUploadField.upload.height");

        impl.setWidth(width);
        impl.setHeight(height);

        int buttonTextLeft = theme.getInt("cuba.web.WebFileMultiUploadField.buttonText.left");
        int buttonTextTop = theme.getInt("cuba.web.WebFileMultiUploadField.buttonText.top");

        impl.setButtonTextLeft(buttonTextLeft);
        impl.setButtonTextTop(buttonTextTop);

        impl.setButtonWidth(Integer.parseInt(width.replace("px", "")));
        impl.setButtonHeight(Integer.parseInt(height.replace("px", "")));

        Messages messages = AppBeans.get(Messages.NAME);
        impl.setCaption(messages.getMessage(AppConfig.getMessagesPack(), "multiupload.submit"));

        Configuration configuration = AppBeans.get(Configuration.NAME);
        impl.setFileSizeLimitMB(configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb());

        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        if (!webConfig.getUseFontIcons()) {
            impl.setButtonImage(new VersionedThemeResource("components/multiupload/images/multiupload-button.png"));
        } else {
            impl.setButtonImage(new VersionedThemeResource("components/multiupload/images/multiupload-button-font-icon.png"));
        }

        impl.setButtonStyles(theme.get("cuba.web.WebFileMultiUploadField.button.style"));
        impl.setButtonDisabledStyles(theme.get("cuba.web.WebFileMultiUploadField.button.disabled.style"));

        impl.setBootstrapFailureHandler(new CubaMultiUpload.BootstrapFailureHandler() {
            @Override
            public void loadWebResourcesFailed() {
                Messages messages = AppBeans.get(Messages.NAME);
                String resourcesLoadFailed = messages.getMessage(WebFileMultiUploadField.class, "multiupload.resources.notLoaded");
                WebWindowManager wm = App.getInstance().getWindowManager();
                wm.showNotification(resourcesLoadFailed, Frame.NotificationType.ERROR);
            }

            @Override
            public void flashNotInstalled() {
                Messages messages = AppBeans.get(Messages.NAME);
                String swfNotSupported = messages.getMessage(WebFileMultiUploadField.class, "multiupload.resources.swfNotSupported");
                WebWindowManager wm = App.getInstance().getWindowManager();
                wm.showNotification(swfNotSupported, Frame.NotificationType.ERROR);
            }
        });

        impl.setReceiver((filename, mimeType) -> {
            FileOutputStream outputStream;
            try {
                FileUploadingAPI.FileInfo fileInfo = fileUploading.createFile();
                tempFileId = fileInfo.getId();
                File tmpFile = fileInfo.getFile();
                outputStream = new FileOutputStream(tmpFile);
            } catch (Exception e) {
                throw new RuntimeException("Unable to open stream for file uploading", e);
            }
            return outputStream;
        });

        setExpandable(false);

        impl.addUploadListener(new CubaMultiUpload.UploadListener() {
            @Override
            public void fileUploadStart(String fileName, long contentLength) {
                fireFileUploadStart(fileName, contentLength);
            }

            @Override
            public void fileUploaded(String fileName, long contentLength) {
                files.put(tempFileId, fileName);

                fireFileUploadFinish(fileName, contentLength);
            }

            @Override
            public void queueUploadComplete() {
                fireQueueUploadComplete();
            }

            @Override
            public void errorNotify(String fileName, String message, CubaMultiUpload.UploadErrorType errorCode, long contentLength) {
                log.warn(String.format("Error while uploading file '%s' with code '%s': %s", fileName, errorCode.getId(), message));

                Messages messages = AppBeans.get(Messages.NAME);
                WebWindowManager wm = App.getInstance().getWindowManager();
                switch (errorCode) {
                    case QUEUE_LIMIT_EXCEEDED:
                        wm.showNotification(messages.getMessage(WebFileMultiUploadField.class, "multiupload.queueLimitExceed"),
                                Frame.NotificationType.WARNING);
                        break;
                    case INVALID_FILETYPE:
                        String invalidFiletypeMsg = messages.formatMainMessage("upload.fileIncorrectExtension.message", fileName);
                        wm.showNotification(invalidFiletypeMsg, Frame.NotificationType.WARNING);
                        break;
                    case FILE_EXCEEDS_SIZE_LIMIT:
                        String warningMsg = messages.formatMessage(WebFileMultiUploadField.class, "multiupload.filesizeLimitExceed", fileName, getFileSizeLimitString());
                        wm.showNotification(warningMsg, Frame.NotificationType.WARNING);
                        break;
                    case SECURITY_ERROR:
                        wm.showNotification(messages.getMessage(WebFileMultiUploadField.class, "multiupload.securityError"),
                                Frame.NotificationType.WARNING);
                        break;
                    case ZERO_BYTE_FILE:
                        wm.showNotification(messages.formatMessage(WebFileMultiUploadField.class, "multiupload.zerobyteFile", fileName),
                                Frame.NotificationType.WARNING);
                        break;
                    default:
                        String uploadError = messages.formatMessage(WebFileMultiUploadField.class, "multiupload.uploadError", fileName);
                        wm.showNotification(uploadError, Frame.NotificationType.ERROR);

                        fireFileUploadError(fileName, contentLength, new IOException("Upload error " + errorCode.name()));

                        break;

                }
            }
        });
        impl.setDescription(null);

        component = impl;
    }

    protected void initComponent() {
        CubaFileUpload impl = createComponent();
        impl.setMultiSelect(true);

        Messages messages = AppBeans.get(Messages.NAME);
        impl.setProgressWindowCaption(messages.getMainMessage("upload.uploadingProgressTitle"));
        impl.setUnableToUploadFileMessage(messages.getMainMessage("upload.unableToUploadFile"));
        impl.setCancelButtonCaption(messages.getMainMessage("upload.cancel"));
        impl.setCaption(messages.getMainMessage("upload.submit"));
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
                        log.warn(String.format("Could not remove temp file %s after broken uploading", tempFileId));
                    }
                }
                log.warn(String.format("Error while delete temp file %s", tempFileId));
            }

            fireFileUploadError(event.getFileName(), event.getContentLength(), event.getReason());
        });
        impl.addFileSizeLimitExceededListener(e -> {
            String warningMsg = messages.formatMessage(WebFileMultiUploadField.class, "multiupload.filesizeLimitExceed", e.getFileName(), getFileSizeLimitString());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        });
        impl.addFileExtensionNotAllowedListener(e ->{
            String warningMsg = messages.formatMainMessage("upload.fileIncorrectExtension.message", e.getFileName());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        });

        component = impl;
    }

    protected CubaFileUpload createComponent() {
        return new CubaFileUpload();
    }

    protected CubaMultiUpload createOldComponent() {
        return new CubaMultiUpload();
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Deprecated
    public int getButtonWidth() {
        return ((CubaMultiUpload) component).getButtonWidth();
    }

    @Deprecated
    public void setButtonWidth(int buttonWidth) {
        ((CubaMultiUpload) component).setButtonWidth(buttonWidth);
    }

    @Deprecated
    public int getButtonHeight() {
        return ((CubaMultiUpload) component).getButtonHeight();
    }

    @Deprecated
    public void setButtonHeight(int buttonHeight) {
        ((CubaMultiUpload) component).setButtonHeight(buttonHeight);
    }

    @Deprecated
    public String getFileTypesMask() {
        return ((CubaMultiUpload) component).getFileTypesMask();
    }

    @Deprecated
    public void setFileTypesMask(String fileTypesMask) {
        ((CubaMultiUpload) component).setFileTypesMask(fileTypesMask);
    }

    @Deprecated
    public String getFileTypesDescription() {
        return ((CubaMultiUpload) component).getFileTypesDescription();
    }

    @Deprecated
    public void setFileTypesDescription(String fileTypesDescription) {
        ((CubaMultiUpload) component).setFileTypesDescription(fileTypesDescription);
    }

    @Deprecated
    public double getFileSizeLimitMB() {
        return ((CubaMultiUpload) component).getFileSizeLimitMB();
    }

    @Deprecated
    public void setFileSizeLimitMB(double filesizeLimit) {
        ((CubaMultiUpload) component).setFileSizeLimitMB(filesizeLimit);
    }

    @Deprecated
    public double getQueueUploadLimitMB() {
        return ((CubaMultiUpload) component).getQueueUploadLimitMB();
    }

    @Deprecated
    public void setQueueUploadLimitMB(double queueUploadLimit) {
        ((CubaMultiUpload) component).setQueueUploadLimitMB(queueUploadLimit);
    }

    @Deprecated
    public int getQueueSizeLimit() {
        return ((CubaMultiUpload) component).getQueueSizeLimit();
    }

    @Deprecated
    public void setQueueSizeLimit(int queueSizeLimit) {
        ((CubaMultiUpload) component).setQueueSizeLimit(queueSizeLimit);
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
                component.setIcon(WebComponentsHelper.getIcon(icon));
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
        if (!StringUtils.equals(accept, getAccept())) {
            this.accept = accept;
            component.setAccept(component instanceof CubaMultiUpload
                    ? FileUploadTypesHelper.convertSeparator(accept, ";")
                    : FileUploadTypesHelper.convertToMIME(accept));
        }
    }

    protected void fireFileUploadStart(String fileName, long contentLength) {
        if (fileUploadStartListeners != null && !fileUploadStartListeners.isEmpty()) {
            FileUploadStartEvent e = new FileUploadStartEvent(fileName, contentLength);
            for (FileUploadStartListener listener : new ArrayList<>(fileUploadStartListeners)) {
                listener.fileUploadStart(e);
            }
        }
    }

    protected void fireFileUploadFinish(String fileName, long contentLength) {
        if (fileUploadFinishListeners != null && !fileUploadFinishListeners.isEmpty()) {
            FileUploadFinishEvent e = new FileUploadFinishEvent(fileName, contentLength);
            for (FileUploadFinishListener listener : new ArrayList<>(fileUploadFinishListeners)) {
                listener.fileUploadFinish(e);
            }
        }
    }

    protected void fireFileUploadError(String fileName, long contentLength, Exception cause) {
        if (fileUploadErrorListeners != null && !fileUploadErrorListeners.isEmpty()) {
            FileUploadErrorEvent e = new FileUploadErrorEvent(fileName, contentLength, cause);
            for (FileUploadErrorListener listener : new ArrayList<>(fileUploadErrorListeners)) {
                listener.fileUploadError(e);
            }
        }
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
        if (fileUploadStartListeners == null) {
            fileUploadStartListeners = new ArrayList<>();
        }
        if (!fileUploadStartListeners.contains(listener)) {
            fileUploadStartListeners.add(listener);
        }
    }

    @Override
    public void removeFileUploadStartListener(FileUploadStartListener listener) {
        if (fileUploadStartListeners != null) {
            fileUploadStartListeners.remove(listener);
        }
    }

    @Override
    public void addFileUploadFinishListener(FileUploadFinishListener listener) {
        if (fileUploadFinishListeners == null) {
            fileUploadFinishListeners = new ArrayList<>();
        }
        if (!fileUploadFinishListeners.contains(listener)) {
            fileUploadFinishListeners.add(listener);
        }
    }

    @Override
    public void removeFileUploadFinishListener(FileUploadFinishListener listener) {
        if (fileUploadFinishListeners != null) {
            fileUploadFinishListeners.remove(listener);
        }
    }

    @Override
    public void addFileUploadErrorListener(FileUploadErrorListener listener) {
        if (fileUploadErrorListeners == null) {
            fileUploadErrorListeners = new ArrayList<>();
        }
        if (!fileUploadErrorListeners.isEmpty()) {
            fileUploadErrorListeners.add(listener);
        }
    }

    @Override
    public void removeFileUploadErrorListener(FileUploadErrorListener listener) {
        if (fileUploadErrorListeners != null) {
            fileUploadErrorListeners.remove(listener);
        }
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
        if (this.component instanceof CubaFileUpload){
            ((CubaFileUpload) this.component).setFileSizeLimit(fileSizeLimit);
        } else if (this.component instanceof CubaMultiUpload) {
            ((CubaMultiUpload) this.component).setFileSizeLimitMB((double) fileSizeLimit/BYTES_IN_MEGABYTE);
        }
    }

    @Override
    public void setPermittedExtensions(Set<String> permittedExtensions) {
        this.permittedExtensions = permittedExtensions;
        if (this.component instanceof CubaFileUpload){
            ((CubaFileUpload) this.component).setPermittedExtensions(permittedExtensions);
        } else if (this.component instanceof CubaMultiUpload) {
            ((CubaMultiUpload) this.component).setPermittedExtensions(permittedExtensions);
            ((CubaMultiUpload) this.component).setFileTypesDescription("");
        }
    }
}