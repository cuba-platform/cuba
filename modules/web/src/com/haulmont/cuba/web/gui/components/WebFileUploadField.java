/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 11.03.2009 17:48:51
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.app.FileUploadService;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.web.toolkit.ui.Upload;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebFileUploadField
        extends
        WebAbstractComponent<Upload>
        implements
        FileUploadField {

    private static final int BUFFER__SIZE = 64 * 1024;

    protected FileUploadService uploadService;

    protected String fileName;
    protected byte[] bytes;

    protected UUID fileId;

    protected FileOutputStream outputStream;
    protected UUID tempFileId;

    private List<Listener> listeners = new ArrayList<Listener>();

    public WebFileUploadField() {
        uploadService = ServiceLocator.lookup(FileUploadService.NAME);
        String caption = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "Upload");
        component = new Upload(
                /* Fixes caption rendering.
                * If caption == "", the VerticalLayout reserves an empty space */
                StringUtils.isEmpty(caption) ? null : caption,
                new Upload.Receiver() {
                    public OutputStream receiveUpload(String filename, String MIMEType) {
                        fileName = filename;
                        try {
                            tempFileId = uploadService.createEmptyFile();
                            File tmpFile = uploadService.getFile(tempFileId);
                            outputStream = new FileOutputStream(tmpFile);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return outputStream;
                    }
                });
        // Set single click upload functional
        component.setImmediate(true);
        component.setAction("");

        component.addListener(new Upload.StartedListener() {
            public void uploadStarted(Upload.StartedEvent event) {
                bytes = null;
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadStarted(e);
                }
            }
        });
        component.addListener(new Upload.FinishedListener() {
            public void uploadFinished(Upload.FinishedEvent event) {
                if (outputStream != null)
                    try {
                        outputStream.close();
                        fileId = tempFileId;
                    } catch (IOException ignored) {
                    }
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadFinished(e);
                }
            }
        });
        component.addListener(new Upload.SucceededListener() {
            public void uploadSucceeded(Upload.SucceededEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadSucceeded(e);
                }
            }
        });
        component.addListener(new Upload.FailedListener() {
            public void uploadFailed(Upload.FailedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadFailed(e);
                }
            }
        });
        component.addListener(new Upload.ProgressListener() {
            public void updateProgress(long readBytes, long contentLength) {
                for (Listener listener : listeners) {
                    listener.updateProgress(readBytes, contentLength);
                }
            }
        });
        component.setButtonCaption(MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(),
                "upload.submit"));
    }

    public String getFilePath() {
        return fileName;
    }

    public String getFileName() {
        String[] strings = fileName.split("[/\\\\]");
        return strings[strings.length - 1];
    }

    public boolean isUploading() {
        return component.isUploading();
    }

    public long getBytesRead() {
        return component.getBytesRead();
    }

    public void release() {
        outputStream = null;
        bytes = null;
    }

    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void readFileToBytes(FileInputStream fileInput, ByteArrayOutputStream byteOutput)
            throws IOException {
        int readedBytes;
        byte[] buffer = new byte[BUFFER__SIZE];
        do {
            readedBytes = fileInput.read(buffer);
            byteOutput.write(buffer, 0, readedBytes);
        }
        while (readedBytes == BUFFER__SIZE);
    }

    /**
     * Get content bytes for uploaded file
     *
     * @return Bytes for uploaded file
     * @deprecated Please use {@link WebFileUploadField#getFileId()} method and {@link FileUploadService}
     */
    public byte[] getBytes() {
        if (bytes == null) {
            try {
                File file = uploadService.getFile(fileId);
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                readFileToBytes(fileInputStream, byteOutput);
                bytes = byteOutput.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return bytes;
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public String getDescription() {
        return component.getDescription();
    }

    public void setDescription(String description) {
        component.setDescription(description);
    }

    /**
     * Get id for uploaded file in {@link FileUploadService}
     *
     * @return File Id
     */
    public UUID getFileId() {
        return fileId;
    }
}
