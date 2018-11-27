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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.backgroundwork.BackgroundWorkProgressWindow;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.compatibility.FileUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.FileDataProvider;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;
import static com.haulmont.cuba.gui.upload.FileUploadingAPI.FileInfo;

public class DesktopFileUploadField extends DesktopAbstractUploadField<CubaFileUploadWrapper> implements FileUploadField {

    private final Logger log = LoggerFactory.getLogger(DesktopFileUploadField.class);

    protected FileUploadingAPI fileUploading;
    protected Messages messages;
    protected ExportDisplay exportDisplay;

    protected FileContentProvider contentProvider;

    protected volatile boolean isUploadingState = false;

    protected String fileName;
    protected Button uploadButton;
    protected FileStoragePutMode mode = FileStoragePutMode.MANUAL;

    protected Datasource datasource;

    protected boolean updatingInstance;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected Datasource.ItemChangeListener securityItemChangeListener;
    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;

    protected UUID fileId;
    protected UUID tempFileId;

    protected List<FileUploadStartListener> fileUploadStartListeners;     // lazily initialized list
    protected List<FileUploadFinishListener> fileUploadFinishListeners;   // lazily initialized list
    protected List<FileUploadSucceedListener> fileUploadSucceedListeners; // lazily initialized list
    protected List<FileUploadErrorListener> fileUploadErrorListeners;     // lazily initialized list

    protected List<BeforeValueClearListener> beforeValueClearListeners; // lazily initialized list
    protected List<AfterValueClearListener> afterValueClearListeners; // lazily initialized list

    protected FileDescriptor prevValue;

