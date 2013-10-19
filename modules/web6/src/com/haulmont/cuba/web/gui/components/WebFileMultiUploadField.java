/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ValueProvider;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.toolkit.ui.MultiUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebFileMultiUploadField extends WebAbstractComponent<MultiUpload> implements FileMultiUploadField {

    private static final Log log = LogFactory.getLog(WebFileMultiUploadField.class);

    protected Messages messages;

    private List<UploadListener> listeners = new ArrayList<>();

    private Map<UUID, String> files = new HashMap<>();

    private String description = "";

    // Client control parameters
    private ValueProvider componentParams = new ValueProvider() {
        Map<String, Object> params = new HashMap<>();

        @Override
        public Map<String, Object> getValues() {
            return params;
        }

        @Override
        public Map<String, Object> getParameters() {
            return params;
        }
    };

    public WebFileMultiUploadField() {
        messages = AppBeans.get(Messages.class);

        String caption = messages.getMessage(AppConfig.getMessagesPack(), "Upload");
        MultiUpload uploader = new MultiUpload(caption);

        componentParams.getParameters().put("caption", "");
        componentParams.getParameters().put("fileSizeLimit",
                AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getMaxUploadSizeMb().toString() + " MB");

        uploader.setValueProvider(componentParams);
        uploader.setWidth("90px");
        uploader.setHeight("25px");
        setExpandable(false);
        //Add listeners
        uploader.addListener(new MultiUpload.FileUploadStartListener() {
            // On file upload start 
            public void fileUploadStart(String fileName) {
                for (UploadListener listener : listeners)
                    listener.fileUploadStart(fileName);
            }
        });
        uploader.addListener(new MultiUpload.FileUploadCompleteListener() {
            // On file uploaded
            public void fileUploaded(String fileName, UUID uuid) {
                files.put(uuid, fileName);
                for (UploadListener listener : listeners)
                    listener.fileUploaded(fileName);
            }
        });
        uploader.addListener(new MultiUpload.QueueCompleteListener() {
            // On queue complete
            public void queueUploadComplete() {
                for (UploadListener listener : listeners)
                    listener.queueUploadComplete();
            }
        });
        uploader.addListener(new MultiUpload.FileErrorHandler(){
            // On upload error
            public void errorNotify(String fileName, String message, int errorCode) {
                log.warn(String.format("Error while uploading file '%s' with code '%s': %s", fileName, errorCode, message));

                WebWindowManager wm = App.getInstance().getWindowManager();
                switch (MultiUpload.UploadErrorType.fromId(errorCode)) {
                    case QUEUE_LIMIT_EXCEEDED:
                        wm.showNotification(messages.getMessage(WebFileMultiUploadField.class,
                                "multiupload.queueLimitExceed"),
                                IFrame.NotificationType.WARNING);
                        break;
                    case FILE_EXCEEDS_SIZE_LIMIT:

                        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
                        final Integer maxUploadSizeMb = clientConfig.getMaxUploadSizeMb();

                        wm.showNotification(messages.formatMessage(WebFileMultiUploadField.class,
                                "multiupload.filesizeLimitExceed", fileName, maxUploadSizeMb),
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
                            String uploadError = messages.formatMessage(WebFileMultiUploadField.class,
                                    "multiupload.uploadError", fileName);
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
        if (componentParams.getParameters().containsKey("caption"))
            return (String) componentParams.getParameters().get("caption");
        else
            return "";
    }

    @Override
    public void setCaption(String caption) {
        this.componentParams.getParameters().put("caption", caption);
        component.requestRepaint();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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

    public ValueProvider getComponentParameters() {
        return componentParams;
    }
}