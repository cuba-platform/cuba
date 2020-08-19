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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.FileDataProvider;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaFileUpload;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.io.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.haulmont.cuba.gui.ComponentsHelper.getScreenContext;
import static com.haulmont.cuba.web.gui.FileUploadTypesHelper.convertToMIME;

public class WebFileUploadField extends WebAbstractUploadField<CubaFileUploadWrapper>
        implements FileUploadField, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WebFileUploadField.class);

    protected FileUploadingAPI fileUploading;
    protected ExportDisplay exportDisplay;
    protected Messages messages;

    protected Supplier<InputStream> contentProvider;

    protected CubaFileUpload uploadButton;
    protected String fileName;
    protected FileStoragePutMode mode = FileStoragePutMode.MANUAL;

    protected String accept;

    protected UUID fileId;
    protected UUID tempFileId;

    /*
     * This flag is used only for MANUAL mode to register that file was uploaded with the upload button rather then
     * setValue calling or changed property in the datasource.
     */
    protected boolean internalValueChangedOnUpload = false;

    public WebFileUploadField() {
        uploadButton = createComponent();
        component = createWrapper();
    }

    protected CubaFileUploadWrapper createWrapper() {
        return new CubaFileUploadWrapper(uploadButton) {
            @Override
            protected void onSetInternalValue(Object newValue) {
                internalValueChanged(newValue);
            }
        };
    }

    @Override
    public void afterPropertiesSet() {
        initUploadButton(uploadButton);

        initComponent();
        attachValueChangeListener(component);
    }

    @Override
    protected void valueBindingConnected(ValueSource<FileDescriptor> valueSource) {
        super.valueBindingConnected(valueSource);

        setShowFileName(true);
    }

    @Inject
    public void setFileUploading(FileUploadingAPI fileUploading) {
        this.fileUploading = fileUploading;
    }

    @Inject
    public void setExportDisplay(ExportDisplay exportDisplay) {
        this.exportDisplay = exportDisplay;
    }

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;

        component.setClearButtonCaption(messages.getMainMessage("FileUploadField.clearButtonCaption"));
        component.setFileNotSelectedMessage(messages.getMainMessage("FileUploadField.fileNotSelected"));
    }

    protected void initComponent() {
        component.addFileNameClickListener(e -> {
            FileDescriptor value = getValue();
            if (value == null) {
                return;
            }

            switch (mode) {
                case MANUAL:
                    String name = getFileName();
                    String fileName = StringUtils.isEmpty(name) ? value.getName() : name;
                    exportDisplay.show(this::getFileContent, fileName);
                    break;
                case IMMEDIATE:
                    exportDisplay.show(value);
                    break;
            }
        });
        component.setClearButtonListener(this::clearButtonClicked);
        component.setRequiredError(null);

        applyPermissions();
    }

    protected void applyPermissions() {
        Security security = beanLocator.get(Security.NAME);

        if (!security.isEntityOpPermitted(FileDescriptor.class, EntityOp.UPDATE)) {
            component.setUploadButtonEnabled(false);
            component.setClearButtonEnabled(false);
        }
        if (!security.isEntityOpPermitted(FileDescriptor.class, EntityOp.READ)) {
            component.setFileNameButtonEnabled(false);
        }
    }

    protected void internalValueChanged(Object newValue) {
        fileName = newValue == null ? null : ((FileDescriptor) newValue).getName();

        if (!internalValueChangedOnUpload) {
            fileId = null;
            tempFileId = null;
        }
    }

    protected void clearButtonClicked(@SuppressWarnings("unused") Button.ClickEvent clickEvent) {
        BeforeValueClearEvent beforeValueClearEvent = new BeforeValueClearEvent(this);
        publish(BeforeValueClearEvent.class, beforeValueClearEvent);

        if (!beforeValueClearEvent.isClearPrevented()) {
            setValue(null);
            fileName = null;
        }

        AfterValueClearEvent afterValueClearEvent = new AfterValueClearEvent(this,
                !beforeValueClearEvent.isClearPrevented());
        publish(AfterValueClearEvent.class, afterValueClearEvent);
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

                    FileDescriptor committedDescriptor = commitFileDescriptor(fileDescriptor);

                    setValue(committedDescriptor);
                } catch (FileStorageException e) {
                    log.error("Error has occurred during file saving", e);
                }
                break;
        }
    }

    protected FileDescriptor commitFileDescriptor(FileDescriptor fileDescriptor) {
        DataSupplier dataSupplier = getDataSupplier();
        if (dataSupplier != null) {
            return dataSupplier.commit(fileDescriptor);
        }

        DataManager dataManager = beanLocator.get(DataManager.NAME);
        return dataManager.commit(fileDescriptor);
    }

    protected DataSupplier getDataSupplier() {
        if (getDatasource() != null) {
            return getDatasource().getDataSupplier();
        }
        Window window = ComponentsHelper.getWindowNN(this);
        if (window.getFrameOwner() instanceof LegacyFrame) {
            DsContext dsContext = ((LegacyFrame) window.getFrameOwner()).getDsContext();
            if (dsContext != null && dsContext.getDataSupplier() != null) {
                return dsContext.getDataSupplier();
            }
        }

        return null;
    }

    protected void initUploadButton(CubaFileUpload impl) {
        impl.setProgressWindowCaption(messages.getMainMessage("upload.uploadingProgressTitle"));
        impl.setUnableToUploadFileMessage(messages.getMainMessage("upload.unableToUploadFile"));
        impl.setCancelButtonCaption(messages.getMainMessage("upload.cancel"));
        impl.setCaption(messages.getMainMessage("upload.submit"));
        impl.setDropZonePrompt(messages.getMainMessage("upload.singleDropZonePrompt"));
        impl.setDescription(null);

        impl.setFileSizeLimit(getActualFileSizeLimit());

        impl.setReceiver(this::receiveUpload);

        impl.addStartedListener(event ->
                fireFileUploadStart(event.getFileName(), event.getContentLength())
        );

        impl.addFinishedListener(event ->
                fireFileUploadFinish(event.getFileName(), event.getContentLength())
        );

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
            Notifications notifications = getScreenContext(this).getNotifications();

            notifications.create(NotificationType.WARNING)
                    .withCaption(messages.formatMainMessage("upload.fileTooBig.message", e.getFileName(), getFileSizeLimitString()))
                    .show();
        });
        impl.addFileExtensionNotAllowedListener(e -> {
            Notifications notifications = getScreenContext(this).getNotifications();

            notifications.create(NotificationType.WARNING)
                    .withCaption(messages.formatMainMessage("upload.fileIncorrectExtension.message", e.getFileName()))
                    .show();
        });
    }

    protected OutputStream receiveUpload(String fileName, String MIMEType) {
        try {
            FileUploadingAPI.FileInfo fileInfo = fileUploading.createFile();
            tempFileId = fileInfo.getId();
            File tmpFile = fileInfo.getFile();

            return new FileOutputStream(tmpFile);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to receive file '%s' of MIME type: %s", fileName, MIMEType), e);
        }
    }

    protected CubaFileUpload createComponent() {
        return new CubaFileUpload();
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
    public Subscription addFileUploadSucceedListener(Consumer<FileUploadSucceedEvent> listener) {
        return getEventHub().subscribe(FileUploadSucceedEvent.class, listener);
    }

    @Override
    public void removeFileUploadSucceedListener(Consumer<FileUploadSucceedEvent> listener) {
        unsubscribe(FileUploadSucceedEvent.class, listener);
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

            uploadButton.setAccept(convertToMIME(accept));
        }
    }

    @Override
    public void setDropZone(DropZone dropZone) {
        super.setDropZone(dropZone);

        if (dropZone == null) {
            uploadButton.setDropZone(null);
        } else {
            com.haulmont.cuba.gui.components.Component target = dropZone.getTarget();
            if (target instanceof Window.Wrapper) {
                target = ((Window.Wrapper) target).getWrappedWindow();
            }

            Component vComponent = target.unwrapComposition(Component.class);
            uploadButton.setDropZone(vComponent);
        }
    }

    @Override
    public void setPasteZone(ComponentContainer pasteZone) {
        super.setPasteZone(pasteZone);

        uploadButton.setPasteZone(pasteZone != null ? pasteZone.unwrapComposition(Component.class) : null);
    }

    @Override
    public void setDropZonePrompt(String dropZonePrompt) {
        super.setDropZonePrompt(dropZonePrompt);

        uploadButton.setDropZonePrompt(dropZonePrompt);
    }

    protected void fireFileUploadStart(String fileName, long contentLength) {
        publish(FileUploadStartEvent.class, new FileUploadStartEvent(this, fileName, contentLength));
    }

    protected void fireFileUploadFinish(String fileName, long contentLength) {
        publish(FileUploadFinishEvent.class, new FileUploadFinishEvent(this, fileName, contentLength));
    }

    protected void fireFileUploadError(String fileName, long contentLength, Exception cause) {
        publish(FileUploadErrorEvent.class, new FileUploadErrorEvent(this, fileName, contentLength, cause));
    }

    protected void fireFileUploadSucceed(String fileName, long contentLength) {
        publish(FileUploadSucceedEvent.class, new FileUploadSucceedEvent(this, fileName, contentLength));
    }

    @Override
    public InputStream getFileContent() {
        if (contentProvider != null) {
            return contentProvider.get();
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

                FileStorageService fileStorageService = beanLocator.get(FileStorageService.NAME);
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
    public void setContentProvider(Supplier<InputStream> contentProvider) {
        this.contentProvider = contentProvider;
    }

    @Override
    public Supplier<InputStream> getContentProvider() {
        return contentProvider;
    }

    @Override
    public Subscription addFileUploadStartListener(Consumer<FileUploadStartEvent> listener) {
        return getEventHub().subscribe(FileUploadStartEvent.class, listener);
    }

    @Override
    public void removeFileUploadStartListener(Consumer<FileUploadStartEvent> listener) {
        unsubscribe(FileUploadStartEvent.class, listener);
    }

    @Override
    public Subscription addFileUploadFinishListener(Consumer<FileUploadFinishEvent> listener) {
        return getEventHub().subscribe(FileUploadFinishEvent.class, listener);
    }

    @Override
    public void removeFileUploadFinishListener(Consumer<FileUploadFinishEvent> listener) {
        unsubscribe(FileUploadFinishEvent.class, listener);
    }

    @Override
    public Subscription addFileUploadErrorListener(Consumer<FileUploadErrorEvent> listener) {
        return getEventHub().subscribe(FileUploadErrorEvent.class, listener);
    }

    @Override
    public void removeFileUploadErrorListener(Consumer<FileUploadErrorEvent> listener) {
        unsubscribe(FileUploadErrorEvent.class, listener);
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;

        uploadButton.setFileSizeLimit(fileSizeLimit);
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
        if (showFileName) {
            component.setFileNameButtonCaption(fileName);
        }
    }

    @Override
    public void setPermittedExtensions(Set<String> permittedExtensions) {
        if (permittedExtensions != null) {
            this.permittedExtensions = permittedExtensions.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        } else {
            this.permittedExtensions = null;
        }
        uploadButton.setPermittedExtensions(this.permittedExtensions);
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
        if (icon != null) {
            IconResolver iconResolver = beanLocator.get(IconResolver.NAME);
            Resource iconResource = iconResolver.getIconResource(icon);
            component.setClearButtonIcon(iconResource);
        } else {
            component.setClearButtonIcon(null);
        }
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
    public Subscription addBeforeValueClearListener(Consumer<BeforeValueClearEvent> listener) {
        return getEventHub().subscribe(BeforeValueClearEvent.class, listener);
    }

    @Override
    public void removeBeforeValueClearListener(Consumer<BeforeValueClearEvent> listener) {
        unsubscribe(BeforeValueClearEvent.class, listener);
    }

    @Override
    public Subscription addAfterValueClearListener(Consumer<AfterValueClearEvent> listener) {
        return getEventHub().subscribe(AfterValueClearEvent.class, listener);
    }

    @Override
    public void removeAfterValueClearListener(Consumer<AfterValueClearEvent> listener) {
        unsubscribe(AfterValueClearEvent.class, listener);
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
            IconResolver iconResolver = beanLocator.get(IconResolver.class);
            component.setUploadButtonIcon(iconResolver.getIconResource(icon));
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
    public void focus() {
        component.focus();
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