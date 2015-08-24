/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.toolkit.ui.CubaFileUpload;
import com.haulmont.cuba.web.toolkit.ui.CubaUpload;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Upload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.haulmont.cuba.gui.components.Frame.NotificationType;

/**
 * @author abramov
 * @version $Id$
 */
public class WebFileUploadField extends WebAbstractComponent<AbstractComponent> implements FileUploadField {

    private static final int BYTES_IN_MEGABYTE = 1048576;

    protected Log log = LogFactory.getLog(getClass());

    protected FileUploadingAPI fileUploading;
    protected Messages messages;

    protected String fileName;

    protected UUID fileId;

    protected UUID tempFileId;

    protected List<Listener> listeners = new ArrayList<>();
    protected String icon;

    public WebFileUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        messages = AppBeans.get(Messages.NAME);

        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() < 10) {
            initOldComponent();
        } else {
            initComponent();
        }
    }

    protected void initOldComponent() {
        final CubaUpload impl = createOldComponent();

        impl.setButtonCaption(messages.getMainMessage("upload.submit"));
        impl.setDescription(null);

        impl.setReceiver(new com.vaadin.ui.Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String fileName, String MIMEType) {
                FileOutputStream outputStream;
                try {
                    tempFileId = fileUploading.createEmptyFile();
                    File tmpFile = fileUploading.getFile(tempFileId);
                    //noinspection ConstantConditions
                    outputStream = new FileOutputStream(tmpFile);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to receive file", e);
                }
                return outputStream;
            }
        });
        // Set single click upload functional
        impl.setImmediate(true);

        impl.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent event) {
                Configuration configuration = AppBeans.get(Configuration.NAME);
                final long maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();
                final long maxSize = maxUploadSizeMb * BYTES_IN_MEGABYTE;
                if (event.getContentLength() > maxSize) {
                    impl.interruptUpload();

                    String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", event.getFilename(), maxUploadSizeMb);
                    getFrame().showNotification(warningMsg, NotificationType.WARNING);
                } else {
                    final Listener.Event e = new Listener.Event(event.getFilename());
                    for (Listener listener : listeners) {
                        listener.uploadStarted(e);
                    }
                }
            }
        });
        impl.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadFinished(e);
                }
            }
        });
        impl.addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                fileName = event.getFilename();
                fileId = tempFileId;

                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadSucceeded(e);
                }
            }
        });
        impl.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent event) {
                try {
                    fileUploading.deleteFile(tempFileId);
                    tempFileId = null;
                } catch (Exception e) {
                    if (e instanceof FileStorageException) {
                        FileStorageException fse = (FileStorageException) e;
                        if (fse.getType() != FileStorageException.Type.FILE_NOT_FOUND)
                            log.warn(String.format("Could not remove temp file %s after broken uploading", tempFileId));
                    }
                    log.warn(String.format("Error while delete temp file %s", tempFileId));
                }
                final Listener.Event e = new Listener.Event(event.getFilename(), event.getReason());
                for (Listener listener : listeners) {
                    listener.uploadFailed(e);
                }
            }
        });

        this.component = impl;
    }

    protected void initComponent() {
        CubaFileUpload impl = createComponent();

        impl.setProgressWindowCaption(messages.getMainMessage("upload.uploadingProgressTitle"));
        impl.setUnableToUploadFileMessage(messages.getMainMessage("upload.unableToUploadFile"));
        impl.setCancelButtonCaption(messages.getMainMessage("upload.cancel"));
        impl.setCaption(messages.getMainMessage("upload.submit"));
        impl.setDescription(null);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        final int maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();
        final int maxSizeBytes = maxUploadSizeMb * BYTES_IN_MEGABYTE;

        impl.setFileSizeLimit(maxSizeBytes);

        impl.setReceiver(new CubaFileUpload.Receiver() {
            @Override
            public OutputStream receiveUpload(String fileName, String MIMEType) {
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
            }
        });
        impl.addStartedListener(new CubaFileUpload.StartedListener() {
            @Override
            public void uploadStarted(CubaFileUpload.StartedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFileName());
                for (Listener listener : listeners) {
                    listener.uploadStarted(e);
                }
            }
        });
        impl.addFinishedListener(new CubaFileUpload.FinishedListener() {
            @Override
            public void uploadFinished(CubaFileUpload.FinishedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFileName());
                for (Listener listener : listeners) {
                    listener.uploadFinished(e);
                }
            }
        });
        impl.addSucceededListener(new CubaFileUpload.SucceededListener() {
            @Override
            public void uploadSucceeded(CubaFileUpload.SucceededEvent event) {
                fileName = event.getFileName();
                fileId = tempFileId;

                final Listener.Event e = new Listener.Event(event.getFileName());
                for (Listener listener : listeners) {
                    listener.uploadSucceeded(e);
                }
            }
        });
        impl.addFailedListener(new CubaFileUpload.FailedListener() {
            @Override
            public void uploadFailed(CubaFileUpload.FailedEvent event) {
                try {
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
                final Listener.Event e = new Listener.Event(event.getFileName(), event.getReason());
                for (Listener listener : listeners) {
                    listener.uploadFailed(e);
                }
            }
        });
        impl.addFileSizeLimitExceededListener(new CubaFileUpload.FileSizeLimitExceededListener() {
            @Override
            public void fileSizeLimitExceeded(CubaFileUpload.FileSizeLimitExceededEvent e) {
                String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", e.getFileName(), maxUploadSizeMb);
                getFrame().showNotification(warningMsg, NotificationType.WARNING);
            }
        });

        this.component = impl;
    }

    protected CubaFileUpload createComponent() {
        return new CubaFileUpload();
    }

    protected CubaUpload createOldComponent() {
        return new CubaUpload();
    }

    @Override
    public String getFileName() {
        if (fileName == null) {
            return null;
        }

        String[] strings = fileName.split("[/\\\\]");
        return strings[strings.length - 1];
    }

    @Override
    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Get content bytes for uploaded file
     *
     * @return Bytes for uploaded file
     * @deprecated Please use {@link WebFileUploadField#getFileId()} method and {@link FileUploadingAPI}
     */
    @Override
    @Deprecated
    public byte[] getBytes() {
        byte[] bytes = null;
        try {
            if (fileId != null) {
                File file = fileUploading.getFile(fileId);
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                IOUtils.copy(fileInputStream, byteOutput);
                bytes = byteOutput.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return bytes;
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

    /**
     * @return File id for uploaded file in {@link FileUploadingAPI}
     */
    @Override
    public UUID getFileId() {
        return fileId;
    }

    @Override
    public FileDescriptor getFileDescriptor() {
        if (fileId != null) {
            return fileUploading.getFileDescriptor(fileId, fileName);
        } else {
            return null;
        }
    }

    @Override
    public String getIcon() {
        return icon;
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
}