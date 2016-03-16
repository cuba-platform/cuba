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

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.compatibility.FileUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.haulmont.cuba.gui.upload.FileUploadingAPI.FileInfo;

/**
 */
public class DesktopFileUploadField extends DesktopAbstractUploadComponent<JButton> implements FileUploadField {

    protected FileUploadingAPI fileUploading;
    protected Messages messages;

    protected String icon;

    protected volatile boolean isUploadingState = false;

    protected String fileName;

    protected String description;

    protected UUID fileId;

    protected UUID tempFileId;

    protected List<FileUploadStartListener> fileUploadStartListeners;     // lazily initialized list
    protected List<FileUploadFinishListener> fileUploadFinishListeners;   // lazily initialized list
    protected List<FileUploadSucceedListener> fileUploadSucceedListeners; // lazily initialized list
    protected List<FileUploadErrorListener> fileUploadErrorListeners;     // lazily initialized list

    public DesktopFileUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        messages = AppBeans.get(Messages.NAME);

        final JFileChooser fileChooser = new JFileChooser();
        String caption = messages.getMessage(getClass(), "export.selectFile");
        impl = new JButton();
        impl.setAction(new AbstractAction(caption) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(impl) == JFileChooser.APPROVE_OPTION) {
                    uploadFile(fileChooser.getSelectedFile());
                }
            }
        });
    }

    protected void uploadFile(File file) {
        if (file.length() > getActualFileSizeLimit()) {
            String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", file.getName(), getFileSizeLimitString());
            getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
        } else {
            boolean success = true;
            try {
                isUploadingState = true;

                fileName = file.getAbsolutePath();
                fireFileUploadStart(file.getName(), file.length());

                FileInfo fileInfo = fileUploading.createFile();
                tempFileId = fileInfo.getId();
                File tmpFile = fileInfo.getFile();

                FileUtils.copyFile(file, tmpFile);

                fileId = tempFileId;

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

    @Override
    public String getFileName() {
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
    public String getCaption() {
        return impl.getText();
    }

    @Override
    public void setCaption(String caption) {
        impl.setText(caption);
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null) {
            impl.setIcon(App.getInstance().getResources().getIcon(icon));
        } else {
            impl.setIcon(null);
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
    public String getAccept() {
        // do nothing
        return null;
    }

    @Override
    public void setAccept(String accept) {
        // do nothing
    }

    @Override
    public void setFileSizeLimit(long fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }
}