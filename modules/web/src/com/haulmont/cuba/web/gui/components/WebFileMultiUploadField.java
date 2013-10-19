/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaMultiUpload;
import com.vaadin.server.Sizeable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebFileMultiUploadField extends WebAbstractComponent<CubaMultiUpload> implements FileMultiUploadField {

    private static final Log log = LogFactory.getLog(WebFileMultiUploadField.class);

    protected FileUploadingAPI fileUploading;
    protected Messages messages;

    protected final List<UploadListener> listeners = new LinkedList<>();

    protected final Map<UUID, String> files = new HashMap<>();

    protected String fileName;

    protected UUID tempFileId;

    public WebFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        messages = AppBeans.get(Messages.class);

        CubaMultiUpload uploader = new CubaMultiUpload();

        uploader.setWidth(90, Sizeable.Unit.PIXELS);
        uploader.setHeight(25, Sizeable.Unit.PIXELS);

        uploader.setButtonWidth(90);
        uploader.setButtonHeight(25);

        uploader.setCaption(messages.getMessage(AppConfig.getMessagesPack(), "multiupload.submit"));
        uploader.setFileSizeLimitMB(AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getMaxUploadSizeMb());

        uploader.setButtonImage(new VersionedThemeResource("components/multiupload/images/multiupload-button.png"));
        uploader.setBootstrapFailureHandler(new CubaMultiUpload.BootstrapFailureHandler() {
            @Override
            public void loadWebResourcesFailed() {
                String resourcesLoadFailed = messages.getMessage(WebFileMultiUploadField.class, "multiupload.resources.notLoaded");
                App.getInstance().getWindowManager().showNotification(resourcesLoadFailed, IFrame.NotificationType.ERROR);
            }

            @Override
            public void flashNotInstalled() {
                String swfNotSupported = messages.getMessage(WebFileMultiUploadField.class, "multiupload.resources.swfNotSupported");
                App.getInstance().getWindowManager().showNotification(swfNotSupported, IFrame.NotificationType.ERROR);
            }
        });

        uploader.setReceiver(new CubaMultiUpload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                FileOutputStream outputStream;
                fileName = filename;
                try {
                    tempFileId = fileUploading.createEmptyFile();
                    File tmpFile = fileUploading.getFile(tempFileId);
                    outputStream = new FileOutputStream(tmpFile);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return outputStream;
            }
        });

        setExpandable(false);

        uploader.addUploadListener(new CubaMultiUpload.UploadListener() {
            @Override
            public void fileUploadStart(String fileName) {
                for (UploadListener listener : listeners)
                    listener.fileUploadStart(fileName);
            }

            @Override
            public void fileUploaded(String fileName) {
                files.put(tempFileId, fileName);

                for (UploadListener listener : listeners)
                    listener.fileUploaded(fileName);
            }

            @Override
            public void queueUploadComplete() {
                for (UploadListener listener : listeners)
                    listener.queueUploadComplete();
            }

            @Override
            public void errorNotify(String fileName, String message, CubaMultiUpload.UploadErrorType errorCode) {
                log.warn(String.format("Error while uploading file '%s' with code '%s': %s", fileName, errorCode.getId(), message));

                WebWindowManager wm = App.getInstance().getWindowManager();
                switch (errorCode) {
                    case QUEUE_LIMIT_EXCEEDED:
                        wm.showNotification(messages.getMessage(WebFileMultiUploadField.class, "multiupload.queueLimitExceed"),
                                IFrame.NotificationType.WARNING);
                        break;
                    case FILE_EXCEEDS_SIZE_LIMIT:

                        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
                        final Integer maxUploadSizeMb = clientConfig.getMaxUploadSizeMb();

                        wm.showNotification(messages.formatMessage(WebFileMultiUploadField.class, "multiupload.filesizeLimitExceed", fileName, maxUploadSizeMb),
                                IFrame.NotificationType.WARNING);
                        break;
                    case SECURITY_ERROR:
                        wm.showNotification(messages.getMessage(WebFileMultiUploadField.class, "multiupload.securityError"),
                                IFrame.NotificationType.WARNING);
                        break;
                    case ZERO_BYTE_FILE:
                        wm.showNotification(messages.formatMessage(WebFileMultiUploadField.class, "multiupload.zerobyteFile", fileName),
                                IFrame.NotificationType.WARNING);
                        break;
                    default:
                        boolean handled = false;
                        for (UploadListener listener : listeners)
                            handled = handled | listener.uploadError(fileName);
                        if (!handled) {
                            String uploadError = messages.formatMessage(WebFileMultiUploadField.class, "multiupload.uploadError", fileName);
                            wm.showNotification(uploadError, IFrame.NotificationType.ERROR);
                        }
                        break;

                }
            }
        });

        component = uploader;
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

    @Override
    public void setEnabled(boolean enabled) {
        component.setButtonEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return component.isButtonEnabled();
    }

    public int getButtonWidth() {
        return component.getButtonWidth();
    }

    public void setButtonWidth(int buttonWidth) {
        component.setButtonWidth(buttonWidth);
    }

    public void setButtonHeight(int buttonHeight) {
        component.setButtonHeight(buttonHeight);
    }

    public int getButtonHeight() {
        return component.getButtonHeight();
    }

    public String getFileTypesMask() {
        return component.getFileTypesMask();
    }

    public void setFileTypesMask(String fileTypesMask) {
        component.setFileTypesMask(fileTypesMask);
    }

    public String getFileTypesDescription() {
        return component.getFileTypesDescription();
    }

    public void setFileTypesDescription(String fileTypesDescription) {
        component.setFileTypesDescription(fileTypesDescription);
    }

    public double getFileSizeLimitMB() {
        return component.getFileSizeLimitMB();
    }

    public void setFileSizeLimitMB(double filesizeLimit) {
        component.setFileSizeLimitMB(filesizeLimit);
    }

    public double getQueueUploadLimitMB() {
        return component.getQueueUploadLimitMB();
    }

    public void setQueueUploadLimitMB(double queueUploadLimit) {
        component.setQueueUploadLimitMB(queueUploadLimit);
    }

    public int getQueueSizeLimit() {
        return component.getQueueSizeLimit();
    }

    public void setQueueSizeLimit(int queueSizeLimit) {
        component.setQueueSizeLimit(queueSizeLimit);
    }

    @Override
    public void addListener(UploadListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    @Override
    public void removeListener(UploadListener listener) {
        listeners.remove(listener);
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
}