/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.IFrame;
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

/**
 * @author budarov
 * @version $Id$
 */
public class DesktopFileUploadField extends DesktopAbstractComponent<JButton> implements FileUploadField {

    private static final int BYTES_IN_MEGABYTE = 1048576;

    protected FileUploadingAPI fileUploading;
    protected Messages messages;

    protected volatile boolean isUploadingState = false;

    protected String fileName;

    protected String description;

    protected UUID fileId;

    protected UUID tempFileId;

    protected List<Listener> listeners = new ArrayList<>();

    public DesktopFileUploadField() {
        fileUploading = AppBeans.get(FileUploadingAPI.NAME);
        messages = AppBeans.get(Messages.class);

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
        final Integer maxUploadSizeMb = AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getMaxUploadSizeMb();
        final long maxSize = maxUploadSizeMb * BYTES_IN_MEGABYTE;

        if (file.length() > maxSize) {
            String warningMsg = messages.formatMainMessage("upload.fileTooBig.message", file.getName(), maxSize);

            getFrame().showNotification(warningMsg, IFrame.NotificationType.WARNING);
        } else {
            boolean succcess = true;
            try {
                isUploadingState = true;

                fileName = file.getAbsolutePath();
                notifyListenersStart(file);

                tempFileId = fileUploading.createEmptyFile();

                File tmpFile = fileUploading.getFile(tempFileId);
                FileUtils.copyFile(file, tmpFile);

                fileId = tempFileId;

                isUploadingState = false;
            } catch (Exception ex) {
                succcess = false;
                try {
                    fileUploading.deleteFile(tempFileId);
                    tempFileId = null;
                } catch (FileStorageException e) {
                    throw new RuntimeException(ex);
                }
                notifyListenersFail(file);
            } finally {
                notifyListenersFinish(file);
            }
            if (succcess) {
                notifyListenersSuccess(file);
            }
        }
    }

    protected void notifyListenersSuccess(File file) {
        final Listener.Event e = new Listener.Event(file.getName());
        for (Listener listener : listeners) {
            listener.uploadSucceeded(e);
        }
    }

    protected void notifyListenersFail(File file) {
        final Listener.Event failedEvent = new Listener.Event(file.getName());
        for (Listener listener : listeners) {
            listener.uploadFailed(failedEvent);
        }
    }

    protected void notifyListenersFinish(File file) {
        final Listener.Event finishedEvent = new Listener.Event(file.getName());
        for (Listener listener : listeners) {
            listener.uploadFinished(finishedEvent);
        }
    }

    protected void notifyListenersStart(File file) {
        final Listener.Event startedEvent = new Listener.Event(file.getName());
        for (Listener listener : listeners) {
            listener.uploadStarted(startedEvent);
        }
    }

    @Override
    public String getFileName() {
        String[] strings = fileName.split("[/\\\\]");
        return strings[strings.length - 1];
    }

    @Override
    public FileDescriptor getFileDescriptor() {
        if (fileId != null)
            return fileUploading.getFileDescriptor(fileId, fileName);
        else
            return null;
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
            throw new RuntimeException(e);
        }

        return bytes;
    }

    @Override
    public UUID getFileId() {
        return fileId;
    }

    @Override
    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
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
}