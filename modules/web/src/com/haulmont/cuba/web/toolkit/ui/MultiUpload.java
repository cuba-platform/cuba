/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 19.11.2010 16:07:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.ValueProvider;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;
import org.apache.commons.fileupload.FileItemStream;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
@ClientWidget(VSwfUpload.class)
public class MultiUpload extends AbstractComponent {

    public interface FileUploadStartListener extends Serializable {
        void fileUploadStart(String fileName);
    }

    public interface FileUploadCompleteListener extends Serializable {
        void fileUploaded(String fileName, UUID uuid);
    }

    public interface FileProgressListener extends Serializable {
        void progressChanged(String fileName, int receivedBytes, int contentLength);
    }

    public interface QueueCompleteListener extends Serializable {
        void queueUploadComplete();
    }

    public interface FileErrorHandler extends Serializable {
        void errorNotify(String fileName, String message, int errorCode);
    }

    private List<FileUploadStartListener> fileStartListeners = new ArrayList<FileUploadStartListener>();

    private List<FileUploadCompleteListener> fileCompleteListeners = new ArrayList<FileUploadCompleteListener>();

    private List<QueueCompleteListener> queueCompleteListeners = new ArrayList<QueueCompleteListener>();

    private List<FileProgressListener> fileProgressListeners = new ArrayList<FileProgressListener>();

    private List<FileErrorHandler> fileErrorListeners = new ArrayList<FileErrorHandler>();

    private ValueProvider valueProvider = null;

    public MultiUpload(String caption) {
        setHeight("50px");
        setCaption(caption);
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        // Uploading complete
        if (variables.containsKey("queueUploadComplete")) {
            for (QueueCompleteListener listener : queueCompleteListeners)
                listener.queueUploadComplete();
        }
        if (variables.containsKey("uploadError")) {
            try {
                Object[] errorVariables = (Object[]) variables.get("uploadError");
                String message = errorVariables[1].toString();
                String file = errorVariables[0].toString();
                int errorCode = Integer.parseInt(errorVariables[2].toString());
                for (FileErrorHandler listener : fileErrorListeners)
                    listener.errorNotify(file, message, errorCode);
            }
            catch (Exception ex) {
                // Do nothing
            }
        }
    }

    //Upload file to server

    public void uploadingFile(FileItemStream itemStream, int contentLength)
            throws IOException, FileStorageException {
        // Upload Start
        final String fileName = itemStream.getName();
        final int streamLength = contentLength;
        for (FileUploadStartListener listener : fileStartListeners)
            listener.fileUploadStart(fileName);
        // Uploading from stream
        FileUploadingAPI fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        UUID uuid = fileUploading.saveFile(itemStream.openStream(), new FileUploadingAPI.UploadProgressListener() {

            public void progressChanged(UUID fileId, int receivedBytes) {
                for (FileProgressListener listener : fileProgressListeners)
                    listener.progressChanged(fileName, receivedBytes, streamLength);
            }
        });
        // Upload complete
        for (FileUploadCompleteListener listener : fileCompleteListeners)
            listener.fileUploaded(fileName, uuid);
    }

    /**
     * Paints the content of this component.
     *
     * @param target Target to paint the content on.
     * @throws com.vaadin.terminal.PaintException
     *          if the paint operation failed.
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (valueProvider != null) {
            Iterator<Map.Entry<String, Object>> iter =
                    valueProvider.getParameters().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> prop = iter.next();
                target.addAttribute(prop.getKey(), String.valueOf(prop.getValue()));
            }
        }
    }

    // Add|Remove Listeners

    public void addListener(FileUploadStartListener startListener) {
        fileStartListeners.add(startListener);
    }

    public void removeListener(FileUploadStartListener startListener) {
        fileStartListeners.remove(startListener);
    }

    public void addListener(FileUploadCompleteListener completeListener) {
        fileCompleteListeners.add(completeListener);
    }

    public void removeListener(FileUploadCompleteListener completeListener) {
        fileCompleteListeners.remove(completeListener);
    }

    public void addListener(FileProgressListener progressListener) {
        fileProgressListeners.add(progressListener);
    }

    public void removeListener(FileProgressListener progressListener) {
        fileProgressListeners.remove(progressListener);
    }

    public void addListener(QueueCompleteListener queueCompleteListener) {
        queueCompleteListeners.add(queueCompleteListener);
    }

    public void removeListener(QueueCompleteListener queueCompleteListener) {
        queueCompleteListeners.remove(queueCompleteListener);
    }

    public void addListener(FileErrorHandler errorListener) {
        fileErrorListeners.add(errorListener);
    }

    public void removeListener(FileErrorHandler errorListener) {
        fileErrorListeners.remove(errorListener);
    }

    // Get|Set value provider

    public ValueProvider getValueProvider() {
        return valueProvider;
    }

    public void setValueProvider(ValueProvider valueProvider) {
        this.valueProvider = valueProvider;
    }

    @Override
    public void setCaption(String caption) {
        if (valueProvider != null) {
            valueProvider.getParameters().put("caption", caption);
            requestRepaint();
        }
        super.setCaption(caption);
    }
}