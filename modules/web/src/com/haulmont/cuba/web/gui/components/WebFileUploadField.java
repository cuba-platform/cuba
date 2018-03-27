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

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.compatibility.FileUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.FileDataProvider;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.gui.FileUploadTypesHelper;
import com.haulmont.cuba.web.widgets.CubaFileUpload;
import com.haulmont.cuba.web.widgets.CubaUpload;
import com.haulmont.cuba.web.widgets.UploadComponent;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.haulmont.cuba.gui.components.Frame.NotificationType;

public class WebFileUploadField extends WebAbstractUploadField<CubaFileUploadWrapper> implements FileUploadField {

    private final Logger log = LoggerFactory.getLogger(WebFileUploadField.class);

    protected FileUploadingAPI fileUploading;
    protected ExportDisplay exportDisplay;
    protected Messages messages;

    protected FileContentProvider contentProvider;

    protected UploadComponent uploadButton;
    protected String fileName;
    protected FileStoragePutMode mode = FileStoragePutMode.MANUAL;

    protected String accept;

    protected UUID fileId;
    protected UUID tempFileId;

    /*
    * This flag is used only for MANUAL mode to register that
    * file was uploaded with the upload button rather then setValue calling
    * or changed property in the datasource
    */
    protected boolean internalValueChangedOnUpload = false;

    public WebFileUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        exportDisplay = AppBeans.get(ExportDisplay.NAME);
        messages = AppBeans.get(Messages.NAME);

        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        if ((webBrowser.isIE() && !webBrowser.isEdge()) && webBrowser.getBrowserMajorVersion() < 10) {
            initOldUploadButton();
        } else {
            initUploadButton();
        }

