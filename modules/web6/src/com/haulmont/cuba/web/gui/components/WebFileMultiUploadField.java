/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 17.11.2010 18:05:20
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.ValueProvider;
import com.haulmont.cuba.web.toolkit.ui.MultiUpload;

import java.util.*;

public class WebFileMultiUploadField extends
        WebAbstractComponent<MultiUpload>
        implements
        FileMultiUploadField {

    private List<UploadListener> listeners = new ArrayList<UploadListener>();

    private Map<UUID, String> files = new HashMap<UUID, String>();

    private String description = "";

    // Client control parameters
    private ValueProvider componentParams = new ValueProvider() {
        Map<String, Object> params = new HashMap<String, Object>();

        public Map<String, Object> getValues() {
            return params;
        }

        public Map<String, Object> getParameters() {
            return params;
        }
    };

    public WebFileMultiUploadField() {
        String caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "Upload");
        MultiUpload uploader = new MultiUpload(caption);

        componentParams.getParameters().put("caption", "");
        componentParams.getParameters().put("fileSizeLimit",
                ConfigProvider.getConfig(ClientConfig.class).getMaxUploadSizeMb().toString() + " MB");

        uploader.setValueProvider(componentParams);
        uploader.setWidth("90px");
        uploader.setHeight("25px");
        setExpandable(false);
        //Add listeners
        uploader.addListener(new MultiUpload.FileProgressListener() {
            // On file uploading
            public void progressChanged(String fileName, int receivedBytes, int contentLength) {
                for (UploadListener listener : listeners)
                    listener.progressChanged(fileName, receivedBytes, contentLength);
            }
        });
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
                for (UploadListener listener : listeners)
                    listener.errorNotify(fileName, message, errorCode);
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
    public Map<UUID, String> getUploadsMap() {
        return files;
    }

    public ValueProvider getComponentParameters() {
        return componentParams;
    }
}