    public DesktopFileUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        messages = AppBeans.get(Messages.NAME);
        exportDisplay = AppBeans.get(ExportDisplay.NAME);

        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);
        uploadButton = (Button) componentsFactory.createComponent(Button.NAME);
        final JFileChooser fileChooser = new JFileChooser();
        uploadButton.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                if (fileChooser.showOpenDialog(uploadButton.unwrap(JButton.class)) == JFileChooser.APPROVE_OPTION) {
                    uploadFile(fileChooser.getSelectedFile());
                }
            }
        });
        uploadButton.setCaption(messages.getMessage(getClass(), "export.selectFile"));

        initImpl();
    }

    protected void initImpl() {
        impl = new CubaFileUploadWrapper(uploadButton);
        impl.setFileNameButtonClickListener(() -> {
            FileDescriptor value = getValue();
            if (value == null)
                return;

            switch (mode) {
                case MANUAL:
                    String name = getFileName();
                    String fileName1 = StringUtils.isEmpty(name) ? value.getName() : name;
                    exportDisplay.show(DesktopFileUploadField.this::getFileContent, fileName1);
                    break;
                case IMMEDIATE:
                    exportDisplay.show(value);
            }
        });
        impl.setClearButtonListener(this::clearButtonClicked);
    }

    protected void clearButtonClicked() {
        boolean preventClearAction = false;
        if (beforeValueClearListeners != null) {
            BeforeValueClearEvent beforeValueClearEvent = new BeforeValueClearEvent(this);
            for (BeforeValueClearListener listener : new ArrayList<>(beforeValueClearListeners)) {
                listener.beforeValueClearPerformed(beforeValueClearEvent);
            }
            preventClearAction = beforeValueClearEvent.isClearPrevented();
        }

        if (!preventClearAction) {
            setValue(null);
            fileName = null;
        }

        if (afterValueClearListeners != null) {
            AfterValueClearEvent afterValueClearEvent = new AfterValueClearEvent(this, !preventClearAction);
            for (AfterValueClearListener listener : new ArrayList<>(afterValueClearListeners)) {
                listener.afterValueClearPerformed(afterValueClearEvent);
            }
        }
    }

    protected void uploadFile(File file) {
        if (file.length() > getActualFileSizeLimit()) {
            String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", file.getName(), getFileSizeLimitString());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        } else if (hasInvalidExtension(file.getName())) {
            String warningMsg = messages.formatMainMessage("upload.fileIncorrectExtension.message", file.getName());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        } else {
            boolean success = true;
            try {
                isUploadingState = true;

                fileName = file.getName();
                fireFileUploadStart(file.getName(), file.length());

                FileInfo fileInfo = fileUploading.createFile();
                tempFileId = fileInfo.getId();
                File tmpFile = fileInfo.getFile();

                FileUtils.copyFile(file, tmpFile);

                fileId = tempFileId;
                saveFile(getFileDescriptor());

                isUploadingState = false;
            } catch (Exception ex) {
                success = false;
                try {
                    fileUploading.deleteFile(tempFileId);
                    tempFileId = null;
                } catch (FileStorageException e) {
                    throw new RuntimeException("Unable to delete file from temp storage", ex);
                }

                fireFileUploadError(file.getName(), file.length(), ex);
            } finally {
                fireFileUploadFinish(file.getName(), file.length());
            }

            if (success) {
                fireFileUploadSucceed(file.getName(), file.length());
            }
        }
    }

    protected void saveFile(FileDescriptor fileDescriptor) {
        switch (mode) {
            case MANUAL:
                setValue(fileDescriptor);
                break;
            case IMMEDIATE:
                BackgroundTask<Long, FileDescriptor> uploadProgress =
                        new BackgroundTask<Long, FileDescriptor>(2400, getFrame()) {
                            @Override
                            public Map<String, Object> getParams() {
                                return ParamsMap.of("fileId", fileId, "fileName", getFileName());
                            }

                            @Override
                            public FileDescriptor run(final TaskLifeCycle<Long> taskLifeCycle) throws Exception {
                                return fileUploading.putFileIntoStorage(taskLifeCycle);
                            }

                            @Override
                            public void done(FileDescriptor result) {
                                FileDescriptor descriptor = commitFileDescriptor(result);
                                setValue(descriptor);
                            }
                        };

                long fileSize = fileUploading.getFile(fileId).length();
                BackgroundWorkProgressWindow.show(uploadProgress, messages.getMainMessage("FileUploadField.uploadingFile"),
                        null, fileSize, true, true);
                break;
        }
    }

    protected FileDescriptor commitFileDescriptor(FileDescriptor fileDescriptor) {
        if (datasource != null) {
            return datasource.getDataSupplier().commit(fileDescriptor);
        }

        if (getFrame().getDsContext().getDataSupplier() != null) {
            return getFrame().getDsContext().getDataSupplier().commit(fileDescriptor);
        }

        return AppBeans.get(DataManager.class).commit(fileDescriptor);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        impl.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        impl.setHeight(height);
    }

    protected boolean hasInvalidExtension(String name) {
        Set<String> permittedExtensions = getPermittedExtensions();
        if (permittedExtensions != null && !permittedExtensions.isEmpty()) {
            if (name.lastIndexOf(".") > 0) {
                String fileExtension = name.substring(name.lastIndexOf("."), name.length());
                return !permittedExtensions.contains(fileExtension.toLowerCase());
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getFileName() {
        if (StringUtils.isEmpty(fileName))
            return null;

        String[] strings = fileName.split("[/\\\\]");
        return strings[strings.length - 1];
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
            throw new RuntimeException("Unable to read file content from temp storage", e);
        }

        return bytes;
    }

    @Override
    public UUID getFileId() {
        return fileId;
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

    @Override
    protected void setCaptionToComponent(String caption) {
        super.setCaptionToComponent(caption);
        
        impl.setCaption(caption);
        requestContainerUpdate();
    }

    @Override
    public <T> T getValue() {
        return (T) prevValue;
    }

    @Override
    public void setValue(Object value) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (!Objects.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent((FileDescriptor) value);
            fireChangeListeners(value);
        } else {
            updateComponent(prevValue);
        }

        if (value == null || !PersistenceHelper.isNew(value)) {
            fileId = null;
            tempFileId = null;
        }
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        impl.setEditable(editable);
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        impl.setRequired(required);
    }

    @Override
    public boolean isRequired() {
        return impl.isRequired();
    }

    @Override
    public void focus() {
        super.focus();
        uploadButton.focus();
    }

    @Override
    public void setDescription(String description) {
        impl.setDescription(description);
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
        requestContainerUpdate();
    }

    @Override
    public String getDescription() {
        return impl.getDescription();
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

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = (FileDescriptor) newValue;
        if (!Objects.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
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
    public String getAccept() {
        // do nothing
        return null;
    }

    @Override
    public void setAccept(String accept) {
        // do nothing
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }

        MetaClass metaClass = datasource.getMetaClass();
        resolveMetaPropertyPath(metaClass, property);

        itemChangeListener = e -> {
            if (updatingInstance)
                return;
            FileDescriptor descriptor = InstanceUtils.getValueEx(e.getItem(), metaPropertyPath.getPath());
            updateComponent(descriptor);
            fireChangeListeners(descriptor);
        };
        // noinspection unchecked
        datasource.addItemChangeListener(new WeakItemChangeListener(datasource, itemChangeListener));

        itemPropertyChangeListener = e -> {
            if (updatingInstance)
                return;
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                updateComponent((FileDescriptor) e.getValue());
                fireChangeListeners(e.getValue());
            }
        };
        // noinspection unchecked
        datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener));

        initRequired(metaPropertyPath);

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            FileDescriptor fileDescriptor = (FileDescriptor) newValue;
            updateComponent(fileDescriptor);
            fireChangeListeners(newValue);
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }

        handleFilteredAttributes(this, this.datasource, metaPropertyPath);
        securityItemChangeListener = e -> handleFilteredAttributes(this, this.datasource, metaPropertyPath);
        // noinspection unchecked
        this.datasource.addItemChangeListener(new WeakItemChangeListener(this.datasource, securityItemChangeListener));

        initBeanValidator();
    }

    protected void updateInstance(Object value) {
        updatingInstance = true;
        try {
            if (datasource != null && metaProperty != null && datasource.getItem() != null) {
                datasource.getItem().setValueEx(metaPropertyPath, value);
            }
        } finally {
            updatingInstance = false;
        }
    }

    protected void updateComponent(FileDescriptor fileDescriptor) {
        if (fileDescriptor != null) {
            impl.setFileName(fileDescriptor.getName());
        } else {
            impl.setFileName(null);
        }
    }

    @Override
    public boolean isShowFileName() {
        return impl.isShowFileName();
    }

    @Override
    public void setShowFileName(boolean showFileName) {
        impl.setShowFileName(showFileName);
    }

    @Override
    public FileStoragePutMode getMode() {
        return mode;
    }

    @Override
    public void setMode(FileStoragePutMode mode) {
        this.mode = mode;
    }

    /*
     * Upload button
     */
    @Override
    public String getUploadButtonCaption() {
        return impl.getUploadButtonCaption();
    }

    @Override
    public void setUploadButtonCaption(String caption) {
        impl.setUploadButtonCaption(caption);
    }

    @Override
    public void setUploadButtonIcon(String icon) {
        impl.setUploadButtonIcon(icon);
    }

    @Override
    public String getUploadButtonIcon() {
        return impl.getUploadButtonIcon();
    }

    @Override
    public void setUploadButtonDescription(String description) {
        impl.setUploadButtonDescription(description);
    }

    @Override
    public String getUploadButtonDescription() {
        return impl.getUploadButtonDescription();
    }

    /*
     * Clear button
     */
    @Override
    public void setShowClearButton(boolean showClearButton) {
        impl.setShowClearButton(showClearButton);
    }

    @Override
    public boolean isShowClearButton() {
        return impl.isShowClearButton();
    }

    @Override
    public void setClearButtonCaption(String caption) {
        impl.setClearButtonCaption(caption);
    }

    @Override
    public String getClearButtonCaption() {
        return impl.getClearButtonCaption();
    }

    @Override
    public void setClearButtonIcon(String icon) {
        impl.setClearButtonIcon(icon);
    }

    @Override
    public String getClearButtonIcon() {
        return impl.getClearButtonIcon();
    }

    @Override
    public void setClearButtonDescription(String description) {
        impl.setClearButtonDescription(description);
    }

    @Override
    public String getClearButtonDescription() {
        return impl.getClearButtonDescription();
    }

    @Override
    public void addBeforeValueClearListener(BeforeValueClearListener listener) {
        if (beforeValueClearListeners == null) {
            beforeValueClearListeners = new ArrayList<>();
        }
        if (!beforeValueClearListeners.contains(listener)) {
            beforeValueClearListeners.add(listener);
        }
    }

    @Override
    public void removeBeforeValueClearListener(BeforeValueClearListener listener) {
        if (beforeValueClearListeners != null) {
            beforeValueClearListeners.remove(listener);
        }
    }

    @Override
    public void addAfterValueClearListener(AfterValueClearListener listener) {
        if (afterValueClearListeners == null) {
            afterValueClearListeners = new ArrayList<>();
        }
        if (!afterValueClearListeners.contains(listener)) {
            afterValueClearListeners.add(listener);
        }
    }

    @Override
    public void removeAfterValueClearListener(AfterValueClearListener listener) {
        if (afterValueClearListeners != null) {
            afterValueClearListeners.remove(listener);
        }
    }

    @Override
    public void commit() {
        // do nothing
    }

    @Override
    public void discard() {
        // do nothing
    }

    @Override
    public boolean isBuffered() {
        // do nothing
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // do nothing
    }

    @Override
    public boolean isModified() {
        // do nothing
        return false;
    }
}