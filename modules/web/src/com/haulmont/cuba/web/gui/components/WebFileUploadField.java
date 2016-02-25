/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.compatibility.FileUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.toolkit.FileUploadTypesHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaFileUpload;
import com.haulmont.cuba.web.toolkit.ui.CubaUpload;
import com.haulmont.cuba.web.toolkit.ui.UploadComponent;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.haulmont.cuba.gui.components.Frame.NotificationType;

/**
 * @author abramov
 * @version $Id$
 */
public class WebFileUploadField extends WebAbstractComponent<UploadComponent> implements FileUploadField {

    private static final int BYTES_IN_MEGABYTE = 1048576;

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected FileUploadingAPI fileUploading;
    protected Messages messages;

    protected String fileName;

    protected String accept;

    protected UUID fileId;

    protected UUID tempFileId;

    protected String icon;

    protected long fileSizeLimit = 0;

    protected List<FileUploadStartListener> fileUploadStartListeners;     // lazily initialized list
    protected List<FileUploadFinishListener> fileUploadFinishListeners;   // lazily initialized list
    protected List<FileUploadErrorListener> fileUploadErrorListeners;     // lazily initialized list
    protected List<FileUploadSucceedListener> fileUploadSucceedListeners; // lazily initialized list

    public WebFileUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        messages = AppBeans.get(Messages.NAME);

        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        if ((webBrowser.isIE() && !webBrowser.isEdge()) && webBrowser.getBrowserMajorVersion() < 10) {
            initOldComponent();
        } else {
            initComponent();
        }
    }

    protected void initOldComponent() {
        final CubaUpload impl = createOldComponent();

        impl.setButtonCaption(messages.getMainMessage("upload.submit"));
        impl.setDescription(null);

        impl.setReceiver((fileName1, MIMEType) -> {
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
        });
        // Set single click upload functional
        impl.setImmediate(true);

        impl.addStartedListener(event -> {
            final long maxSize;

            if (fileSizeLimit > 0) {
                maxSize = fileSizeLimit;
            } else {
                Configuration configuration = AppBeans.get(Configuration.NAME);
                final long maxUploadSizeMb = configuration.getConfig(ClientConfig.class).getMaxUploadSizeMb();
                maxSize = maxUploadSizeMb * BYTES_IN_MEGABYTE;
            }

            if (event.getContentLength() > maxSize) {
                impl.interruptUpload();

                double fileSizeInMb = maxSize / BYTES_IN_MEGABYTE;
                Datatype<Double> doubleDatatype = Datatypes.getNN(Double.class);
                String fileSizeLimitString = doubleDatatype.format(fileSizeInMb);
                String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", event.getFilename(), fileSizeLimitString);

                getFrame().showNotification(warningMsg, NotificationType.WARNING);
            } else {
                fireFileUploadStart(event.getFilename(), event.getContentLength());
            }
        });

        impl.addFinishedListener(event -> fireFileUploadFinish(event.getFilename(), event.getLength()));

        impl.addSucceededListener(event -> {
            fileName = event.getFilename();
            fileId = tempFileId;

            fireFileUploadSucceed(event.getFilename(), event.getLength());
        });

        impl.addFailedListener(event -> {
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

            fireFileUploadError(event.getFilename(), event.getLength(), event.getReason());
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

        impl.setReceiver((fileName1, MIMEType) -> {
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

        impl.addFinishedListener(event -> fireFileUploadFinish(event.getFileName(), event.getContentLength()));

        impl.addSucceededListener(event -> {
            fileName = event.getFileName();
            fileId = tempFileId;

            fireFileUploadSucceed(event.getFileName(), event.getContentLength());
        });

        impl.addFailedListener(event -> {
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

            fireFileUploadError(event.getFileName(), event.getContentLength(), event.getReason());
        });
        impl.addFileSizeLimitExceededListener(e -> {
            String warningMsg;
            if (fileSizeLimit > 0){
                double fileSizeInMb = fileSizeLimit / BYTES_IN_MEGABYTE;
                Datatype<Double> doubleDatatype = Datatypes.getNN(Double.class);
                String fileSizeLimitString = doubleDatatype.format(fileSizeInMb);
                warningMsg = messages.formatMainMessage("upload.fileTooBig.message", e.getFileName(), fileSizeLimitString);
            } else {
                warningMsg = messages.formatMainMessage("upload.fileTooBig.message", e.getFileName(), maxUploadSizeMb);
            }
            getFrame().showNotification(warningMsg, NotificationType.WARNING);
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
        FileUploadFieldListenerWrapper wrapper = new FileUploadFieldListenerWrapper(listener);

        addFileUploadStartListener(wrapper);
        addFileUploadErrorListener(wrapper);
        addFileUploadFinishListener(wrapper);
        addFileUploadSucceedListener(wrapper);
    }

    @Override
    public void removeListener(Listener listener) {
        FileUploadFieldListenerWrapper wrapper = new FileUploadFieldListenerWrapper(listener);

        removeFileUploadStartListener(wrapper);
        removeFileUploadErrorListener(wrapper);
        removeFileUploadFinishListener(wrapper);
        removeFileUploadSucceedListener(wrapper);
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
            throw new RuntimeException("Unable to get file content", e);
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

    @Override
    public String getAccept() {
        return accept;
    }

    @Override
    public void setAccept(String accept) {
        if (!StringUtils.equals(accept, getAccept())) {
            this.accept = accept;
            component.setAccept(FileUploadTypesHelper.convertToMIME(accept));
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

    protected void fireFileUploadSucceed(String fileName, long contentLength) {
        if (fileUploadSucceedListeners != null && !fileUploadSucceedListeners.isEmpty()) {
            FileUploadSucceedEvent e = new FileUploadSucceedEvent(fileName, contentLength);
            for (FileUploadSucceedListener listener : new ArrayList<>(fileUploadSucceedListeners)) {
                listener.fileUploadSucceed(e);
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
    public void addFileUploadSucceedListener(FileUploadSucceedListener listener) {
        if (fileUploadSucceedListeners == null) {
            fileUploadSucceedListeners = new ArrayList<>();
        }
        if (!fileUploadSucceedListeners.contains(listener)) {
            fileUploadSucceedListeners.add(listener);
        }
    }

    @Override
    public void removeFileUploadSucceedListener(FileUploadSucceedListener listener) {
        if (fileUploadSucceedListeners != null) {
            fileUploadSucceedListeners.remove(listener);
        }
    }

    @Override
    public long getFileSizeLimit() {
        return this.fileSizeLimit;
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
        if (this.component instanceof CubaFileUpload){
            ((CubaFileUpload) this.component).setFileSizeLimit((int) fileSizeLimit);
        }
    }
}