        initComponent();
        attachListener(component);
    }

    protected void initComponent() {
        component = new CubaFileUploadWrapper(uploadButton) {
            @Override
            protected void onSetInternalValue(Object newValue) {
                internalValueChanged(newValue);
            }
        };

        component.addFileNameClickListener(e -> {
            FileDescriptor value = getValue();
            if (value == null)
                return;

            switch (mode) {
                case MANUAL:
                    String name = getFileName();
                    String fileName = StringUtils.isEmpty(name) ? value.getName() : name;
                    exportDisplay.show(this::getFileContent, fileName);
                    break;
                case IMMEDIATE:
                    exportDisplay.show(value);
            }
        });
        component.setClearButtonListener((Button.ClickListener) this::clearButtonClicked);
    }

    protected void internalValueChanged(Object newValue) {
        fileName = newValue == null ? null : ((FileDescriptor) newValue).getName();

        if (!internalValueChangedOnUpload) {
            fileId = null;
            tempFileId = null;
        }
    }

    protected void clearButtonClicked(Button.ClickEvent clickEvent) {
        BeforeValueClearEvent beforeValueClearEvent = new BeforeValueClearEvent(this);
        getEventRouter().fireEvent(BeforeValueClearListener.class, BeforeValueClearListener::beforeValueClearPerformed, beforeValueClearEvent);

        if (!beforeValueClearEvent.isClearPrevented()) {
            setValue(null);
            fileName = null;
        }

        AfterValueClearEvent afterValueClearEvent = new AfterValueClearEvent(this, !beforeValueClearEvent.isClearPrevented());
        getEventRouter().fireEvent(AfterValueClearListener.class, AfterValueClearListener::afterValueClearPerformed, afterValueClearEvent);
    }

    protected void saveFile(FileDescriptor fileDescriptor) {
        switch (mode) {
            case MANUAL:
                internalValueChangedOnUpload = true;
                setValue(fileDescriptor);
                internalValueChangedOnUpload = false;
                break;
            case IMMEDIATE:
                try {
                    fileUploading.putFileIntoStorage(fileId, fileDescriptor);

                    FileDescriptor commitedDescriptor = commitFileDescriptor(fileDescriptor);

                    setValue(commitedDescriptor);
                } catch (FileStorageException e) {
                    log.error("Error has occurred during file saving", e);
                }
                break;
        }
    }

    protected FileDescriptor commitFileDescriptor(FileDescriptor fileDescriptor) {
        if (getDatasource() != null) {
            return getDatasource().getDataSupplier().commit(fileDescriptor);
        }

        if (getFrame().getDsContext().getDataSupplier() != null) {
            return getFrame().getDsContext().getDataSupplier().commit(fileDescriptor);
        }

        return AppBeans.get(DataManager.class).commit(fileDescriptor);
    }

    protected void initOldUploadButton() {
        uploadButton = createOldComponent();
        final CubaUpload impl = (CubaUpload) uploadButton;

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

        impl.addStartedListener(event -> {
            if (event.getContentLength() > getActualFileSizeLimit()) {
                impl.interruptUpload();
                String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", event.getFilename(), getFileSizeLimitString());

                getFrame().showNotification(warningMsg, NotificationType.WARNING);
            } else if (hasInvalidExtensionOld(event.getFilename())) {
                impl.interruptUpload();
                String warningMsg = messages.formatMainMessage("upload.fileIncorrectExtension.message", event.getFilename());
                getFrame().showNotification(warningMsg, NotificationType.WARNING);
            } else {
                fireFileUploadStart(event.getFilename(), event.getContentLength());
            }
        });

        impl.addFinishedListener(event -> fireFileUploadFinish(event.getFilename(), event.getLength()));

        impl.addSucceededListener(event -> {
            fileName = event.getFilename();
            fileId = tempFileId;

            saveFile(getFileDescriptor());
            component.setFileNameButtonCaption(fileName);

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
    }

    protected void initUploadButton() {
        uploadButton = createComponent();
        CubaFileUpload impl = (CubaFileUpload) uploadButton;

        impl.setProgressWindowCaption(messages.getMainMessage("upload.uploadingProgressTitle"));
        impl.setUnableToUploadFileMessage(messages.getMainMessage("upload.unableToUploadFile"));
        impl.setCancelButtonCaption(messages.getMainMessage("upload.cancel"));
        impl.setCaption(messages.getMainMessage("upload.submit"));
        impl.setDropZonePrompt(messages.getMainMessage("upload.singleDropZonePrompt"));
        impl.setDescription(null);

        impl.setFileSizeLimit(getActualFileSizeLimit());

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

            saveFile(getFileDescriptor());
            component.setFileNameButtonCaption(fileName);

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
            String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", e.getFileName(), getFileSizeLimitString());
            getFrame().showNotification(warningMsg, NotificationType.WARNING);
        });
        impl.addFileExtensionNotAllowedListener(e -> {
            String warningMsg = messages.formatMainMessage("upload.fileIncorrectExtension.message", e.getFileName());
            getFrame().showNotification(warningMsg, NotificationType.WARNING);
        });
    }

    protected CubaFileUpload createComponent() {
        return new CubaFileUpload();
    }

    protected CubaUpload createOldComponent() {
        return new CubaUpload();
    }

    protected boolean hasInvalidExtensionOld(String name) {
        if (getPermittedExtensions() != null && !getPermittedExtensions().isEmpty()) {
            if (name.lastIndexOf(".") > 0) {
                String fileExtension = name.substring(name.lastIndexOf("."), name.length());
                return !getPermittedExtensions().contains(fileExtension.toLowerCase());
            } else {
                return true;
            }
        }
        return false;
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
    public String getAccept() {
        return accept;
    }

    @Override
    public void setAccept(String accept) {
        if (!Objects.equals(accept, getAccept())) {
            this.accept = accept;
            uploadButton.setAccept(FileUploadTypesHelper.convertToMIME(accept));
        }
    }

    @Override
    public void setDropZone(DropZone dropZone) {
        super.setDropZone(dropZone);

        if (uploadButton instanceof CubaFileUpload) {
            if (dropZone == null) {
                ((CubaFileUpload) uploadButton).setDropZone(null);
            } else {
                com.haulmont.cuba.gui.components.Component target = dropZone.getTarget();
                if (target instanceof Window.Wrapper) {
                    target = ((Window.Wrapper) target).getWrappedWindow();
                }

                Component vComponent = target.unwrapComposition(Component.class);
                ((CubaFileUpload) uploadButton).setDropZone(vComponent);
            }
        }
    }

    @Override
    public void setPasteZone(Container pasteZone) {
        super.setPasteZone(pasteZone);
        if (uploadButton instanceof CubaFileUpload) {
            if (pasteZone == null) {
                ((CubaFileUpload) uploadButton).setPasteZone(null);
            } else {
                Component vComponent = pasteZone.unwrapComposition(Component.class);
                ((CubaFileUpload) uploadButton).setPasteZone(vComponent);
            }
        }
    }

    @Override
    public void setDropZonePrompt(String dropZonePrompt) {
        super.setDropZonePrompt(dropZonePrompt);

        if (uploadButton instanceof CubaFileUpload) {
            ((CubaFileUpload) uploadButton).setDropZonePrompt(dropZonePrompt);
        }
    }

    protected void fireFileUploadStart(String fileName, long contentLength) {
        FileUploadStartEvent e = new FileUploadStartEvent(fileName, contentLength);
        getEventRouter().fireEvent(FileUploadStartListener.class, FileUploadStartListener::fileUploadStart, e);
    }

    protected void fireFileUploadFinish(String fileName, long contentLength) {
        FileUploadFinishEvent e = new FileUploadFinishEvent(fileName, contentLength);
        getEventRouter().fireEvent(FileUploadFinishListener.class, FileUploadFinishListener::fileUploadFinish, e);
    }

    protected void fireFileUploadError(String fileName, long contentLength, Exception cause) {
        FileUploadErrorEvent e = new FileUploadErrorEvent(fileName, contentLength, cause);
        getEventRouter().fireEvent(FileUploadErrorListener.class, FileUploadErrorListener::fileUploadError, e);
    }

    protected void fireFileUploadSucceed(String fileName, long contentLength) {
        FileUploadSucceedEvent e = new FileUploadSucceedEvent(fileName, contentLength);
        getEventRouter().fireEvent(FileUploadSucceedListener.class, FileUploadSucceedListener::fileUploadSucceed, e);
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
        getEventRouter().addListener(FileUploadErrorListener.class, listener);
    }

    @Override
    public void removeFileUploadErrorListener(FileUploadErrorListener listener) {
        getEventRouter().removeListener(FileUploadErrorListener.class, listener);
    }

    @Override
    public void addFileUploadSucceedListener(FileUploadSucceedListener listener) {
        getEventRouter().addListener(FileUploadSucceedListener.class, listener);
    }

    @Override
    public void removeFileUploadSucceedListener(FileUploadSucceedListener listener) {
        getEventRouter().removeListener(FileUploadSucceedListener.class, listener);
    }

    @Override
    public InputStream getFileContent() {
        if (contentProvider != null) {
            return contentProvider.provide();
        }

        FileDescriptor fileDescriptor = getValue();
        switch (mode) {
            case MANUAL:
                if (fileId == null) {
                    return new FileDataProvider(fileDescriptor).provide();
                }

                File file = fileUploading.getFile(fileId);
                if (file != null) {
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        log.error("Unable to get content of {}", file, e);
                    }
                    return null;
                }

                FileStorageService fileStorageService = AppBeans.get(FileStorageService.NAME);
                try {
                    if (fileStorageService.fileExists(fileDescriptor)) {
                        return new FileDataProvider(fileDescriptor).provide();
                    }
                } catch (FileStorageException e) {
                    log.error("Unable to get content of {}", fileDescriptor, e);
                    return null;
                }
                break;
            case IMMEDIATE:
                if (fileDescriptor != null) {
                    return new FileDataProvider(fileDescriptor).provide();
                }
        }
        return null;
    }

    @Override
    public void setContentProvider(FileContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    @Override
    public FileContentProvider getContentProvider() {
        return contentProvider;
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
        if (uploadButton instanceof CubaFileUpload) {
            ((CubaFileUpload) uploadButton).setFileSizeLimit(fileSizeLimit);
        }
    }

    @Override
    public FileStoragePutMode getMode() {
        return mode;
    }

    @Override
    public void setMode(FileStoragePutMode mode) {
        this.mode = mode;
    }

    @Override
    public boolean isShowFileName() {
        return component.isShowFileName();
    }

    @Override
    public void setShowFileName(boolean showFileName) {
        component.setShowFileName(showFileName);
        if (showFileName && StringUtils.isNotEmpty(fileName)) {
            component.setFileNameButtonCaption(fileName);
        }
    }

    @Override
    public void setPermittedExtensions(Set<String> permittedExtensions) {
        if (permittedExtensions != null) {
            this.permittedExtensions = permittedExtensions.stream().map(String::toLowerCase).collect(Collectors.toSet());
        } else {
            this.permittedExtensions = null;
        }
        if (uploadButton instanceof CubaFileUpload) {
            ((CubaFileUpload) uploadButton).setPermittedExtensions(this.permittedExtensions);
        }
    }

    @Override
    public void setShowClearButton(boolean showClearButton) {
        component.setShowClearButton(showClearButton);
    }

    @Override
    public boolean isShowClearButton() {
        return component.isShowClearButton();
    }

    @Override
    public void setClearButtonCaption(String caption) {
        component.setClearButtonCaption(caption);
    }

    @Override
    public String getClearButtonCaption() {
        return component.getClearButtonCaption();
    }

    @Override
    public void setClearButtonIcon(String icon) {
        component.setClearButtonIcon(icon);
    }

    @Override
    public String getClearButtonIcon() {
        return component.getClearButtonIcon();
    }

    @Override
    public void setClearButtonDescription(String description) {
        component.setClearButtonDescription(description);
    }

    @Override
    public String getClearButtonDescription() {
        return component.getClearButtonDescription();
    }

    @Override
    public void addBeforeValueClearListener(BeforeValueClearListener listener) {
        getEventRouter().addListener(BeforeValueClearListener.class, listener);
    }

    @Override
    public void removeBeforeValueClearListener(BeforeValueClearListener listener) {
        getEventRouter().removeListener(BeforeValueClearListener.class, listener);
    }

    @Override
    public void addAfterValueClearListener(AfterValueClearListener listener) {
        getEventRouter().addListener(AfterValueClearListener.class, listener);
    }

    @Override
    public void removeAfterValueClearListener(AfterValueClearListener listener) {
        getEventRouter().removeListener(AfterValueClearListener.class, listener);
    }

    @Override
    public void setUploadButtonCaption(String caption) {
        component.setUploadButtonCaption(caption);
    }

    @Override
    public String getUploadButtonCaption() {
        return component.getUploadButtonCaption();
    }

    @Override
    public void setUploadButtonIcon(String icon) {
        if (!StringUtils.isEmpty(icon)) {
            component.setUploadButtonIcon(icon);
        } else {
            component.setUploadButtonIcon(null);
        }
    }

    @Override
    public String getUploadButtonIcon() {
        return component.getUploadButtonIcon();
    }

    @Override
    public void setUploadButtonDescription(String description) {
        component.setUploadButtonDescription(description);
    }

    @Override
    public String getUploadButtonDescription() {
        return component.getUploadButtonDescription();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }
}