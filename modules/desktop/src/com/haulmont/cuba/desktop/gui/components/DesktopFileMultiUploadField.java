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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.compatibility.MultiUploadFieldListenerWrapper;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

import static com.haulmont.cuba.gui.upload.FileUploadingAPI.FileInfo;

/**
 */
public class DesktopFileMultiUploadField extends DesktopAbstractUploadComponent<JButton> implements FileMultiUploadField {

    protected static final String DEFAULT_ICON = "/components/multiupload/multiupload-button.png";

    protected FileUploadingAPI fileUploading;

    protected Map<UUID, String> filesMap = new HashMap<>();
    protected String icon;

    protected List<FileUploadStartListener> fileUploadStartListeners;         // lazily initialized list
    protected List<FileUploadFinishListener> fileUploadFinishListeners;       // lazily initialized list
    protected List<FileUploadErrorListener> fileUploadErrorListeners;         // lazily initialized list
    protected List<QueueUploadCompleteListener> queueUploadCompleteListeners; // lazily initialized list

    public DesktopFileMultiUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        DesktopResources resources = App.getInstance().getResources();
        Messages messages = AppBeans.get(Messages.NAME);
        String caption = messages.getMessage(getClass(), "upload.selectFiles");
        impl = new JButton();
        impl.setAction(new AbstractAction(caption, resources.getIcon(DEFAULT_ICON)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(impl) == JFileChooser.APPROVE_OPTION) {
                    processFiles(fileChooser.getSelectedFiles());
                }
            }
        });
        DesktopComponentsHelper.adjustSize(impl);
    }

    protected void processFiles(File[] files) {
        if (checkFiles(files)) {
            uploadFiles(files);
        }
    }

    protected void uploadFiles(File[] files) {
        for (File file : files) {
            try {
                fireFileUploadStart(file.getName(), file.length());

                FileInfo fileInfo = fileUploading.createFile();
                UUID tempFileId = fileInfo.getId();
                File tmpFile = fileInfo.getFile();

                FileUtils.copyFile(file, tmpFile);

                filesMap.put(tempFileId, file.getName());

                fireFileUploadFinish(file.getName(), file.length());
            } catch (Exception ex) {
                Messages messages = AppBeans.get(Messages.NAME);
                String uploadError = messages.formatMessage(DesktopFileMultiUploadField.class,
                        "multiupload.uploadError", file.getName());

                TopLevelFrame topLevelFrame = DesktopComponentsHelper.getTopLevelFrame(this);
                topLevelFrame.showNotification(uploadError, Frame.NotificationType.ERROR);

                fireFileUploadError(file.getName(), file.length(), ex);
            }
        }

        fireQueueUploadComplete();
    }

    protected boolean checkFiles(File[] files) {
        for (File file : files) {
            if (file.length() > getActualFileSizeLimit()) {
                notifyFileSizeExceedLimit(file);
                return false;
            }
        }
        return true;
    }

    protected void notifyFileSizeExceedLimit(File file) {
        Messages messages = AppBeans.get(Messages.NAME);
        String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", file.getName(), getFileSizeLimitString());
        getFrame().showNotification(warningMsg, Frame.NotificationType.WARNING);
    }

    @Override
    public void addListener(UploadListener listener) {
        MultiUploadFieldListenerWrapper wrapper = new MultiUploadFieldListenerWrapper(listener);

        addFileUploadStartListener(wrapper);
        addFileUploadFinishListener(wrapper);
        addFileUploadErrorListener(wrapper);
        addQueueUploadCompleteListener(wrapper);
    }

    @Override
    public void removeListener(UploadListener listener) {
        MultiUploadFieldListenerWrapper wrapper = new MultiUploadFieldListenerWrapper(listener);

        removeFileUploadStartListener(wrapper);
        removeFileUploadFinishListener(wrapper);
        removeFileUploadErrorListener(wrapper);
        removeQueueUploadCompleteListener(wrapper);
    }

    @Override
    public Map<UUID, String> getUploadsMap() {
        return Collections.unmodifiableMap(filesMap);
    }

    @Override
    public void clearUploads() {
        filesMap.clear();
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
        if (icon != null)
            impl.setIcon(App.getInstance().getResources().getIcon(icon));
        else
            impl.setIcon(null);
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

    protected void fireQueueUploadComplete() {
        if (queueUploadCompleteListeners != null) {
            for (QueueUploadCompleteListener listener : new ArrayList<>(queueUploadCompleteListeners)) {
                listener.queueUploadComplete();
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
    public void addQueueUploadCompleteListener(QueueUploadCompleteListener listener) {
        if (queueUploadCompleteListeners == null) {
            queueUploadCompleteListeners = new ArrayList<>();
        }
        if (!queueUploadCompleteListeners.contains(listener)) {
            queueUploadCompleteListeners.add(listener);
        }
    }

    @Override
    public void removeQueueUploadCompleteListener(QueueUploadCompleteListener listener) {
        if (queueUploadCompleteListeners != null) {
            queueUploadCompleteListeners.remove(listener);
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